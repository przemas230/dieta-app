const CACHE_NAME = "dieta-app-v25";
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
    renotify: false,
    requireInteraction: true,
    // Order swapped from the "obvious" add-then-remove: the diagnostic log
    // showed every single tap on the button labeled "+1" (always the first
    // declared action, i.e. the one the user taps expecting to add) coming
    // back as action=remove-water — 7 times in a row, never once
    // add-water. Whatever the underlying mechanism (this Android/Chrome
    // build renders the two actions in reverse of declaration order, or
    // consistently reports the last declared action for the first button —
    // both fit the evidence equally), moving add-water into the slot that
    // was actually firing puts the right action behind the button the user
    // taps expecting +1.
    actions: [{ action: "remove-water", title: "-1 ↩️" }, { action: "add-water", title: "+1 💧" }],
    icon: "icon-192.png"
  });
}
// Guards against a single physical tap being dispatched as two
// 'notificationclick' events — a real, documented Android/Chrome bug for web
// push action buttons on some OS/browser builds. Without this, that duplicate
// event silently applies the action twice (e.g. +1 landing as +2, which then
// looks like the wrong button fired when the user taps again against a count
// that's already ahead of what they expected).
let lastWaterActionAt = 0;
// Diagnostic trail for the water-notification buttons: every dispatch this
// SW instance actually receives gets appended here (kept to the last 20),
// visible in Ustawienia. Every previous fix for "+1 acts like -1" checked out
// on static review of this exact logic, so at this point the only way
// forward is to see, on the device where it happens, what event.action and
// CACHE_NAME this SW instance is really running — instead of guessing again.
async function logWaterAction(entry){
  const log = (await getKV("waterActionLog")) || [];
  log.push(entry);
  while(log.length > 20) log.shift();
  await setKV("waterActionLog", log);
}
async function handleWaterTrackerAction(action){
  const now = Date.now();
  // Short window: long enough to swallow a same-tap duplicate dispatch
  // (those land within single-digit-to-low-double-digit ms), short enough
  // to never eat a real second tap — lifting and re-pressing a finger takes
  // people well over 150ms even when double-tapping quickly on purpose.
  if (now - lastWaterActionAt < 150){
    await logWaterAction({t: now, action, swCache: CACHE_NAME, result: "swallowed-duplicate"});
    return;
  }
  lastWaterActionAt = now;
  const today = todayStr();
  const storedDate = await getKV("waterDate");
  let count = Number(await getKV("pendingWater"));
  if (!Number.isFinite(count) || storedDate !== today) count = 0;
  const countBefore = count;
  count = action === "add-water" ? clampCount(count + 1) : clampCount(count - 1);
  await setKV("waterDate", today);
  await setKV("pendingWater", count);
  await logWaterAction({t: now, action, swCache: CACHE_NAME, countBefore, countAfter: count});
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
