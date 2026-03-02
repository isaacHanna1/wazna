const searchInput    = document.getElementById("search-input");
const userList       = document.getElementById("suggestUsers");
const btn_attendance = document.getElementById("attendance-btn");
const baseURL        = getBaseUrl();

// ── FIX: exclude meetingCode select so native dropdown opens correctly ──
document.addEventListener("click", function (event) {
  const meetingCode = document.getElementById("meetingCode");
  if (
    !userList.contains(event.target) &&
    !searchInput.contains(event.target) &&
    event.target !== meetingCode        // ← was missing, caused select to not open
  ) {
    userList.innerHTML     = "";
    userList.style.display = "none";
  }
});


async function generateUser(userPhone) {
  try {
    const response = await fetch(baseURL + `/api/profile/search?phone=${userPhone}`);
    if (!response.ok) throw new Error("Network response was not ok: " + response.status);
    const data = await response.json();
    renderUserList(data);
    return data;
  } catch (error) {
    console.error(error);
    renderUserList(null, "Error loading users");
  }
}

function debounce(fn, wait) {
  let timeOut;
  return function (...args) {
    clearTimeout(timeOut);
    timeOut = setTimeout(() => fn(...args), wait);
  };
}

const debouncedSearch = debounce(function (event) {
  const phone = event.target.value;
  btn_attendance.disabled = true;
  btn_attendance.classList.add("disabled");
  generateUser(phone);
}, 200);

searchInput.addEventListener("keyup", debouncedSearch);

function renderUserList(users, errorMessage) {
  userList.innerHTML = "";

  if (errorMessage) {
    userList.innerHTML     = `<li>${errorMessage}</li>`;
    userList.style.display = "block";
    return;
  }
  if (!users || users.length === 0) {
    userList.style.display = "none";
    return;
  }
  userList.style.display = "block";
  users.forEach(user => {
    const li       = document.createElement("li");
    li.textContent = `${user.firstName} ${user.lastName}`;
    userList.appendChild(li);
    li.addEventListener("click", async () => {
      displayUser(user);
      userList.style.display  = "none";
      btn_attendance.disabled = false;
      btn_attendance.classList.remove("disabled");
    });
  });
}

function displayUser(user) {
  const userDisplay     = document.getElementById("user-display");
  searchInput.value     = `${user.phone}`;
  userDisplay.className = "user-display filled";
  userDisplay.innerHTML = `
    <div class="user_info">
      <img src="${baseURL}/images/${user.imagePath}" class="user-avatar" />
      <div class="user-details">
        <h3 class="user-name">${user.firstName} ${user.lastName}</h3>
        <div class="user-meta">
          <span>ID: ${user.id}</span>
          <span>Phone: ${user.phone}</span>
          <input type="hidden" id="user_Id" value="${user.userId}" />
        </div>
      </div>
    </div>
  `;
}

async function markPersent() {
  try {
    btn_attendance.disabled  = true;
    const btnInnerHtml       = btn_attendance.innerHTML;
    btn_attendance.innerHTML = `<div class="loading-spinner"></div>Recording Attendance...`;

    let meetingCode = document.getElementById("meetingCode").value;
    if (meetingCode === "") {
        
    Toast.error('عملية خاطئة', 'كود الاجتماع فارغ! ');
      btn_attendance.innerHTML = btnInnerHtml;
      btn_attendance.disabled  = false;
      return;
    }

    let userId     = document.getElementById("user_Id").value;
    const response = await fetch(baseURL + `/api/scanner/${meetingCode}/${userId}`);
    if (!response.ok) {
      const errorData          = await response.json();
      btn_attendance.innerHTML = btnInnerHtml;
      throw new Error("Error : " + errorData.message);
    }

    const user               = await response.json();
    updateAttendanceTable(user);
    btn_attendance.innerHTML = btnInnerHtml;
    Toast.success("Attendance recorded successfully!",
        ` ${user.firstName} has been marked present and earned ${user.points} Wazna points.`);
     } catch (error) {
        Toast.error('عملية خاطئة', error.message);
        btn_attendance.disabled = false;
        return;
    }
  btn_attendance.disabled = false;
}

function updateAttendanceTable(user) {
  const tbody       = document.getElementById("attendance-tbody");
  const currentTime = new Date().toLocaleTimeString([], { hour: "2-digit", minute: "2-digit", second: "2-digit" });

  const row       = document.createElement("tr");
  let rowNumInput = document.getElementById("rowNum");
  let rowNum      = parseInt(rowNumInput?.value) || 1;
  row.className   = "table-row";
  row.innerHTML   = `
    <td class="table-cell hidden-mobile">${rowNum}</td>
    <td class="table-cell hidden-mobile">${user.userId}</td>
    <td class="table-cell">${user.firstName} ${user.lastName}</td>
    <td class="table-cell">${user.points}</td>
    <td class="table-cell">${user.balance}</td>
    <td class="table-cell hidden-mobile">${currentTime}</td>
  `;
  tbody.appendChild(row);
  rowNumInput.value = rowNum + 1;
}

function displayEmptyUser() {
  const userDisplay     = document.getElementById("user-display");
  userDisplay.className = "user-display empty";
  userDisplay.innerHTML = `
    <div class="empty-state">
      <svg class="empty-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/>
        <circle cx="9" cy="7" r="4"/>
        <path d="M22 21v-2a4 4 0 0 0-3-3.87"/>
        <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
      </svg>
      <p>Search for a user to display their information</p>
    </div>
  `;
}

function cancelAttendance() {
  userList.innerHTML      = "";
  userList.style.display  = "none";
  searchInput.value       = "";
  displayEmptyUser();
  btn_attendance.disabled = true;
  btn_attendance.classList.add("disabled");
}