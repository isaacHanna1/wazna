/**
 * marketDashboard.js
 * Fetches data from 3 API endpoints on sprint change.
 * Handles live status update via PATCH per cart item.
 */
(function () {
  'use strict';

  /* ── State ── */
  const state = { sprintId: null, byItem: [], bySupplier: [], byUser: [] };

  /* ── DOM ── */
  const sprintSelect    = document.getElementById('sprintSelect');
  const loader          = document.getElementById('mdbLoader');
  const statTotalItems  = document.getElementById('statTotalItems');
  const statTotalPoints = document.getElementById('statTotalPoints');
  const statSuppliers   = document.getElementById('statSuppliers');
  const statPending     = document.getElementById('statPending');
  const bodyByItem      = document.getElementById('bodyByItem');
  const bodyBySupplier  = document.getElementById('bodyBySupplier');
  const bodyByUser      = document.getElementById('bodyByUser');
  const searchItem      = document.getElementById('searchItem');
  const searchSupplier  = document.getElementById('searchSupplier');
  const searchUser      = document.getElementById('searchUser');

  /* ── Tabs ── */
  document.querySelectorAll('.mdb-tab').forEach(tab => {
    tab.addEventListener('click', () => {
      document.querySelectorAll('.mdb-tab').forEach(t => t.classList.remove('mdb-tab--active'));
      document.querySelectorAll('.mdb-panel').forEach(p => p.classList.remove('mdb-panel--active'));
      tab.classList.add('mdb-tab--active');
      document.getElementById('panel-' + tab.dataset.tab).classList.add('mdb-panel--active');
    });
  });

  /* ── Sprint change ── */
  sprintSelect.addEventListener('change', () => {
    state.sprintId = sprintSelect.value;
    loadAll();
  });

  /* ── Live search ── */
searchItem.addEventListener('input',     () => renderByItem(filter(state.byItem, searchItem.value, ['itemName', 'supplierName'])));
searchSupplier.addEventListener('input', () => renderBySupplier(filter(state.bySupplier, searchSupplier.value, ['supplierName'])));
searchUser.addEventListener('input',     () => renderByUser(filter(state.byUser, searchUser.value, ['firstName', 'lastName', 'itemName', 'supplierName'])));
  /* ══════════════════════════
     LOAD ALL
  ══════════════════════════ */
  async function loadAll() {
    if (!state.sprintId) return;
    showLoader(true);
    try {
      const [byItem, bySupplier, byUser] = await Promise.all([
        get(`/admin/dashboard/sprint/${state.sprintId}/by-item`),
        get(`/admin/dashboard/sprint/${state.sprintId}/by-supplier`),
        get(`/admin/dashboard/sprint/${state.sprintId}/by-user`),
      ]);
      state.byItem     = byItem     || [];
      state.bySupplier = bySupplier || [];
      state.byUser     = byUser     || [];

      updateStats();
      renderByItem(state.byItem);
      renderBySupplier(state.bySupplier);
      renderByUser(state.byUser);
    } catch (e) {
      console.error(e);
      showToast('Error', 'Failed to load dashboard data', 'error');
    } finally {
      showLoader(false);
    }
  }

  /* ══════════════════════════
     STATS
  ══════════════════════════ */
  function updateStats() {
    const totalSold   = state.byItem.reduce((s, r) => s + (r.totalSold   || 0), 0);
    const totalPoints = state.byItem.reduce((s, r) => s + (r.totalPoints || 0), 0);
    const pending     = state.byUser.filter(r => r.status === 'PENDING').length;

    pop(statTotalItems,  totalSold);
    pop(statTotalPoints, totalPoints % 1 === 0 ? totalPoints : totalPoints.toFixed(1));
    pop(statSuppliers,   state.bySupplier.length);
    pop(statPending,     pending);
  }

  function pop(el, val) {
    el.style.opacity   = '0';
    el.style.transform = 'translateY(6px)';
    requestAnimationFrame(() => setTimeout(() => {
      el.textContent   = val;
      el.style.transition = 'opacity .35s ease, transform .35s ease';
      el.style.opacity   = '1';
      el.style.transform = 'translateY(0)';
    }, 60));
  }

  /* ══════════════════════════
     RENDER — BY ITEM
  ══════════════════════════ */
  function renderByItem(data) {
    if (!data.length) { bodyByItem.innerHTML = empty(5, 'No items found for this sprint'); return; }
    bodyByItem.innerHTML = data.map((r, i) => `
      <tr>
        <td>${i + 1}</td>
        <td>${esc(r.itemName)}</td>
        <td><span class="mdb-badge-supplier">${esc(r.supplierName)}</span></td>
        <td>${r.totalSold}</td>
        <td class="mdb-cell-points">${r.totalPoints}</td>
      </tr>`).join('');
  }

  /* ══════════════════════════
     RENDER — BY SUPPLIER
  ══════════════════════════ */
  function renderBySupplier(data) {
    if (!data.length) { bodyBySupplier.innerHTML = empty(5, 'No suppliers found for this sprint'); return; }
    bodyBySupplier.innerHTML = data.map((r, i) => `
      <tr>
        <td>${i + 1}</td>
        <td><span class="mdb-badge-supplier">${esc(r.supplierName)}</span></td>
        <td>${r.totalItems}</td>
        <td>${r.totalSold}</td>
        <td class="mdb-cell-points">${r.totalPoints}</td>
      </tr>`).join('');
  }

  /* ══════════════════════════
     RENDER — BY USER
  ══════════════════════════ */
  function renderByUser(data) {
    if (!data.length) { bodyByUser.innerHTML = empty(9, 'No orders found for this sprint'); return; }
    bodyByUser.innerHTML = data.map((r, i) => `
      <tr id="row-${r.cartId}">
        <td>${i + 1}</td>
        <td>${esc(r.firstName + ' ' + r.lastName)}</td>
        <td>${esc(r.itemName)}</td>
        <td><span class="mdb-badge-supplier">${esc(r.supplierName)}</span></td>
        <td>${r.itemCount}</td>
        <td class="mdb-cell-points">${r.points}</td>
        <td>${fmtDate(r.purchaseDate)}</td>
        <td id="status-${r.cartId}">${statusBadge(r.status)}</td>
        <td>
          <button class="mdb-btn-deliver"
                  id="btn-${r.cartId}"
                  onclick="window.mdbDeliver(${r.cartId})"
                  ${r.status === 'DELIVERED' ? 'disabled' : ''}>
            ${r.status === 'DELIVERED' ? '✓ Done' : '↑ Deliver'}
          </button>
        </td>
      </tr>`).join('');
  }

  /* ══════════════════════════
     STATUS CHANGE — PATCH
  ══════════════════════════ */
  window.mdbDeliver = async function (cartId) {
    const btn = document.getElementById(`btn-${cartId}`);
    if (!btn || btn.disabled) return;
    btn.disabled  = true;
    btn.innerHTML = '<span style="display:inline-block;width:10px;height:10px;border:2px solid rgba(5,150,105,.3);border-top-color:#059669;border-radius:50%;animation:mdbSpin .75s linear infinite"></span>';
    try {
      await put(`/api/v1/cart/${cartId}/status?status=DELIVERED`);
      const row = state.byUser.find(r => r.cartId === cartId);
      if (row) row.status = 'DELIVERED';
      document.getElementById(`status-${cartId}`).innerHTML = statusBadge('DELIVERED');
      btn.textContent = '✓ Done';
      btn.disabled = true;
      pop(statPending, state.byUser.filter(r => r.status === 'PENDING').length);
      showToast('Success', 'Order marked as delivered', 'success');
    } catch {
      btn.disabled  = false;
      btn.textContent = '↑ Deliver';
      showToast('Error', 'Failed to update status', 'error');
    }
  };

  /* ══════════════════════════
     HELPERS
  ══════════════════════════ */
  function statusBadge(status) {
    const map = { PENDING: 'mdb-status--pending', DELIVERED: 'mdb-status--delivered', CANCELLED: 'mdb-status--cancelled' };
    return `<span class="mdb-status ${map[status] || 'mdb-status--pending'}">${status}</span>`;
  }

  function filter(data, q, fields) {
    if (!q.trim()) return data;
    const lq = q.toLowerCase();
    return data.filter(r => fields.some(f => (r[f] || '').toLowerCase().includes(lq)));
  }

  function empty(cols, msg) {
    return `<tr class="mdb-empty-row"><td colspan="${cols}">${msg}</td></tr>`;
  }

  function esc(s) {
    return (s || '').replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/"/g,'&quot;');
  }

  function fmtDate(d) {
    if (!d) return '—';
    try { return new Date(d).toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' }); }
    catch { return d; }
  }

  function showLoader(v) { loader.classList.toggle('visible', v); }

  async function get(url) {
    const r = await fetch(url, { headers: { Accept: 'application/json' } });
    if (!r.ok) throw new Error(`HTTP ${r.status}`);
    return r.json();
  }

async function put(url) {
    const r = await fetch(url, { method: 'PUT', headers: { 'Content-Type': 'application/json' } });
    if (!r.ok) throw new Error(`HTTP ${r.status}`);
}

  /* Auto-load if sprint pre-selected */
  if (sprintSelect.value) { state.sprintId = sprintSelect.value; loadAll(); }

})();