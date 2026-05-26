/**
 * attendanceOverride.js
 *
 * Handles the admin attendance override page.
 * Submits records instantly, logs successes to the table, and preserves meeting data for re-use.
 */

// ── State ────────────────────────────────────────────────
let selectedUser   = null;   
let rowCounter     = 1;      // Tracks row numbers for the log table

const baseURL = getBaseUrl();  // from Common.js

// ── Element refs ─────────────────────────────────────────
const searchInput   = document.getElementById("searchInput");
const suggestList   = document.getElementById("suggestUsers");
const userDisplay   = document.getElementById("userDisplay");
const submitBtn     = document.getElementById("submitBtn");
const recordsTbody  = document.getElementById("recordsTbody");
const tableEmpty    = document.getElementById("tableEmpty");
const recordCount   = document.getElementById("recordCount");

// ── Close suggestions on outside click ───────────────────
document.addEventListener("click", (e) => {
  if (!suggestList.contains(e.target) && e.target !== searchInput) {
    hideSuggestions();
  }
});

// ── Search with debounce ──────────────────────────────────
function debounce(fn, wait) {
  let t;
  return (...args) => { clearTimeout(t); t = setTimeout(() => fn(...args), wait); };
}

const debouncedSearch = debounce(async (e) => {
  const query = e.target.value.trim();
  if (!query) { hideSuggestions(); return; }

  try {
    const res  = await fetch(`${baseURL}/api/profile/search?phone=${encodeURIComponent(query)}`);
    if (!res.ok) throw new Error("Search failed");
    const data = await res.json();
    renderSuggestions(data);
  } catch (err) {
    console.error("User search error:", err);
    renderSuggestions([]);
  }
}, 220);

searchInput.addEventListener("keyup", debouncedSearch);

// ── Render suggestion dropdown ────────────────────────────
function renderSuggestions(users) {
  suggestList.innerHTML = "";
  if (!users || users.length === 0) { hideSuggestions(); return; }
  suggestList.style.display = "block";

  users.forEach(user => {
    const li = document.createElement("li");
    li.innerHTML = `
      <span>${user.firstName} ${user.lastName}</span>
      <span class="suggest-phone">${user.phone}</span>
    `;
    li.addEventListener("click", () => selectUser(user));
    suggestList.appendChild(li);
  });
}

function hideSuggestions() {
  suggestList.innerHTML     = "";
  suggestList.style.display = "none";
}

// ── Select a user ─────────────────────────────────────────
function selectUser(user) {
  selectedUser       = user;
  searchInput.value  = user.phone;
  hideSuggestions();
  renderUserCard(user);
  checkFormReady();
}

function renderUserCard(user) {
  const initials = `${user.firstName[0]}${user.lastName[0]}`.toUpperCase();
  const avatarHtml = user.imagePath
    ? `<div class="u-avatar"><img src="${baseURL}/images/${user.imagePath}" alt="${initials}"/></div>`
    : `<div class="u-avatar">${initials}</div>`;

  userDisplay.className = "user-card filled";
  userDisplay.innerHTML = `
    <div class="user-filled">
      ${avatarHtml}
      <div>
        <div class="u-name">${user.firstName} ${user.lastName}</div>
        <div class="u-meta">
          <span>ID: ${user.id}</span>
          <span>${user.phone}</span>
          <input type="hidden" id="hiddenUserId" value="${user.userId}"/>
        </div>
      </div>
      <button class="u-clear" onclick="clearUser()" title="Remove">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/>
        </svg>
      </button>
    </div>
  `;
}

// ── Clear selected user ───────────────────────────────────
function clearUser() {
  selectedUser       = null;
  searchInput.value  = "";
  userDisplay.className = "user-card empty";
  userDisplay.innerHTML = `
    <div class="user-empty">
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/>
        <circle cx="9" cy="7" r="4"/>
        <path d="M22 21v-2a4 4 0 0 0-3-3.87"/>
        <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
      </svg>
      <p>No user selected</p>
    </div>
  `;
  checkFormReady();
}

// ── Enable submit only when all required fields are filled ─
function checkFormReady() {
  const meeting = document.getElementById("meetingSelect").value;
  const date    = document.getElementById("meetingDate").value;
  const time    = document.getElementById("arrivalTime").value;
  submitBtn.disabled = !(meeting && date && time && selectedUser);
}

["meetingSelect", "meetingDate", "arrivalTime"].forEach(id => {
  document.getElementById(id).addEventListener("change", checkFormReady);
});

function onMeetingChange() {
  checkFormReady();
}

// ── Direct Submission to Server + Log to Table ──────────────────
async function submitOverride() {
  if (submitBtn.disabled) return;

  const meetingCode = document.getElementById("meetingSelect").value;
  const meetingName = document.getElementById("meetingSelect").selectedOptions[0]?.text || "";
  const meetingDate = document.getElementById("meetingDate").value;
  const arrivalTime = document.getElementById("arrivalTime").value;
  const userId      = document.getElementById("hiddenUserId")?.value;
  const reason      = document.getElementById("overrideReason").value.trim();

  if (!meetingDate || !arrivalTime) {
    Toast.error("بيانات ناقصة", "Please select a date and arrival time.");
    return;
  }

  // Preserve UI element state
  const origHtml = submitBtn.innerHTML;
  submitBtn.innerHTML = `<div class="spinner"></div> Sending…`;
  submitBtn.disabled = true;

  try {
    // 1. Prepare Request Parameters
    const params = new URLSearchParams({
      theTakenDate: meetingDate,
      time: arrivalTime,
    }).toString();

    const targetURL = `${baseURL}/api/scanner/${meetingCode}/${userId}?${params}`;

    // 2. Fire Request to Spring Controller
    const res = await fetch(targetURL, {
      method: "GET",
      headers: { "Content-Type": "application/json" }
    });

    if (!res.ok) {
      let errorMessage = "Server error";
      try {
        const err = await res.json();
        errorMessage = err.message || errorMessage;
      } catch (_) {}
      throw new Error(errorMessage);
    }

    const data = await res.json();

    // 3. SUCCESS: Append record to history table log
    appendHistoryRow({
      firstName: selectedUser.firstName,
      lastName: selectedUser.lastName,
      meetingCode,
      meetingDate,
      arrivalTime,
      reason
    });

    Toast.success(
      "Success!",
      `Attendance saved for ${selectedUser.firstName} at ${meetingName}.`
    );

    // 4. PARTIAL RESET: Clear only the user card and reason text field
    clearUser();
    document.getElementById("overrideReason").value = "";
    
    // Refresh form ready status so button disables until next user is picked
    checkFormReady();

  } catch (err) {
    console.error("Submission failed:", err);
    Toast.error("Override Failed", err.message || "Something went wrong on the backend.");
  } finally {
    submitBtn.innerHTML = origHtml;
    submitBtn.disabled = false;
  }
}

// ── Append a successful record to the history table ─────────
function appendHistoryRow(item) {
  tableEmpty.style.display = "none";

  const tr = document.createElement("tr");
  const shortDate = item.meetingDate
    ? new Date(item.meetingDate).toLocaleDateString("en-GB", { day: "numeric", month: "short" })
    : "—";

  tr.innerHTML = `
    <td>${rowCounter++}</td>
    <td>${item.firstName} ${item.lastName}</td>
    <td><span class="mtg-badge">${item.meetingCode}</span></td>
    <td>${shortDate}</td>
    <td class="time-val">${item.arrivalTime}</td>
    <td class="note-cell" title="${item.reason || ''}">${item.reason || "—"}</td>
    <td>
      <span class="badge bg-success" style="font-size: 0.75rem; padding: 0.25rem 0.5rem; border-radius: 4px; color: white; background-color: #28a745;">Saved</span>
    </td>
  `;

  recordsTbody.appendChild(tr);
  
  const totalRows = recordsTbody.querySelectorAll("tr").length;
  recordCount.textContent = `${totalRows} record${totalRows !== 1 ? "s" : ""} saved`;
}

// ── Full Reset: Only triggered when clicking the explicit Reset Button ──
function resetForm() {
  document.getElementById("meetingSelect").value   = "";
  document.getElementById("meetingDate").value     = "";
  document.getElementById("arrivalTime").value     = "";
  document.getElementById("overrideReason").value  = "";
  clearUser();
  checkFormReady();
}