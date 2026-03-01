// ═══════════════════════════════════════
// table.js — Global Reusable Table Logic
// Works for ALL tables in the app.
// ═══════════════════════════════════════


// ── 1. Truncate Toggle ──────────────────
// Usage: <button class="truncate-toggle" onclick="toggleTruncate(this)">more</button>
function toggleTruncate(btn) {
    const cell = btn.previousElementSibling;
    if (cell.classList.contains('expanded')) {
        cell.classList.remove('expanded');
        btn.textContent = 'more';
    } else {
        cell.classList.add('expanded');
        btn.textContent = 'less';
    }
}


// ── 2. Client-side Search/Filter ────────
// Usage: call filterTable('tableId', 'inputId', [columnIndexes to search])
// Example: filterTable('myTable', 'searchInput', [1, 2])
function filterTable(tableId, inputId, columnIndexes) {
    const input   = document.getElementById(inputId);
    const filter  = input.value.toLowerCase();
    const rows    = document.querySelectorAll(`#${tableId} tbody tr`);

    rows.forEach(row => {
        const cells = row.querySelectorAll('td');
        const match = columnIndexes.some(i => {
            const cell = cells[i];
            return cell && cell.textContent.toLowerCase().includes(filter);
        });
        row.style.display = match ? '' : 'none';
    });
}


// ── 3. Sort Table by Column ─────────────
// Usage: <th onclick="sortTable('tableId', 0)">Name</th>
const sortDirections = {};
function sortTable(tableId, colIndex) {
    const table   = document.getElementById(tableId);
    const tbody   = table.querySelector('tbody');
    const rows    = Array.from(tbody.querySelectorAll('tr'));
    const dir     = sortDirections[tableId + colIndex] === 'asc' ? 'desc' : 'asc';
    sortDirections[tableId + colIndex] = dir;

    rows.sort((a, b) => {
        const aText = a.cells[colIndex]?.textContent.trim().toLowerCase() || '';
        const bText = b.cells[colIndex]?.textContent.trim().toLowerCase() || '';
        const aNum  = parseFloat(aText);
        const bNum  = parseFloat(bText);

        if (!isNaN(aNum) && !isNaN(bNum)) {
            return dir === 'asc' ? aNum - bNum : bNum - aNum;
        }
        return dir === 'asc'
            ? aText.localeCompare(bText)
            : bText.localeCompare(aText);
    });

    rows.forEach(row => tbody.appendChild(row));

    // update sort icons in headers
    table.querySelectorAll('thead th').forEach((th, i) => {
        th.classList.remove('sort-asc', 'sort-desc');
        if (i === colIndex) th.classList.add(`sort-${dir}`);
    });
}


// ── 4. Highlight Search Term ─────────────
// Usage: highlightText('tableId', 'searchTerm')
function highlightText(tableId, term) {
    const tbody = document.querySelector(`#${tableId} tbody`);
    if (!tbody) return;

    // remove old highlights first
    tbody.querySelectorAll('mark').forEach(m => {
        m.replaceWith(m.textContent);
    });

    if (!term) return;

    const walk = node => {
        if (node.nodeType === 3) { // text node
            const idx = node.textContent.toLowerCase().indexOf(term.toLowerCase());
            if (idx >= 0) {
                const mark   = document.createElement('mark');
                mark.style.background = '#E5DEFF';
                mark.style.color      = '#6E59A5';
                mark.style.borderRadius = '3px';
                mark.style.padding    = '0 2px';
                mark.textContent = node.textContent.substring(idx, idx + term.length);

                const after  = node.splitText(idx);
                after.textContent = after.textContent.substring(term.length);
                node.parentNode.insertBefore(mark, after);
            }
        } else if (node.nodeType === 1 && node.nodeName !== 'MARK') {
            Array.from(node.childNodes).forEach(walk);
        }
    };

    tbody.querySelectorAll('td').forEach(walk);
}


// ── 5. Select All Checkboxes ─────────────
// Usage: <input type="checkbox" onclick="selectAll(this, 'myTable')">
function selectAll(masterCheckbox, tableId) {
    const table      = document.getElementById(tableId);
    const checkboxes = table.querySelectorAll('tbody input[type="checkbox"]');
    checkboxes.forEach(cb => cb.checked = masterCheckbox.checked);
}


// ── 6. Confirm Delete ────────────────────
// Usage: <button onclick="confirmDelete('/api/item/5', 'Item Name')">Delete</button>
function confirmDelete(url, itemName) {
    if (!confirm(`Are you sure you want to delete "${itemName}"?`)) return;

    fetch(url, { method: 'DELETE' })
        .then(res => {
            if (res.ok) location.reload();
            else res.text().then(msg => alert('Error: ' + msg));
        })
        .catch(err => {
            console.error('Delete error:', err);
            alert('Something went wrong, please try again.');
        });
}


// ── 7. Auto-init on DOMContentLoaded ─────
document.addEventListener('DOMContentLoaded', () => {

    // Hide "more" button if text fits without truncation
    document.querySelectorAll('.truncate-toggle').forEach(btn => {
        const cell = btn.previousElementSibling;
        if (cell && cell.scrollHeight <= cell.clientHeight + 4) {
            btn.style.display = 'none';
        }
    });

    // Fix toggle-switch: Thymeleaf th:field inserts a hidden input BEFORE
    // the checkbox, which breaks CSS `input[type="checkbox"]:checked + .toggle-slider`
    // Solution: move hidden input outside the label so the CSS sibling selector works.
    document.querySelectorAll('.toggle-switch').forEach(label => {
        const hidden   = label.querySelector('input[type="hidden"]');
        const checkbox = label.querySelector('input[type="checkbox"]');
        const slider   = label.querySelector('.toggle-slider');

        if (!checkbox || !slider) return;

        // Move hidden input before the label — outside toggle-switch
        if (hidden) label.parentNode.insertBefore(hidden, label);

        // Ensure DOM order is: checkbox → slider (CSS depends on this)
        label.appendChild(checkbox);
        label.appendChild(slider);
    });
});