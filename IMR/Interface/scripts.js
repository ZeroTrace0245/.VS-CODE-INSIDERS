// Theme toggle and simple report fetcher
document.addEventListener('DOMContentLoaded', function () {
  const toggle = document.getElementById('theme-toggle');
  const icon = toggle && toggle.querySelector('.theme-icon');
  const stored = localStorage.getItem('gg_theme');

  // if user has saved pref, use it; otherwise follow system preference
  if (stored === 'dark') document.documentElement.classList.add('dark-theme');
  else if (!stored && window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
    document.documentElement.classList.add('dark-theme');
  }

  function updateIcon() {
    const isDark = document.documentElement.classList.contains('dark-theme');
    if (icon) icon.textContent = isDark ? '☀️' : '🌙';
    if (toggle) toggle.setAttribute('aria-pressed', isDark ? 'true' : 'false');
    if (toggle) toggle.title = isDark ? 'Switch to light theme' : 'Switch to dark theme';
  }

  updateIcon();

  toggle.addEventListener('click', () => {
    document.documentElement.classList.toggle('dark-theme');
    const isDark = document.documentElement.classList.contains('dark-theme');
    localStorage.setItem('gg_theme', isDark ? 'dark' : 'light');
    updateIcon();
  });

  // Report loader
  const loadBtn = document.getElementById('load-revenue');
  const out = document.getElementById('report-output');
  const start = document.getElementById('start-date');
  const end = document.getElementById('end-date');

  loadBtn.addEventListener('click', () => {
    const s = start.value || '';
    const e = end.value || '';
    out.textContent = 'Loading...';
    fetch(`api/get_reports.php?report=revenue&start=${encodeURIComponent(s)}&end=${encodeURIComponent(e)}`)
      .then(r => r.json())
      .then(data => {
        if (!data.success) {
          out.textContent = data.error || 'No data';
          return;
        }
        // render simple table
        const rows = data.rows || [];
        if (rows.length === 0) {
          out.textContent = 'No results for selected period.';
          return;
        }
        const table = document.createElement('table');
        table.style.width = '100%';
        table.style.borderCollapse = 'collapse';
        const thead = document.createElement('thead');
        thead.innerHTML = '<tr><th style="text-align:left;padding:8px">Utility</th><th style="text-align:right;padding:8px">Revenue</th></tr>';
        table.appendChild(thead);
        const tbody = document.createElement('tbody');
        rows.forEach(r => {
          const tr = document.createElement('tr');
          tr.innerHTML = `<td style="padding:8px">${r.utility}</td><td style="padding:8px;text-align:right">${parseFloat(r.revenue).toFixed(2)}</td>`;
          tbody.appendChild(tr);
        });
        table.appendChild(tbody);
        out.innerHTML = '';
        out.appendChild(table);
      })
      .catch(err => {
        out.textContent = 'Error loading report';
        console.error(err);
      });
  });

  // Mobile nav toggle
  const mobileToggle = document.getElementById('mobile-toggle');
  const navLinks = document.getElementById('nav-links');
  if (mobileToggle && navLinks) {
    mobileToggle.addEventListener('click', () => {
      navLinks.classList.toggle('open');
    });
  }
});
