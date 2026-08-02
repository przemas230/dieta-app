const CACHE_NAME = "dieta-app-v12";
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

self.addEventListener("notificationclick", (event) => {
  event.notification.close();
  if (event.action === "add-water") {
    event.waitUntil((async () => {
      const today = todayStr();
      const storedDate = await getKV("waterDate");
      let count = await getKV("pendingWater");
      if (storedDate !== today) count = 0;
      count = Math.min(8, (count || 0) + 1);
      await setKV("waterDate", today);
      await setKV("pendingWater", count);
      const clientsList = await self.clients.matchAll({ type: "window", includeUncontrolled: true });
      clientsList.forEach((c) => c.postMessage({ type: "water-add", count }));
      await self.registration.showNotification("💧 Nawodnienie", {
        body: `Wypito dziś: ${count} / 8 szklanek`,
        tag: "water-tracker",
        silent: true,
        actions: [{ action: "add-water", title: "+1 szklanka 💧" }],
        icon: "icon-192.png"
      });
    })());
  } else {
    event.waitUntil(
      self.clients.matchAll({ type: "window" }).then((clientsList) => {
        if (clientsList.length > 0) return clientsList[0].focus();
        return self.clients.openWindow("./index.html");
      })
    );
  }
});
