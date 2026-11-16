// SaeSubam Matrimony Service Worker
const CACHE_NAME = 'saesubam-cache-v1';
const OFFLINE_URL = '/offline.html';

const PRECACHE_ASSETS = [
  '/',
  OFFLINE_URL,
  '/manifest.json',
  '/images/favicon.svg',
  '/images/icons/icon-192.png',
  '/images/icons/icon-512.png'
];

// Install: Cache essential assets & offline fallback
self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      return cache.addAll(PRECACHE_ASSETS).catch((err) => {
        console.warn('Precache partial fallback:', err);
      });
    }).then(() => self.skipWaiting())
  );
});

// Activate: Clean up older cache versions
self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((keys) => {
      return Promise.all(
        keys.map((key) => {
          if (key !== CACHE_NAME) {
            return caches.delete(key);
          }
        })
      );
    }).then(() => self.clients.claim())
  );
});

// Fetch strategy
self.addEventListener('fetch', (event) => {
  const request = event.request;
  const url = new URL(request.url);

  // Ignore non-GET requests (e.g. POST form submissions)
  if (request.method !== 'GET') {
    return;
  }

  // Handle HTML navigation (pages)
  if (request.mode === 'navigate') {
    event.respondWith(
      fetch(request)
        .catch(async () => {
          const cache = await caches.open(CACHE_NAME);
          const cachedResponse = await cache.match(request);
          if (cachedResponse) {
            return cachedResponse;
          }
          const offlinePage = await cache.match(OFFLINE_URL);
          if (offlinePage) {
            return offlinePage;
          }
          return new Response(
            '<html><body style="font-family:sans-serif;text-align:center;padding:50px;"><h2>Offline</h2><p>Please check your internet connection.</p></body></html>',
            { headers: { 'Content-Type': 'text/html' } }
          );
        })
    );
    return;
  }

  // Handle static assets (images, icons, styles)
  if (
    url.pathname.startsWith('/images/') ||
    url.pathname.endsWith('.css') ||
    url.pathname.endsWith('.js') ||
    url.pathname.endsWith('.svg') ||
    url.pathname.endsWith('.png') ||
    url.pathname.endsWith('.ico')
  ) {
    event.respondWith(
      caches.match(request).then((cachedResponse) => {
        const fetchPromise = fetch(request).then((networkResponse) => {
          if (networkResponse && networkResponse.status === 200) {
            const clone = networkResponse.clone();
            caches.open(CACHE_NAME).then((cache) => cache.put(request, clone));
          }
          return networkResponse;
        }).catch(() => cachedResponse);

        return cachedResponse || fetchPromise;
      })
    );
    return;
  }

  // Default network fetch
  event.respondWith(
    fetch(request).catch(() => caches.match(request))
  );
});
