/* ═══════════════════════════════════════════
   quiz.js — Quiz Module Logic
   WZNA Church App
   Depends on: Common.js (showToast), nav.js
═══════════════════════════════════════════ */

'use strict';

/* ════════════════════════════════
   1. SEARCH & FILTER
════════════════════════════════ */

const QuizSearch = (() => {
    let searchTimeout = null;

    function init() {
        const input    = document.getElementById('quizSearch');
        const status   = document.getElementById('filterStatus');
        const type     = document.getElementById('filterType');
        const active   = document.getElementById('filterActive');

        if (input)  input.addEventListener('input',  () => debounceFilter());
        if (status) status.addEventListener('change', () => applyFilter());
        if (type)   type.addEventListener('change',   () => applyFilter());
        if (active) active.addEventListener('change',  () => applyFilter());
    }

    function debounceFilter() {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(applyFilter, 280);
    }

    function applyFilter() {
        const term     = (document.getElementById('quizSearch')?.value   || '').toLowerCase().trim();
        const status   = (document.getElementById('filterStatus')?.value  || '').toLowerCase();
        const type     = (document.getElementById('filterType')?.value    || '').toLowerCase();
        const active   = (document.getElementById('filterActive')?.value  || '');

        const rows = document.querySelectorAll('.quiz-row');
        let visible = 0;

        rows.forEach(row => {
            const title   = (row.dataset.title   || '').toLowerCase();
            const creator = (row.dataset.creator || '').toLowerCase();
            const rowStat = (row.dataset.status  || '').toLowerCase();
            const rowType = (row.dataset.type    || '').toLowerCase();
            const rowAct  = (row.dataset.active  || '');

            const matchTerm   = !term   || title.includes(term) || creator.includes(term);
            const matchStatus = !status || rowStat  === status;
            const matchType   = !type   || rowType  === type;
            const matchActive = !active || rowAct   === active;

            const show = matchTerm && matchStatus && matchType && matchActive;
            row.style.display = show ? '' : 'none';
            if (show) visible++;
        });

        updateCount(visible);
        toggleEmpty(visible === 0);
    }

    function updateCount(n) {
        const el = document.getElementById('quizVisibleCount');
        if (el) el.textContent = n + ' quiz' + (n !== 1 ? 'zes' : '');
    }

    function toggleEmpty(isEmpty) {
        const empty = document.getElementById('quizEmptyState');
        const tbody = document.getElementById('quizTbody');
        if (empty) empty.style.display = isEmpty ? 'table-row' : 'none';
        if (tbody) tbody.style.display = isEmpty ? 'none' : '';
    }

    return { init };
})();


/* ════════════════════════════════
   2. DELETE — reuses youth-modal pattern
════════════════════════════════ */

const QuizDelete = (() => {
    let pendingId   = null;
    let pendingRow  = null;
    let pendingTitle = '';

    function init() {
        // Delegate: catch all delete buttons
        document.addEventListener('click', e => {
            const btn = e.target.closest('[data-quiz-delete]');
            if (!btn) return;
            e.preventDefault();
            pendingId    = btn.dataset.quizDelete;
            pendingRow   = btn.closest('tr');
            pendingTitle = btn.dataset.quizTitle || 'this quiz';
            openModal(pendingTitle);
        });

        // Confirm button
        const confirmBtn = document.getElementById('quizDeleteConfirm');
        if (confirmBtn) confirmBtn.addEventListener('click', executeDelete);

        // Cancel / close
        document.querySelectorAll('[data-quiz-modal-close]').forEach(el =>
            el.addEventListener('click', closeModal)
        );
    }

    function openModal(title) {
        const modal    = document.getElementById('quizDeleteModal');
        const nameSpan = document.getElementById('quizDeleteName');
        if (!modal) return;
        if (nameSpan) nameSpan.textContent = title;
        modal.style.display = 'flex';
        // Reset confirm button
        const confirmBtn = document.getElementById('quizDeleteConfirm');
        if (confirmBtn) confirmBtn.disabled = false;
    }

    function closeModal() {
        const modal = document.getElementById('quizDeleteModal');
        if (modal) modal.style.display = 'none';
        pendingId    = null;
        pendingRow   = null;
        pendingTitle = '';
    }

    function executeDelete() {
        if (!pendingId) return;

        const confirmBtn = document.getElementById('quizDeleteConfirm');
        if (confirmBtn) confirmBtn.disabled = true;

        fetch(`/dashboard/quiz/${pendingId}/delete`, {
            method: 'POST',
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
                [_csrfHeader()]: _csrfToken()
            }
        })
        .then(res => {
            if (!res.ok) throw new Error('Server error');
            // Remove row from table
            if (pendingRow) {
                pendingRow.style.transition = 'opacity .3s, transform .3s';
                pendingRow.style.opacity  = '0';
                pendingRow.style.transform = 'translateX(30px)';
                setTimeout(() => {
                    pendingRow.remove();
                    updateStatCount('totalQuizzes', -1);
                }, 320);
            }
            closeModal();
            showToast('Deleted', `"${pendingTitle}" was removed.`, 'success');
        })
        .catch(() => {
            closeModal();
            showToast('Error', 'Could not delete quiz. Please try again.', 'error');
        });
    }

    /* Read Spring's CSRF meta tags */
    function _csrfToken()  { return document.querySelector('meta[name="_csrf"]')?.content || ''; }
    function _csrfHeader() { return document.querySelector('meta[name="_csrf_header"]')?.content || 'X-CSRF-TOKEN'; }

    return { init };
})();


/* ════════════════════════════════
   3. TOGGLE STATUS (active/draft)
════════════════════════════════ */

const QuizToggle = (() => {
    function init() {
        document.addEventListener('click', e => {
            const btn = e.target.closest('[data-quiz-toggle]');
            if (!btn) return;
            e.preventDefault();
            const id  = btn.dataset.quizToggle;
            const row = btn.closest('tr');
            doToggle(id, btn, row);
        });
    }

    function doToggle(id, btn, row) {
        btn.disabled = true;

        fetch(`/dashboard/quiz/${id}/toggle`, {
            method: 'POST',
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
                [_csrf_header()]: _csrf()
            }
        })
        .then(r => r.json())
        .then(data => {
            // data = { status: 'ACTIVE' | 'DRAFT' }
            const badge = row?.querySelector('.quiz-badge');
            if (badge && data.status) {
                badge.className = 'quiz-badge quiz-badge-' + data.status.toLowerCase();
                badge.querySelector('.quiz-badge-dot') && (badge.querySelector('.quiz-badge-dot').className = 'quiz-badge-dot');
                badge.childNodes[badge.childNodes.length - 1].textContent = ' ' + capitalise(data.status);
            }
            row && (row.dataset.status = data.status.toLowerCase());
            showToast('Updated', `Quiz status changed to ${capitalise(data.status)}.`, 'success');
        })
        .catch(() => showToast('Error', 'Could not update status.', 'error'))
        .finally(() => { btn.disabled = false; });
    }

    function capitalise(s) { return s.charAt(0) + s.slice(1).toLowerCase(); }
    function _csrf()        { return document.querySelector('meta[name="_csrf"]')?.content || ''; }
    function _csrf_header() { return document.querySelector('meta[name="_csrf_header"]')?.content || 'X-CSRF-TOKEN'; }

    return { init };
})();


/* ════════════════════════════════
   4. STAT COUNTER HELPER
════════════════════════════════ */

function updateStatCount(statId, delta) {
    const el = document.getElementById(statId);
    if (!el) return;
    const current = parseInt(el.textContent, 10) || 0;
    el.textContent = Math.max(0, current + delta);
}


/* ════════════════════════════════
   5. CREATE / EDIT FORM VALIDATION
════════════════════════════════ */

const QuizForm = (() => {
    function init() {
        const form = document.getElementById('quizForm');
        if (!form) return;

        form.addEventListener('submit', e => {
            if (!validate(form)) {
                e.preventDefault();
            }
        });

        // Live validation on blur
        form.querySelectorAll('.form-input[required]').forEach(input => {
            input.addEventListener('blur', () => validateField(input));
            input.addEventListener('input', () => {
                if (input.classList.contains('invalid')) validateField(input);
            });
        });
    }

    function validate(form) {
        let valid = true;
        form.querySelectorAll('.form-input[required]').forEach(input => {
            if (!validateField(input)) valid = false;
        });
        return valid;
    }

    function validateField(input) {
        const ok = input.value.trim() !== '';
        input.classList.toggle('invalid', !ok);
        return ok;
    }

    return { init };
})();


/* ════════════════════════════════
   6. PAGE-SIZE SELECTOR
════════════════════════════════ */

function quizChangePageSize(select) {
    const url = new URL(window.location.href);
    url.searchParams.set('pageSize', select.value);
    url.searchParams.set('pageNum', 0);
    window.location.href = url.toString();
}


/* ════════════════════════════════
   7. INIT — called on DOMContentLoaded
════════════════════════════════ */

document.addEventListener('DOMContentLoaded', () => {
    QuizSearch.init();
    QuizDelete.init();
    QuizToggle.init();
    QuizForm.init();
});