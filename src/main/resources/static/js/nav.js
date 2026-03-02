/**
 * nav.js — Navbar interactions
 * Desktop : pure CSS hover (position:absolute)
 * Mobile  : icon-only sidebar (66px), dropdowns fly LEFT via JS
 */
document.addEventListener('DOMContentLoaded', function () {

  var btn     = document.getElementById('mobile-menu-button');
  var nav     = document.getElementById('navbar-nav');
  var overlay = document.getElementById('nav-overlay');

  if (!btn || !nav) return;

  var SIDEBAR_W = 56;   // matches .navbar-nav width on mobile
  var SUBMENU_W = 200;  // matches .sub-menu width on mobile

  function isMobile() { return window.innerWidth <= 900; }

  /* ── Sidebar open / close ── */
  function openNav() {
    nav.classList.add('open');
    if (overlay) overlay.classList.add('visible');
  }
  function closeNav() {
    nav.classList.remove('open');
    if (overlay) overlay.classList.remove('visible');
    closeAllDropdowns();
  }
  function closeAllDropdowns() {
    nav.querySelectorAll('.touch-open').forEach(function (el) {
      el.classList.remove('touch-open');
      // clear JS-set coords
      var sub = el.querySelector(':scope > .sub-menu, :scope > .sub-sub-menu');
      if (sub) { sub.style.top = ''; sub.style.right = ''; }
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

  window.addEventListener('resize', function () {
    if (!isMobile()) closeAllDropdowns();
  });

  /* ══════════════════════
     POSITION HELPER
     Places a fixed dropdown to the LEFT of the sidebar.
     Uses `right` offset from viewport edge.
     Clamps top so it never overflows viewport.
  ══════════════════════ */
  function positionDropdown(dropdown, triggerRect, rightOffset) {
    var vpH   = window.innerHeight;
    var menuH = dropdown.offsetHeight || 200;

    // Align top with trigger icon top
    var idealTop = triggerRect.top;
    // Clamp within viewport
    var top = Math.min(idealTop, vpH - menuH - 8);
    top = Math.max(top, 8);

    dropdown.style.top   = top + 'px';
    dropdown.style.right = rightOffset + 'px';
    dropdown.style.left  = '';  // clear any stale left value
  }

  /* ══════════════════════
     LEVEL 1 — main-menu tap
  ══════════════════════ */
  var mainMenus = Array.prototype.slice.call(nav.querySelectorAll('.main-menu'));

  mainMenus.forEach(function (menu) {
    var sub = menu.querySelector(':scope > .sub-menu');
    if (!sub) return;

    menu.addEventListener('touchstart', function (e) {
      if (!isMobile()) return;
      e.stopPropagation();
      e.preventDefault();

      var isOpen = menu.classList.contains('touch-open');

      // Close all level-1
      mainMenus.forEach(function (m) {
        m.classList.remove('touch-open');
        var s = m.querySelector(':scope > .sub-menu');
        if (s) { s.style.top = ''; s.style.right = ''; }
      });
      // Close all level-2
      nav.querySelectorAll('.sub-menu > .nav-item.touch-open').forEach(function (item) {
        item.classList.remove('touch-open');
        var ss = item.querySelector(':scope > .sub-sub-menu');
        if (ss) { ss.style.top = ''; ss.style.right = ''; }
      });

      if (!isOpen) {
        // Make visible briefly to measure height
        sub.style.visibility = 'visible';
        sub.style.opacity    = '0';
        var rect = menu.getBoundingClientRect();
        positionDropdown(sub, rect, SIDEBAR_W);
        sub.style.visibility = '';
        sub.style.opacity    = '';
        menu.classList.add('touch-open');
      }
    }, { passive: false });
  });

  /* ══════════════════════
     LEVEL 2 — sub-menu item tap
  ══════════════════════ */
  nav.querySelectorAll('.sub-menu > .nav-item').forEach(function (item) {
    var subsub = item.querySelector(':scope > .sub-sub-menu');
    if (!subsub) return;

    item.addEventListener('touchstart', function (e) {
      if (!isMobile()) return;
      e.stopPropagation();
      e.preventDefault();

      var isOpen = item.classList.contains('touch-open');

      // Close siblings
      if (item.parentElement) {
        item.parentElement.querySelectorAll(':scope > .nav-item.touch-open').forEach(function (o) {
          o.classList.remove('touch-open');
          var ss = o.querySelector(':scope > .sub-sub-menu');
          if (ss) { ss.style.top = ''; ss.style.right = ''; }
        });
      }

      if (!isOpen) {
        subsub.style.visibility = 'visible';
        subsub.style.opacity    = '0';
        var rect = item.getBoundingClientRect();
        positionDropdown(subsub, rect, SIDEBAR_W + SUBMENU_W);
        subsub.style.visibility = '';
        subsub.style.opacity    = '';
        item.classList.add('touch-open');
      }
    }, { passive: false });
  });

});