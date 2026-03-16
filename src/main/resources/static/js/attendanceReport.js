/* ═══════════════════════════════════════════════════════════════
   attendanceReport.js
   WZNA Church App
   Cascade: Date Range → Bonus Type → QR Code → Generate Report
═══════════════════════════════════════════════════════════════ */

/* ════════════════════════════════════════════════════
   1. API ENDPOINTS
════════════════════════════════════════════════════ */
const RPT_API = {
  bonusTypes : ()                  => `/api/bonus-types`,
  qrCodes    : (bonusId, from, to) => `/api/qr-codes?bonus_type_id=${bonusId}&date_from=${from}&date_to=${to}`,
  report         : (qrId, svcClass, role) =>
    `/api/attendance/status?qr_code_id=${qrId}`  +
    `${svcClass ? '&service_class=' + encodeURIComponent(svcClass) : ''}` +
    `${role     ? '&role='          + encodeURIComponent(role)      : ''}`,
  serviceClasses : () => `/api/service-classes`,
};

/* ════════════════════════════════════════════════════
   2. STATE
════════════════════════════════════════════════════ */
const RPT = {
  dateFrom     : null,
  dateTo       : null,
  bonus        : null,
  qr           : null,
  allData      : [],
  filteredData : [],
};

/* ════════════════════════════════════════════════════
   3. DOM REFS
════════════════════════════════════════════════════ */
let $dateFrom, $dateTo,
    $dateError, $dateErrorText,
    $bonusSel, $bonusSpinner,
    $qrSel, $qrSpinner,
    $serviceClassFilter, $roleFilter,
    $qrInfo, $qrInfoText,
    $generateBtn, $exportBtn, $printBtn,
    $emptyState, $loadingState, $reportContent, $errorState, $errorText,
    $statsGrid, $tbody, $reportTitle,
    $searchInput, $countLabel;

document.addEventListener('DOMContentLoaded', () => {
  $dateFrom           = document.getElementById('rptDateFrom');
  $dateTo             = document.getElementById('rptDateTo');
  $dateError          = document.getElementById('rptDateError');
  $dateErrorText      = document.getElementById('rptDateErrorText');
  $bonusSel           = document.getElementById('rptBonusTypeSelect');
  $bonusSpinner       = document.getElementById('rptBonusSpinner');
  $qrSel              = document.getElementById('rptQrSelect');
  $qrSpinner          = document.getElementById('rptQrSpinner');
  $serviceClassFilter = document.getElementById('rptServiceClassFilter');
  $roleFilter         = document.getElementById('rptRoleFilter');
  $qrInfo             = document.getElementById('rptQrInfo');
  $qrInfoText         = document.getElementById('rptQrInfoText');
  $generateBtn        = document.getElementById('rptGenerateBtn');
  $exportBtn          = document.getElementById('rptExportBtn');
  $printBtn           = document.getElementById('rptPrintBtn');
  $emptyState         = document.getElementById('rptEmptyState');
  $loadingState       = document.getElementById('rptLoadingState');
  $reportContent      = document.getElementById('rptContent');
  $errorState         = document.getElementById('rptErrorState');
  $errorText          = document.getElementById('rptErrorText');
  $statsGrid          = document.getElementById('rptStatsGrid');
  $tbody              = document.getElementById('rptTbody');
  $reportTitle        = document.getElementById('rptReportTitle');
  $searchInput        = document.getElementById('rptSearchInput');
  $countLabel         = document.getElementById('rptCountLabel');

  /* ── Date inputs trigger cascade automatically on change ── */
  $dateFrom.addEventListener('change', onDateRangeChange);
  $dateTo.addEventListener('change',   onDateRangeChange);

  $bonusSel.addEventListener('change',           onBonusChange);
  $qrSel.addEventListener('change',              onQrChange);
  $serviceClassFilter.addEventListener('change', onServiceClassFilter);
  $roleFilter.addEventListener('change',         onServiceClassFilter);  /* re-use same handler — triggers applyFilters */
  $generateBtn.addEventListener('click',         generateReport);
  $exportBtn.addEventListener('click',           exportCSV);
  $printBtn.addEventListener('click',            () => window.print());
  $searchInput.addEventListener('input',         onSearch);

  /* load service classes independently on page start */
  loadServiceClasses();
});

/* ════════════════════════════════════════════════════
   4. STEP 1 — Date range changes → load bonus types
   Both $dateFrom and $dateTo must be filled to proceed.
════════════════════════════════════════════════════ */
async function onDateRangeChange() {
  hideDateError();
  resetFrom('bonus');

  const from = $dateFrom.value;
  const to   = $dateTo.value;

  /* wait until both are filled */
  if (!from || !to) return;

  if (from > to) {
    showDateError('"From" date must be before or equal to "To" date.');
    return;
  }

  RPT.dateFrom = from;
  RPT.dateTo   = to;
  stepState(1, 'done');
  stepState(2, 'active');

  spinner($bonusSpinner, true);
  $bonusSel.disabled = true;
  $bonusSel.innerHTML = '<option value="">Loading bonus types…</option>';

  try {
    const data = await apiFetch(RPT_API.bonusTypes());
    $bonusSel.innerHTML = '<option value="">— Select bonus type —</option>';
    if (!data.length) {
      $bonusSel.innerHTML = '<option value="">No bonus types found</option>';
    } else {
      data.forEach(b => addOpt($bonusSel, b.id, b.description || `Bonus #${b.id}`, b));
      $bonusSel.disabled = false;
    }
  } catch (e) {
    $bonusSel.innerHTML = '<option value="">— Error loading bonus types —</option>';
    showBodyError('Could not load bonus types: ' + e.message);
  } finally {
    spinner($bonusSpinner, false);
  }
}

/* ════════════════════════════════════════════════════
   5. STEP 2 — Bonus type chosen → load QR codes
════════════════════════════════════════════════════ */
async function onBonusChange() {
  resetFrom('qr');
  if (!$bonusSel.value) { stepState(2, 'active'); return; }

  RPT.bonus = getRaw($bonusSel);
  stepState(2, 'done');
  stepState(3, 'active');

  spinner($qrSpinner, true);
  $qrSel.disabled = true;
  $qrSel.innerHTML = '<option value="">Loading QR codes…</option>';

  try {
    const data = await apiFetch(RPT_API.qrCodes(RPT.bonus.id, RPT.dateFrom, RPT.dateTo));
    $qrSel.innerHTML = '<option value="">— Select a QR code —</option>';
    if (!data.length) {
      $qrSel.innerHTML = '<option value="">No QR codes found</option>';
    } else {
      data.forEach(q => addOpt($qrSel, q.id, buildQrLabel(q), q));
      $qrSel.disabled = false;
    }
  } catch (e) {
    $qrSel.innerHTML = '<option value="">— Error loading QR codes —</option>';
    showBodyError('Could not load QR codes: ' + e.message);
  } finally {
    spinner($qrSpinner, false);
  }
}

/* ════════════════════════════════════════════════════
   6. STEP 3 — QR code chosen → show info, enable Generate
════════════════════════════════════════════════════ */
function onQrChange() {
  RPT.qr = null;
  $qrInfo.style.display = 'none';
  $generateBtn.disabled = true;
  if (!$qrSel.value) return;

  RPT.qr = getRaw($qrSel);
  stepState(3, 'done');

  const q = RPT.qr;
  let html = `<strong>${esc(q.description || 'QR #'+q.id)}</strong>`;
  if (q.valid_date)  html += `&nbsp;·&nbsp; Date: <strong>${q.valid_date}</strong>`;
  if (q.valid_start) html += `&nbsp;·&nbsp; From: <strong>${q.valid_start}</strong>`;
  if (q.valid_end)   html += `&nbsp;·&nbsp; To: <strong>${q.valid_end}</strong>`;
  if (q.is_active !== undefined)
    html += q.is_active
      ? `&nbsp;<span style="color:var(--success-500);font-size:.8rem;"><i class="fas fa-circle" style="font-size:.55rem"></i> Active</span>`
      : `&nbsp;<span style="color:var(--error-500);font-size:.8rem;"><i class="fas fa-circle" style="font-size:.55rem"></i> Inactive</span>`;

  $qrInfoText.innerHTML = html;
  $qrInfo.style.display = 'flex';
  $generateBtn.disabled = false;
}

/* ════════════════════════════════════════════════════
   7. GENERATE REPORT
════════════════════════════════════════════════════ */
async function generateReport() {
  if (!RPT.qr) return;

  showBody('loading');
  stepState(4, 'active');
  $generateBtn.disabled = true;
  setExport(false);

  try {
    const serviceClass = $serviceClassFilter.value;  // '' = all classes
    const role         = $roleFilter.value;           // '' = all roles
    const data = await apiFetch(RPT_API.report(RPT.qr.id, serviceClass, role));
    RPT.allData      = data;
    RPT.filteredData = data;
    renderReport(data);
    stepState(4, 'done');
    setExport(true);
  } catch (e) {
    showBodyError('Failed to load report: ' + e.message);
    stepState(4, 'active');
  } finally {
    $generateBtn.disabled = false;
  }
}

/* ════════════════════════════════════════════════════
   8. LOAD SERVICE CLASSES FROM API  (called once on page load)
   Endpoint: GET /api/service-classes
   Accepts two shapes from the backend:
     • Array of strings → ["Servant", "Deacon", "Reader", …]
     • Array of objects → [{ id, name }, …]  (uses .name as label+value)
   Always prepends "All" as the first option.
════════════════════════════════════════════════════ */
async function loadServiceClasses() {
  $serviceClassFilter.disabled = true;
  $serviceClassFilter.innerHTML = '<option value="">Loading…</option>';

  try {
    const data = await apiFetch(RPT_API.serviceClasses());

    $serviceClassFilter.innerHTML = '<option value="">الكل</option>';

    if (!data || !data.length) {
      $serviceClassFilter.innerHTML = '<option value="">— No classes found —</option>';
      return;
    }

    data.forEach(item => {
      /* support both string[] and {id,name}[] shapes */
      const label = typeof item === 'string' ? item : (item.name || item.description || String(item.value));
      const value = typeof item === 'string' ? item : (item.name || item.description || String(item.key));
      const opt   = document.createElement('option');
      opt.value       = value;
      opt.textContent = label;
      $serviceClassFilter.appendChild(opt);
    });

    $serviceClassFilter.disabled = false;
  } catch (e) {
    $serviceClassFilter.innerHTML = '<option value="">— Error loading —</option>';
    console.warn('[attendanceReport] Could not load service classes:', e.message);
  }
}

/* ════════════════════════════════════════════════════
   9. SERVICE CLASS FILTER CHANGE
   Combines with the text search to narrow results.
════════════════════════════════════════════════════ */
function onServiceClassFilter() {
  applyFilters();
}

/* ════════════════════════════════════════════════════
   10. CLIENT-SIDE SEARCH
════════════════════════════════════════════════════ */
function onSearch() {
  applyFilters();
}

/* ── Unified filter: text search + service class dropdown ── */
function applyFilters() {
  const q   = $searchInput.value.trim().toLowerCase();
  const cls = $serviceClassFilter.value;   // "" = all

  const role = $roleFilter.value;  // '' = all

  RPT.filteredData = RPT.allData.filter(r => {
    /* service class filter */
    if (cls && (r.service_class || '').trim() !== cls) return false;
    /* role filter — matches against r.role or r.roles array */
    if (role) {
      const userRole = (r.role || r.roles || '');
      const rolesArr = Array.isArray(userRole) ? userRole : [userRole];
      if (!rolesArr.some(rv => String(rv).toUpperCase().includes(role.toUpperCase()))) return false;
    }
    /* text search — also searches attendance_status */
    if (q) {
      const hay = `${r.first_name||''} ${r.last_name||''} ${r.phone||''} ${r.service_class||''} ${r.gendre||''} ${r.attendance_status||''}`.toLowerCase();
      if (!hay.includes(q)) return false;
    }
    return true;
  });

  renderRows(RPT.filteredData);
  updateCount(RPT.filteredData.length, RPT.allData.length);
}

/* ════════════════════════════════════════════════════
   11. RENDER REPORT
════════════════════════════════════════════════════ */
function renderReport(data) {
  const q = RPT.qr, b = RPT.bonus;

  $reportTitle.textContent =
    `${esc(b.description || 'Bonus #'+b.id)}  —  ${esc(q.description || 'QR #'+q.id)}` +
    (q.valid_date ? `  (${q.valid_date})` : '');

  const male    = data.filter(r => r.gendre === 'Male').length;
  const female  = data.filter(r => r.gendre === 'Female').length;
  const present = data.filter(r => (r.attendance_status || '').toLowerCase() === 'present').length;
  const absent  = data.filter(r => (r.attendance_status || '').toLowerCase() === 'absent').length;

  $statsGrid.innerHTML = `
    <div class="rpt-stat-card">
      <span class="rpt-stat-value">${data.length}</span>
      <div class="rpt-stat-label"><i class="fas fa-users"></i> Total</div>
    </div>
    <div class="rpt-stat-card" style="border-color:#bbf7d0;">
      <span class="rpt-stat-value" style="color:#15803d;">${present}</span>
      <div class="rpt-stat-label"><i class="fas fa-check-circle"></i> Present</div>
    </div>
    <div class="rpt-stat-card" style="border-color:#fecaca;">
      <span class="rpt-stat-value" style="color:#dc2626;">${absent}</span>
      <div class="rpt-stat-label"><i class="fas fa-times-circle"></i> Absent</div>
    </div>
    <div class="rpt-stat-card">
      <span class="rpt-stat-value">${male}</span>
      <div class="rpt-stat-label"><i class="fas fa-mars"></i> Male</div>
    </div>
    <div class="rpt-stat-card">
      <span class="rpt-stat-value">${female}</span>
      <div class="rpt-stat-label"><i class="fas fa-venus"></i> Female</div>
    </div>
    <div class="rpt-stat-card">
      <span class="rpt-stat-value" style="font-size:.95rem;padding-top:.25rem">${q.valid_date || '—'}</span>
      <div class="rpt-stat-label"><i class="fas fa-calendar"></i> Meeting Date</div>
    </div>
  `;

  $searchInput.value = '';
  renderRows(data);
  updateCount(data.length, data.length);
  showBody('content');
}

function renderRows(rows) {
  if (!rows.length) {
    $tbody.innerHTML = `
      <tr>
        <td colspan="7" style="padding:2rem;color:var(--gray-400);text-align:center;">
          <i class="fas fa-search" style="margin-right:.4rem;"></i>No records found.
        </td>
      </tr>`;
    return;
  }
  $tbody.innerHTML = rows.map((r, i) => {
    const initials    = `${(r.first_name||'?')[0]}${(r.last_name||'?')[0]}`.toUpperCase();
    const genderBadge = r.gendre === 'Male'
      ? `<span class="rpt-badge rpt-badge-male"><i class="fas fa-mars" style="font-size:.6rem"></i> Male</span>`
      : `<span class="rpt-badge rpt-badge-female"><i class="fas fa-venus" style="font-size:.6rem"></i> Female</span>`;
    const time = r.scanned_at
      ? `<span class="rpt-time-badge"><i class="fas fa-clock" style="font-size:.65rem"></i> ${esc(r.scanned_at)}</span>`
      : '—';
    /* service class badge */
    const svcClass = r.service_class
      ? `<span class="rpt-badge rpt-badge-svc">${esc(r.service_class)}</span>`
      : '—';
    /* attendance status badge */
    const status = (r.attendance_status || '').toLowerCase();
    const statusBadge = status === 'present'
      ? `<span class="rpt-badge rpt-badge-present"><i class="fas fa-check" style="font-size:.6rem"></i> Present</span>`
      : `<span class="rpt-badge rpt-badge-absent"><i class="fas fa-times" style="font-size:.6rem"></i> Absent</span>`;
    return `
      <tr>
        <td>${i + 1}</td>
        <td class="rpt-name-col">
          <div class="rpt-name-cell">
            <span class="rpt-avatar">${initials}</span>
            ${esc(r.first_name||'')} ${esc(r.last_name||'')}
          </div>
        </td>
        <td>${genderBadge}</td>
        <td>${esc(r.phone||'—')}</td>
        <td>${svcClass}</td>
        <td>${statusBadge}</td>
        <td>${time}</td>
      </tr>`;
  }).join('');
}

function updateCount(shown, total) {
  $countLabel.textContent = shown === total
    ? `${total} attendee${total !== 1 ? 's' : ''}`
    : `Showing ${shown} of ${total}`;
}

/* ════════════════════════════════════════════════════
   12. EXPORT CSV
════════════════════════════════════════════════════ */
function exportCSV() {
  if (!RPT.filteredData.length) return;
  const headers = ['#','First Name','Last Name','Gender','Phone','Service Class','Status','Scanned At'];
  const csv = [headers, ...RPT.filteredData.map((r, i) => [
    i+1, r.first_name||'', r.last_name||'', r.gendre||'',
    r.phone||'', r.service_class||'', r.attendance_status||'', r.scanned_at||''
  ])].map(row => row.map(c => `"${String(c).replace(/"/g,'""')}"`).join(',')).join('\n');

  const filename = `attendance_${(RPT.bonus.description||RPT.bonus.id)}_${RPT.qr.valid_date||RPT.qr.id}.csv`
    .replace(/\s+/g,'_');
  const blob = new Blob(['\ufeff'+csv], { type:'text/csv;charset=utf-8;' });
  const url  = URL.createObjectURL(blob);
  const a    = Object.assign(document.createElement('a'), { href:url, download:filename });
  document.body.appendChild(a); a.click();
  document.body.removeChild(a); URL.revokeObjectURL(url);
}

/* ════════════════════════════════════════════════════
   13. STEP INDICATOR
════════════════════════════════════════════════════ */
function stepState(n, state) {
  const el   = document.getElementById('rptStep' + n);
  const line = document.getElementById('rptLine' + n);
  if (!el) return;
  const circle = el.querySelector('.rpt-step-circle');
  if (state === 'done') {
    el.className     = 'rpt-step done';
    circle.innerHTML = '<i class="fas fa-check" style="font-size:.65rem"></i>';
    if (line) line.classList.add('done');
  } else if (state === 'active') {
    el.className       = 'rpt-step active';
    circle.textContent = n;
    if (line) line.classList.remove('done');
  } else {
    el.className       = 'rpt-step';
    circle.textContent = n;
    if (line) line.classList.remove('done');
  }
}

/* Reset cascade from a given stage downward */
function resetFrom(stage) {
  const order = ['bonus', 'qr', 'report'];
  const idx   = order.indexOf(stage);

  if (idx <= 0) {
    RPT.bonus = null;
    $bonusSel.innerHTML = '<option value="">— Select date range first —</option>';
    $bonusSel.disabled  = true;
    stepState(2, 'idle');
  }
  if (idx <= 1) {
    RPT.qr = null;
    $qrSel.innerHTML = '<option value="">— Select bonus type first —</option>';
    $qrSel.disabled  = true;
    $qrInfo.style.display = 'none';
    $generateBtn.disabled = true;
    stepState(3, 'idle');
  }
  if (idx <= 2) {
    RPT.allData = []; RPT.filteredData = [];
    /* reset filters back to ALL but keep options (loaded from API) */
    $serviceClassFilter.value = '';
    $roleFilter.value         = '';
    setExport(false);
    showBody('empty');
    stepState(4, 'idle');
  }
}

/* ════════════════════════════════════════════════════
   14. UI HELPERS
════════════════════════════════════════════════════ */
function showBody(state, msg) {
  $emptyState.style.display    = 'none';
  $loadingState.style.display  = 'none';
  $reportContent.style.display = 'none';
  $errorState.style.display    = 'none';
  if (state === 'empty')   $emptyState.style.display    = 'block';
  if (state === 'loading') $loadingState.style.display  = 'block';
  if (state === 'content') $reportContent.style.display = 'block';
  if (state === 'error')   { $errorState.style.display  = 'flex'; $errorText.textContent = msg || 'An error occurred.'; }
}
function showBodyError(msg)  { showBody('error', msg); }
function setExport(on)       { $exportBtn.disabled = $printBtn.disabled = !on; }
function spinner(el, on)     { el.classList.toggle('visible', on); }
function showDateError(msg)  { $dateErrorText.textContent = msg; $dateError.style.display = 'flex'; }
function hideDateError()     { $dateError.style.display = 'none'; }

/* ════════════════════════════════════════════════════
   15. UTILITIES
════════════════════════════════════════════════════ */
async function apiFetch(url) {
  const res = await fetch(url, { headers: { 'Accept': 'application/json' } });
  if (!res.ok) {
    let msg = `HTTP ${res.status}`;
    try { const j = await res.json(); msg = j.message || j.error || msg; } catch(_) {}
    throw new Error(msg);
  }
  return res.json();
}

function addOpt(sel, value, label, rawObj) {
  const opt = document.createElement('option');
  opt.value       = value;
  opt.textContent = label;
  opt.dataset.raw = JSON.stringify(rawObj);
  sel.appendChild(opt);
}

function getRaw(sel) {
  return JSON.parse(sel.options[sel.selectedIndex].dataset.raw || '{}');
}

function buildQrLabel(q) {
  let label = q.description || `QR #${q.id}`;
  if (q.valid_date)  label += `  ·  ${q.valid_date}`;
  if (q.valid_start && q.valid_end) label += `  (${q.valid_start}–${q.valid_end})`;
  return label;
}

function esc(str) {
  return String(str)
    .replace(/&/g,'&amp;').replace(/</g,'&lt;')
    .replace(/>/g,'&gt;').replace(/"/g,'&quot;');
}