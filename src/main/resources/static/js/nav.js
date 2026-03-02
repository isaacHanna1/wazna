document.addEventListener('DOMContentLoaded', function () {

  var btn     = document.getElementById('mobile-menu-button');
  var nav     = document.getElementById('navbar-nav');
  var overlay = document.getElementById('nav-overlay');
  if (!btn || !nav) return;

  function isMobile() { return window.innerWidth <= 900; }

  function openNav() {
    nav.classList.add('open');
    if (overlay) overlay.classList.add('visible');
  }
  function closeNav() {
    nav.classList.remove('open');
    if (overlay) overlay.classList.remove('visible');
    closeAll();
  }
  function closeAll() {
    nav.querySelectorAll('.touch-open').forEach(function (el) {
      el.classList.remove('touch-open');
    });
  }

  btn.addEventListener('click', function (e) {
    e.stopPropagation();
    nav.classList.contains('open') ? closeNav() : openNav();
  });
  if (overlay) overlay.addEventListener('click', closeNav);
  document.addEventListener('click', function (e) {
    if (!nav.contains(e.target) && !btn.contains(e.target)) closeNav();
  });
  nav.addEventListener('click', function (e) { e.stopPropagation(); });
  window.addEventListener('resize', function () { if (!isMobile()) closeAll(); });

  /* ══════════════════════════════
     LEVEL 1 — main-menu icon tap
     Attach to the icon <a> trigger only
  ══════════════════════════════ */
  var mainMenus = Array.prototype.slice.call(nav.querySelectorAll('.main-menu'));

  mainMenus.forEach(function (menu) {
    var sub     = menu.querySelector(':scope > .sub-menu');
    var trigger = menu.querySelector(':scope > .nav-link');  // the icon <a>
    if (!sub || !trigger) return;

    trigger.addEventListener('touchstart', function (e) {
      if (!isMobile()) return;
      e.stopPropagation();
      e.preventDefault();  // safe — this <a> has href="#"

      var isOpen = menu.classList.contains('touch-open');
      mainMenus.forEach(function (m) { m.classList.remove('touch-open'); });

      if (!isOpen) {
        // measure height briefly
        sub.style.visibility   = 'hidden';
        sub.style.display      = 'block';
        var menuH = sub.offsetHeight || 180;
        sub.style.display      = '';
        sub.style.visibility   = '';

        var rect = menu.getBoundingClientRect();
        var vpH  = window.innerHeight;
        var top  = Math.max(8, Math.min(rect.top, vpH - menuH - 8));
        sub.style.top = top + 'px';
        menu.classList.add('touch-open');
      }
    }, { passive: false });
  });

  /* ══════════════════════════════
     LEVEL 2 — parent row tap
     Attach ONLY to .nav-link--parent span, 
     NOT to the nav-item (which contains real <a> links)
  ══════════════════════════════ */
  nav.querySelectorAll('.sub-menu > .nav-item').forEach(function (item) {
    var subsub  = item.querySelector(':scope > .sub-sub-menu');
    var trigger = item.querySelector(':scope > .nav-link--parent');
    if (!subsub || !trigger) return;

    trigger.addEventListener('touchstart', function (e) {
      if (!isMobile()) return;
      e.stopPropagation();
      e.preventDefault();  // safe — this <span> has no href

      var isOpen = item.classList.contains('touch-open');
      if (item.parentElement) {
        item.parentElement.querySelectorAll(':scope > .nav-item.touch-open')
          .forEach(function (o) { o.classList.remove('touch-open'); });
      }
      if (!isOpen) item.classList.add('touch-open');
    }, { passive: false });

    /* ── The <a> links INSIDE sub-sub-menu must navigate freely ──
       Make sure no ancestor swallows their touch/click           */
    subsub.querySelectorAll('a').forEach(function (link) {
      link.addEventListener('touchend', function (e) {
        // Let the browser handle this as a normal click/navigation
        e.stopPropagation();
        // Do NOT preventDefault — we want the link to work
      });
    });
  });

});