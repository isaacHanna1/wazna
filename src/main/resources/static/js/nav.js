document.addEventListener('DOMContentLoaded', function () {
  var btn     = document.getElementById('mobile-menu-button');
  var nav     = document.getElementById('navbar-nav');
  var overlay = document.getElementById('nav-overlay');

  if (!btn || !nav) return;

  var SIDEBAR_W = 66;

  function isMobile() { return window.innerWidth <= 900; }

  /* ── Sidebar ── */
  function openNav()  { nav.classList.add('open');    if (overlay) overlay.classList.add('visible'); }
  function closeNav() { nav.classList.remove('open'); if (overlay) overlay.classList.remove('visible'); closeAll(); }
  function closeAll() { nav.querySelectorAll('.touch-open').forEach(function(el){ el.classList.remove('touch-open'); }); }

  btn.addEventListener('click', function(e) {
    e.stopPropagation();
    nav.classList.contains('open') ? closeNav() : openNav();
  });
  if (overlay) overlay.addEventListener('click', closeNav);
  document.addEventListener('click', function(e) {
    if (!nav.contains(e.target) && !btn.contains(e.target)) closeNav();
  });
  nav.addEventListener('click', function(e) { e.stopPropagation(); });
  window.addEventListener('resize', function() { if (!isMobile()) closeAll(); });

  /* ════════════════════════════════
     POSITION HELPERS
     Strategy: make the menu visible (block) with opacity:0 first,
     measure it, set coordinates, then restore opacity.
     This ensures getBoundingClientRect() returns real dimensions.
  ════════════════════════════════ */

  function showAndPosition(menu, setCoords) {
    // Temporarily show to allow measurement
    menu.style.opacity  = '0';
    menu.style.display  = 'block';
    setCoords(menu);
    menu.style.opacity  = '';
    menu.style.display  = '';  // let CSS hover/touch-open control display
  }

  /* Level 1: position sub-menu below trigger item */
  function positionSub(triggerItem, sub) {
    var r = triggerItem.getBoundingClientRect();
    if (isMobile()) {
      sub.style.top   = r.top + 'px';
      sub.style.right = SIDEBAR_W + 'px';
      sub.style.left  = 'auto';
    } else {
      sub.style.top   = (r.bottom + 4) + 'px';
      sub.style.right = (window.innerWidth - r.right) + 'px';
      sub.style.left  = 'auto';
    }
  }

  /* Level 2: position sub-sub-menu to the left of the sub-menu */
  function positionSubSub(triggerRow, subsub, parentSub) {
    var rowRect = triggerRow.getBoundingClientRect();

    if (isMobile()) {
      // Mobile: right = sidebar(66) + sub-menu min-width(175) = 241px
      subsub.style.top   = rowRect.top + 'px';
      subsub.style.right = (SIDEBAR_W + 175) + 'px';  // 241px
      subsub.style.left  = 'auto';
    } else {
      // Desktop: sub-menu IS visible when this runs — measure it directly
      var subRect = parentSub.getBoundingClientRect();
      subsub.style.top   = rowRect.top + 'px';
      subsub.style.right = (window.innerWidth - subRect.left + 4) + 'px';
      subsub.style.left  = 'auto';
    }
  }

  /* ── Level 1: main-menu items ── */
  var mainItems = Array.prototype.slice.call(nav.querySelectorAll('.main-menu'));

  mainItems.forEach(function(item) {
    var sub = item.querySelector(':scope > .sub-menu');
    if (!sub) return;

    item.addEventListener('mouseenter', function() {
      positionSub(item, sub);
    });

    item.addEventListener('touchstart', function(e) {
      if (!isMobile()) return;
      var isOpen = item.classList.contains('touch-open');
      mainItems.forEach(function(o) { if (o !== item) o.classList.remove('touch-open'); });
      nav.querySelectorAll('.sub-menu > .nav-item.touch-open').forEach(function(o){ o.classList.remove('touch-open'); });
      if (isOpen) {
        item.classList.remove('touch-open');
      } else {
        positionSub(item, sub);
        item.classList.add('touch-open');
        e.preventDefault();
      }
    }, { passive: false });
  });

  /* ── Level 2: sub-menu rows with sub-sub-menus ── */
  nav.querySelectorAll('.sub-menu > .nav-item').forEach(function(item) {
    var subsub    = item.querySelector(':scope > .sub-sub-menu');
    var parentSub = item.closest('.sub-menu');
    if (!subsub || !parentSub) return;

    item.addEventListener('mouseenter', function() {
      positionSubSub(item, subsub, parentSub);
    });

    item.addEventListener('touchstart', function(e) {
      if (!isMobile()) return;
      var isOpen = item.classList.contains('touch-open');
      if (item.parentElement) {
        item.parentElement.querySelectorAll(':scope > .nav-item.touch-open').forEach(function(o){
          if (o !== item) o.classList.remove('touch-open');
        });
      }
      if (isOpen) {
        item.classList.remove('touch-open');
      } else {
        positionSubSub(item, subsub, parentSub);
        item.classList.add('touch-open');
        e.stopPropagation();
        e.preventDefault();
      }
    }, { passive: false });
  });

  /* Reposition on scroll (sticky navbar shifts) */
  window.addEventListener('scroll', function() {
    mainItems.forEach(function(item) {
      var sub = item.querySelector(':scope > .sub-menu');
      if (!sub) return;
      if (item.matches(':hover') || item.classList.contains('touch-open')) {
        positionSub(item, sub);
      }
    });
  }, { passive: true });
});