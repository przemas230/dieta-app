const CACHE_NAME = "dieta-app-v16";
const ASSETS = ["./index.html", "./manifest.json", "./icon-192.png", "./icon-512.png"];

self.addEventListener("install", (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => cache.addAll(ASSETS))
  );
  self.skipWaiting();
});

self.addEventListener("activate", (event) => {
  event.waitUntil(
    caches.keys().then((keys) =>
      Promise.all(keys.filter((k) => k !== CACHE_NAME).map((k) => caches.delete(k)))
    )
  );
  self.clients.claim();
});

self.addEventListener("fetch", (event) => {
  event.respondWith(
    caches.match(event.request).then((cached) => {
      const fetchPromise = fetch(event.request)
        .then((networkResponse) => {
          if (event.request.method === "GET" && networkResponse && networkResponse.status === 200) {
            const clone = networkResponse.clone();
            caches.open(CACHE_NAME).then((cache) => cache.put(event.request, clone));
          }
          return networkResponse;
        })
        .catch(() => cached);
      return cached || fetchPromise;
    })
  );
});

// ---------- WATER TRACKER: notification quick-actions ----------
const WATER_DB_NAME = "dieta-water-db";
function openWaterDB(){
  return new Promise((resolve, reject) => {
    const req = indexedDB.open(WATER_DB_NAME, 1);
    req.onupgradeneeded = () => { req.result.createObjectStore("kv"); };
    req.onsuccess = () => resolve(req.result);
    req.onerror = () => reject(req.error);
  });
}
async function getKV(key){
  const db = await openWaterDB();
  return new Promise((resolve) => {
    const tx = db.transaction("kv", "readonly");
    const req = tx.objectStore("kv").get(key);
    req.onsuccess = () => resolve(req.result);
    req.onerror = () => resolve(undefined);
  });
}
async function setKV(key, val){
  const db = await openWaterDB();
  return new Promise((resolve) => {
    const tx = db.transaction("kv", "readwrite");
    tx.objectStore("kv").put(val, key);
    tx.oncomplete = () => resolve();
    tx.onerror = () => resolve();
  });
}
function todayStr(){ return new Date().toISOString().slice(0, 10); }

function dropletsText(count){
  let out = "";
  for(let i=0;i<8;i++) out += (i<count ? "💧" : "⚪");
  return out;
}
function clampCount(n){
  n = Number(n);
  if(!Number.isFinite(n)) n = 0;
  return Math.max(0, Math.min(8, n));
}
async function pushWaterNotification(count){
  await self.registration.showNotification("💧 Nawodnienie", {
    body: `${dropletsText(count)}\n${count} / 8 szklanek`,
    tag: "water-tracker",
    silent: true,
    requireInteraction: true,
    actions: [{ action: "add-water", title: "+1 💧" }, { action: "remove-water", title: "-1 ↩️" }],
    icon: "icon-192.png"
  });
}
async function handleWaterTrackerAction(action){
  const today = todayStr();
  const storedDate = await getKV("waterDate");
  let count = Number(await getKV("pendingWater"));
  if (!Number.isFinite(count) || storedDate !== today) count = 0;
  count = action === "add-water" ? clampCount(count + 1) : clampCount(count - 1);
  await setKV("waterDate", today);
  await setKV("pendingWater", count);
  const clientsList = await self.clients.matchAll({ type: "window", includeUncontrolled: true });
  clientsList.forEach((c) => c.postMessage({ type: "water-add", count }));
  await pushWaterNotification(count);
}
function focusOrOpenApp(){
  return self.clients.matchAll({ type: "window" }).then((clientsList) => {
    if (clientsList.length > 0) return clientsList[0].focus();
    return self.clients.openWindow("./index.html");
  });
}

// ---------- WATER REMINDER: periodic drink-water alarm ----------
function parseHM(str){
  const parts = (str || "08:00").split(":");
  const h = parseInt(parts[0], 10) || 0, m = parseInt(parts[1], 10) || 0;
  return h * 60 + m;
}
function isActiveMinute(minutesOfDay, fromMin, toMin){
  if (fromMin === toMin) return true;
  if (fromMin < toMin) return minutesOfDay >= fromMin && minutesOfDay < toMin;
  return minutesOfDay >= fromMin || minutesOfDay < toMin;
}
function computeNextReminderAt(fromMs, cfg){
  const intervalMs = Math.max(15, Number(cfg && cfg.intervalMinutes) || 90) * 60000;
  let next = fromMs + intervalMs;
  const fromMin = parseHM(cfg && cfg.activeFrom), toMin = parseHM(cfg && cfg.activeTo);
  const d = new Date(next);
  const minutesOfDay = d.getHours() * 60 + d.getMinutes();
  if (!isActiveMinute(minutesOfDay, fromMin, toMin)) {
    d.setHours(Math.floor(fromMin / 60), fromMin % 60, 0, 0);
    if (d.getTime() <= next) d.setDate(d.getDate() + 1);
    next = d.getTime();
  }
  return next;
}
async function pushWaterReminderNotification(){
  await self.registration.showNotification("💧 Czas się napić wody!", {
    body: "Krótkie przypomnienie o nawodnieniu.",
    tag: "water-reminder",
    icon: "icon-192.png",
    actions: [{ action: "snooze-water", title: "Odłóż 15 min" }, { action: "skip-water", title: "Pomiń do następnego" }]
  });
}

self.addEventListener("notificationclick", (event) => {
  const notif = event.notification;

  if (notif.tag === "water-tracker") {
    if (event.action === "add-water" || event.action === "remove-water") {
      // Intentionally not closing: the tracker notification stays on screen
      // until the user dismisses it manually, so repeated taps keep working.
      event.waitUntil(handleWaterTrackerAction(event.action));
      return;
    }
    notif.close();
    event.waitUntil(focusOrOpenApp());
    return;
  }

  if (notif.tag === "water-reminder") {
    notif.close();
    event.waitUntil((async () => {
      const cfg = (await getKV("reminderConfig")) || {};
      const nextAt = event.action === "snooze-water"
        ? Date.now() + 15 * 60000
        : computeNextReminderAt(Date.now(), cfg);
      await setKV("reminderNextAt", nextAt);
      const clientsList = await self.clients.matchAll({ type: "window", includeUncontrolled: true });
      clientsList.forEach((c) => c.postMessage({ type: "water-reminder-updated", nextAt }));
      if (!event.action) await focusOrOpenApp();
    })());
    return;
  }

  notif.close();
  event.waitUntil(focusOrOpenApp());
});
