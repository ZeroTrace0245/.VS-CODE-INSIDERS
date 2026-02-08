function getCsrfToken() {
  const match = document.cookie.match(/XSRF-TOKEN=([^;]+)/);
  return match ? decodeURIComponent(match[1]) : null;
}

async function apiFetch(url, options = {}) {
  const headers = options.headers || {};
  const csrf = getCsrfToken();
  if (csrf) headers["X-XSRF-TOKEN"] = csrf;
  headers["Content-Type"] = headers["Content-Type"] || "application/json";
  const res = await fetch(url, { credentials: "include", ...options, headers });
  if (!res.ok) {
    const body = await res.json().catch(() => ({}));
    const message = body.error || res.statusText;
    throw new Error(message);
  }
  if (res.status === 204) return null;
  const ct = res.headers.get("content-type") || "";
  return ct.includes("application/json") ? res.json() : res.text();
}

function showAlert(containerId, message, type = "error") {
  const el = document.getElementById(containerId);
  if (!el) return;
  el.innerHTML = `<div class="alert ${type === "success" ? "success" : "error"}">${message}</div>`;
}

function clearAlert(containerId) {
  const el = document.getElementById(containerId);
  if (el) el.innerHTML = "";
}

function setActiveNav(path) {
  document.querySelectorAll(".nav a").forEach((a) => {
    if (a.getAttribute("href") === path) {
      a.classList.add("active");
    }
  });
}
