package com.przemas230.dietaapp.ui

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

private const val VERSION_MANIFEST_URL =
    "https://raw.githubusercontent.com/przemas230/dieta-app/main/android/dist/version.json"
private const val DEFAULT_APK_URL =
    "https://raw.githubusercontent.com/przemas230/dieta-app/main/android/dist/app-debug.apk"

sealed class UpdateState {
    data object Idle : UpdateState()
    data object Checking : UpdateState()
    data class UpToDate(val versionName: String) : UpdateState()
    data class UpdateAvailable(val versionName: String, val versionCode: Long, val apkUrl: String) : UpdateState()
    data object Downloading : UpdateState()
    data class Error(val message: String) : UpdateState()
}

/**
 * Manual "check for update" for the sideloaded debug build we distribute via
 * android/dist/ (see dist/README.md) — there's no Play Store auto-update
 * path for this APK. Compares the installed versionCode against
 * dist/version.json on GitHub (kept in sync with dist/app-debug.apk by hand
 * on every release commit — see CLAUDE.md's Android section), downloads the
 * new APK into the app's cache dir, then hands it to the system package
 * installer via a FileProvider content:// URI.
 *
 * This can get the user to one tap on "Zainstaluj" — it can't skip that tap.
 * Android requires the system installer's own confirmation for any APK not
 * coming from an already-trusted store, even with REQUEST_INSTALL_PACKAGES
 * granted; only that permission (Ustawienia → Aplikacje → Dieta App →
 * Instaluj nieznane aplikacje) is what this app can ask the user for ahead
 * of time so the installer doesn't also have to stop and ask that first.
 */
class AppUpdateViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow<UpdateState>(UpdateState.Idle)
    val state: StateFlow<UpdateState> = _state.asStateFlow()

    val installedVersionName: String
    private val installedVersionCode: Long

    init {
        val context = getApplication<Application>()
        val info = context.packageManager.getPackageInfo(context.packageName, 0)
        installedVersionName = info.versionName ?: "?"
        installedVersionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            info.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            info.versionCode.toLong()
        }
    }

    fun checkForUpdate() {
        _state.value = UpdateState.Checking
        viewModelScope.launch {
            try {
                val body = withContext(Dispatchers.IO) { httpGet(VERSION_MANIFEST_URL) }
                val json = JSONObject(body)
                val remoteVersionCode = json.getLong("versionCode")
                val remoteVersionName = json.getString("versionName")
                val apkUrl = json.optString("apkUrl", DEFAULT_APK_URL)
                _state.value = if (remoteVersionCode > installedVersionCode) {
                    UpdateState.UpdateAvailable(remoteVersionName, remoteVersionCode, apkUrl)
                } else {
                    UpdateState.UpToDate(remoteVersionName)
                }
            } catch (e: Exception) {
                _state.value = UpdateState.Error("Nie udało się sprawdzić aktualizacji: ${e.message ?: e.toString()}")
            }
        }
    }

    fun downloadAndInstall(apkUrl: String) {
        _state.value = UpdateState.Downloading
        viewModelScope.launch {
            try {
                val context = getApplication<Application>()
                val file = withContext(Dispatchers.IO) { downloadApk(context.cacheDir, apkUrl) }
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/vnd.android.package-archive")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
                _state.value = UpdateState.Idle
            } catch (e: Exception) {
                _state.value = UpdateState.Error("Nie udało się pobrać/zainstalować aktualizacji: ${e.message ?: e.toString()}")
            }
        }
    }

    private fun httpGet(url: String): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connectTimeout = 10_000
        connection.readTimeout = 10_000
        return try {
            connection.inputStream.bufferedReader().use { it.readText() }
        } finally {
            connection.disconnect()
        }
    }

    private fun downloadApk(cacheDir: File, apkUrl: String): File {
        val updatesDir = File(cacheDir, "updates").apply { mkdirs() }
        val target = File(updatesDir, "dieta-app-update.apk")
        val connection = URL(apkUrl).openConnection() as HttpURLConnection
        connection.connectTimeout = 15_000
        connection.readTimeout = 30_000
        try {
            connection.inputStream.use { input ->
                target.outputStream().use { output -> input.copyTo(output) }
            }
        } finally {
            connection.disconnect()
        }
        return target
    }
}
