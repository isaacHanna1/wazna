
//       // Mock data
//       let  users = [
//       ];

//       let todayAttendance = [];
//       let selectedUser = null;

//       // Initialize app
//       document.addEventListener("DOMContentLoaded", function () {
//        // updateStats();

//         // Add enter key listener to search input
//         document.getElementById("search-input").addEventListener("keyup", function (e) {
//             generateUser(document.getElementById("search-input").value);
//             generateAutoCompleteNumber(users);
//             if (e.key === "Enter") {
//               if (!selectedUser) {
//                 handleSearch();
//               } else {
//                 handleAddAttendance();
//               }
//             }
//           });
//       });
//       function updateStats() {
//         const totalPoints = users.reduce((sum, user) => sum + user.points, 0);
//         document.getElementById("total-points").textContent =
//           totalPoints.toLocaleString();
//       }

//       function showToast(title, message, type = "info") {
//         const toastContainer = document.getElementById("toast-container");
//         const toast = document.createElement("div");
//         toast.className = `toast toast-${type}`;

//         toast.innerHTML = `
//                 <div class="toast-content">
//                     <div class="toast-title">${title}</div>
//                     <div class="toast-message">${message}</div>
//                 </div>
//                 <button class="toast-close" onclick="this.parentElement.remove()">×</button>
//             `;

//         toastContainer.appendChild(toast);

//         // Auto remove after 5 seconds
//         setTimeout(() => {
//           if (toast.parentElement) {
//             toast.remove();
//           }
//         }, 5000);
//       }

//       function handleSearch() {

//         const searchValue = document.getElementById("search-input").value.trim();
//         if (!searchValue) return;
//         const foundUser = users.find(
//           (user) => user.id === searchValue || user.phone === searchValue,
//         );

//         if (foundUser) {
//           selectedUser = foundUser;
//           displayUser(foundUser);
//           updateAttendanceButton();
//         } else {
//           selectedUser = null;
//           displayEmptyUser();
//           updateAttendanceButton();
//           showToast(
//             "User not found",
//             "No user found with that ID or phone number.",
//             "error",
//           );
//         }
//       }

//       function displayUser(user) {
//         const userDisplay = document.getElementById("user-display");
//         const hasAttended = hasAttendedToday(user.id);

//         userDisplay.className = "user-display filled";
//         userDisplay.innerHTML = `
//                 <div class="user-info">
//                     <div class="user-avatar">${user.avatar}</div>
//                     <div class="user-details">
//                         <h3 class="user-name">${user.name}</h3>
//                         <div class="user-meta">
//                             <span>ID: ${user.id}</span>
//                             <span>Phone: ${user.phone}</span>
//                         </div>
//                     </div>
//                 </div>
//                 <div class="user-status">
//                     <div class="points-badge">${user.points} points</div>
//                     ${hasAttended ? '<div class="attended-badge">Already attended today</div>' : ""}
//                 </div>
//             `;
//       }

//       function displayEmptyUser() {
//         const userDisplay = document.getElementById("user-display");
//         userDisplay.className = "user-display empty";
//         userDisplay.innerHTML = `
//                 <div class="empty-state">
//                     <svg class="empty-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
//                         <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/>
//                         <circle cx="9" cy="7" r="4"/>
//                         <path d="M22 21v-2a4 4 0 0 0-3-3.87"/>
//                         <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
//                     </svg>
//                     <p>Search for a user to display their information</p>
//                 </div>
//             `;
//       }

//       function updateAttendanceButton() {
//         const btn = document.getElementById("attendance-btn");
//         const hasAttended = selectedUser && hasAttendedToday(selectedUser.id);

//         if (!selectedUser || hasAttended) {
//           btn.disabled = true;
//           btn.className = "attendance-btn disabled";
//           btn.innerHTML = hasAttended
//             ? `<svg class="btn-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>Already Attended Today`
//             : `<svg class="btn-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="m15.477 12.89 1.515 8.526a.5.5 0 0 1-.81.47l-3.58-2.687a1 1 0 0 0-1.197 0l-3.586 2.686a.5.5 0 0 1-.81-.469l1.514-8.526"/><circle cx="12" cy="8" r="6"/></svg>Mark Present (+25 points)`;
//         } else {
//           btn.disabled = false;
//           btn.className = "attendance-btn";
//           btn.innerHTML = `<svg class="btn-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="m15.477 12.89 1.515 8.526a.5.5 0 0 1-.81.47l-3.58-2.687a1 1 0 0 0-1.197 0l-3.586 2.686a.5.5 0 0 1-.81-.469l1.514-8.526"/><circle cx="12" cy="8" r="6"/></svg>Mark Present (+25 points)`;
//         }
//       }

//       function hasAttendedToday(userId) {
//         const today = new Date().toISOString().split("T")[0];
//         return todayAttendance.some(
//           (record) => record.userId === userId && record.date === today,
//         );
//       }

//       async function handleAddAttendance() {
//         if (!selectedUser || hasAttendedToday(selectedUser.id)) return;

//         // Show loading state
//         const btn = document.getElementById("attendance-btn");
//         btn.disabled = true;
//         btn.innerHTML = `<div class="loading-spinner"></div>Recording Attendance...`;

//         try {
//           // Simulate API call
//           await new Promise((resolve) => setTimeout(resolve, 500));

//           // Add attendance record
//           const now = new Date();
//           const record = {
//             id: `attendance_${Date.now()}`,
//             userId: selectedUser.id,
//             meetingId: "meeting_001",
//             date: now.toISOString().split("T")[0],
//             pointsEarned: 25,
//             timestamp: now.toISOString(),
//           };

//           todayAttendance.push(record);

//           // Update user points
//           selectedUser.points += 25;

//           showToast(
//             "Attendance recorded!",
//             `${selectedUser.name} has been marked present and earned 25 points.`,
//             "success",
//           );

//           // Clear form
//           document.getElementById("search-input").value = "";
//           selectedUser = null;
//           displayEmptyUser();
//           updateAttendanceButton();
//           updateAttendanceTable();
//           updateStats();
//         } catch (error) {
//           showToast(
//             "Error",
//             "Failed to record attendance. Please try again.",
//             "error",
//           );
//         }
//       }

//       function updateAttendanceTable() {
//         const emptyState = document.getElementById("empty-state");
//         const statsSection = document.getElementById("attendance-stats");
//         const tableSection = document.getElementById("attendance-table");
//         const tbody = document.getElementById("attendance-tbody");

//         if (todayAttendance.length === 0) {
//           emptyState.classList.remove("hidden");
//           statsSection.classList.add("hidden");
//           tableSection.classList.add("hidden");
//         } else {
//           emptyState.classList.add("hidden");
//           statsSection.classList.remove("hidden");
//           tableSection.classList.remove("hidden");

//           // Update stats
//           const totalPoints = todayAttendance.reduce(
//             (sum, record) => sum + record.pointsEarned,
//             0,
//           );
//           document.getElementById("stats-attendees").textContent =
//             todayAttendance.length;
//           document.getElementById("stats-points").textContent = totalPoints;
//           document.getElementById("attendee-count").textContent =
//             `${todayAttendance.length} attendees`;

//           // Update table
//           tbody.innerHTML = "";
//           todayAttendance
//             .sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp))
//             .forEach((record, index) => {
//               const user = users.find((u) => u.id === record.userId);
//               const time = new Date(record.timestamp).toLocaleTimeString(
//                 "en-US",
//                 {
//                   hour: "2-digit",
//                   minute: "2-digit",
//                   hour12: true,
//                 },
//               );

//               const row = document.createElement("tr");
//               row.className = "table-row";
//               row.style.animationDelay = `${index * 0.1}s`;
//               row.innerHTML = `
//                             <td class="table-cell">${String(index + 1).padStart(2, "0")}</td>
//                             <td class="table-cell">
//                                 <div class="user-cell">
//                                     <div class="user-avatar-small">${user.avatar}</div>
//                                     <div class="user-cell-info">
//                                         <div class="user-cell-name">${user.name}</div>
//                                         <div class="user-cell-meta">ID: ${user.id} • ${user.phone}</div>
//                                     </div>
//                                 </div>
//                             </td>
//                             <td class="table-cell hidden-mobile">${user.id}</td>
//                             <td class="table-cell hidden-tablet">${user.phone}</td>
//                             <td class="table-cell">
//                                 <div class="points-earned-badge">+${record.pointsEarned}</div>
//                             </td>
//                             <td class="table-cell">${time}</td>
//                         `;
//               tbody.appendChild(row);
//             });
//         }
//       }


//     async function generateUser(userPhone){
//         user =[];
//             const baseURL   = getBaseUrl();
//             try{
//             const response  = await fetch(baseURL+`/api/profile/search?phone=${userPhone}`);
//             if(!response.ok){
//                 throw new Error("Network response was not ok: " + response.status);
//             }
//             const data     = await response.json();
//             console.log(data);
//             return data
//         }catch(error){
//                     console.error("Fetch error:", error);
//             }
//       }

// function generateAutoCompleteNumber(user){
//         const listOfUsers = document.getElementById("suggestUsers");
//         listOfUsers.innerHTML = "";
//         if (!user || user.length === 0) return;
//           user.forEach(u => {
//     const li = document.createElement("li");
//     li.textContent = `${u.name} - ${u.phone}`;
//     li.style.cursor = "pointer";
//     li.style.padding = "5px 10px";
//     li.style.borderBottom = "1px solid #ccc";
//     li.addEventListener("click", () => {
//       document.getElementById("search-input").value = u.phone;
//       listOfUsers.innerHTML = "";
//     });

//     listOfUsers.appendChild(li);
//   });
// }

// function clearSuggestions() {
//   document.getElementById("suggestUsers").innerHTML = "";
// }
// let typingTimer; // Timer identifier
// const debounceDelay = 500; // Delay in ms

// document.getElementById("phoneInput").addEventListener("input", () => {
//   clearTimeout(typingTimer);
//   const phone = document.getElementById("phoneInput").value.trim();

//   typingTimer = setTimeout(() => {
//     if (phone.length >= 3) { // Wait until at least 3 digits
//       data = generateUser(phone);
      
//     } else {
//       clearSuggestions();
//     }
//   }, debounceDelay);
// });



const searchInput                = document.getElementById("search-input");
const userList                   = document.getElementById("suggestUsers");
const btn_attendance             = document.getElementById("attendance-btn");
 const baseURL      = getBaseUrl();

 document.addEventListener("click", function (event) {
  if (!userList.contains(event.target) && !searchInput.contains(event.target)) {
    userList.innerHTML     = "";
    userList.style.display = "none";
  }
});

function showToast(title, message, type = "info") {
        const toastContainer = document.getElementById("toast-container");
        const toast          = document.createElement("div");
        toast.className      = `toast toast-${type}`;

        toast.innerHTML = `
                <div class="toast-content">
                    <div class="toast-title">${title}</div>
                    <div class="toast-message">${message}</div>
                </div>
                <button class="toast-close" onclick="this.parentElement.remove()">×</button>
            `;

        toastContainer.appendChild(toast);

        // Auto remove after 5 seconds
        setTimeout(() => {
          if (toast.parentElement) {
            toast.remove();
          }
        }, 5000);
}

async function generateUser(userPhone){
    try{
        const response  = await fetch(baseURL+`/api/profile/search?phone=${userPhone}`);
        if(!response.ok){
            throw new Error("Network response was not ok: " + response.status);
        }
        const data     = await response.json();
        console.log(data.length);
        renderUserList(data);
        return data
        }catch(error){
           console.error(err);
            renderUserList(null, "Error loading users");
        }
}


function debounce(fn, wait) {
    let timeOut;
    return function(...args) {
        clearTimeout(timeOut);
        timeOut = setTimeout(() => fn(...args), wait);
    };
}

const debouncedSearch = debounce(function(event) {
    const phone = event.target.value;
     btn_attendance.disabled = true;
     btn_attendance.classList.add("disabled");
    generateUser(phone);
}, 200);

searchInput.addEventListener("keyup", debouncedSearch);

 function renderUserList(users,errorMessage){
 const userList = document.getElementById("suggestUsers");
    userList.innerHTML = ""; 

    if (errorMessage) {
        userList.innerHTML = `<li>${errorMessage}</li>`;
        return;
    }
    if (!users || users.length === 0) {
        userList.style.display = "none";
        return;
    }
    userList.style.display = "block";
    users.forEach(user => {
        const li = document.createElement("li");
        li.textContent = `${user.firstName} ${user.lastName}`;
        userList.appendChild(li);
        li.addEventListener("click",async ()=>{
            displayUser(user);
            userList.style.display = "none";
            btn_attendance.classList.toggle("disabled");
            btn_attendance.disabled = !btn_attendance.disabled;
        })
    });
}

    function displayUser(user) {
        const userDisplay          = document.getElementById("user-display");
        searchInput.value          = `${user.phone}`;
        userDisplay.className      = "user-display filled";
        userDisplay.innerHTML      = `
                <div class="user-info">
                    <img src ="${baseURL}/images/${user.imagePath}" class="user-avatar" />
                    <div class="user-details">
                        <h3 class="user-name">${user.firstName} ${user.lastName}</h3>
                        <div class="user-meta">
                            <span>ID: ${user.id}</span>
                            <span>Phone: ${user.phone}</span>
                            <input type = "hidden" id = "userId" value=${user.userId} />
                        </div>
                    </div>
                </div>
            `;
      }

      async function markPersent() {
        try{

            btn_attendance.disabled = true;
            const btnInnerHtml  = btn_attendance.innerHTML;
            btn_attendance.innerHTML = `<div class="loading-spinner"></div>Recording Attendance...`;
            let meetingCode  = document.getElementById("meetingCode").value;
            let userId       = document.getElementById("userId").value;
             const response  = await fetch(baseURL+`/api/scanner/${meetingCode}/${userId}`);
             console.l
            if(!response.ok){
                const errorData    = await response.json();
                btn_attendance.innerHTML      = btnInnerHtml;
                throw new Error("Error : " + errorData.message);
            }
            const user     = await response.json();
            updateAttendanceTable(user); 
            btn_attendance.innerHTML  = btnInnerHtml;
            showToast("Attendance recorded!",`${user.firstName} has been marked present and earned ${user.points} Wazna.`,
            "success",
            );
            }catch(error){
                showToast("Error", error.message, "error");
                btn_attendance.disabled = false;
                return;
            }
            btn_attendance.disabled = false;
      }


function updateAttendanceTable(user) {
    const tbody = document.getElementById("attendance-tbody");
    const currentTime = new Date().toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit"
    });

    const row          = document.createElement("tr");
    let rowNumInput    = document.getElementById("rowNum");
    let rowNum = 1;
    if (rowNumInput) {
        rowNum = parseInt(rowNumInput.value) || 1;
    }
    row.className = "table-row";

    row.innerHTML = `
        <td class="table-cell hidden-mobile ">${rowNum}</td>
        <td class="table-cell hidden-mobile ">${user.userId}</td>
        <td class="table-cell">${user.firstName} ${user.lastName}</td>
        <td class="table-cell">${user.points}</td>
        <td class="table-cell">${user.balance}</td>
        <td class="table-cell hidden-mobile ">${currentTime}</td>
    `;
    tbody.appendChild(row);
    rowNumInput.value = rowNum + 1;
}

      function displayEmptyUser() {
        const userDisplay = document.getElementById("user-display");
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

function cancelAttendance(){
 const userList             = document.getElementById("suggestUsers");
    userList.innerHTML      = ""; 
    userList.style.display  = "none";
    searchInput.value       = "";
    displayEmptyUser();
}