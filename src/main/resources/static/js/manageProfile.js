async function profileSearch(keyWord , churchId,meetingId) {
    const profiles          = await getProfile(keyWord,churchId,meetingId);
    showProfileSuggestion(profiles);
}

function showProfileSuggestion(profiles) {
    const list          = document.getElementById("profileSuggestion");
    list.innerHTML      = ''; 

    if (profiles.length === 0) {
        list.style.display = 'none';
        return;
    }

    profiles.forEach(profile => {
        const listItem = document.createElement('li');
        listItem.textContent = `${profile.firstName} ${profile.lastName}`;
        listItem.style.padding = '5px';
        listItem.style.cursor = 'pointer';

        listItem.addEventListener('click', () => {
            document.getElementById('searchBox').value          = `${profile.firstName} ${profile.lastName}`;
            document.getElementById('searchProfileId').value    = `${profile.id}`;
            list.style.display                                  = 'none'; 
        });

        list.appendChild(listItem);
    });

    list.style.display = 'block';
}
const churchId    = document.getElementById("churchId").value;
const meetingId   = document.getElementById("meetingId").value;
document.getElementById('searchBox').addEventListener('input', debounce(function(e) {
    const keyword = e.target.value.trim();
    if (keyword.length > 0) {
        profileSearch(keyword, churchId, meetingId);
    } else {
        document.getElementById('searchProfileId').value  = "";
        document.getElementById('profileSuggestion').style.display = 'none';
    }
}, 300));

const filterBtn = document.getElementById("filter_btn");
filterBtn.addEventListener('click', function() {
    document.getElementById("pageNum").value = "1";
});

async function updateProfileStatus(element){
    const userName          = element.getAttribute('data-userName');
    let isEnabled           = element.getAttribute("data-isenabled") 
    const URL               = getBaseUrl();
     const isEnabledBool    = isEnabled === "true";
    const fullURL           = `${URL}/api/users/${userName}/status?enabled=${!isEnabledBool}`;
     try {
    const response = await fetch(fullURL, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      }
    });
    if (!response.ok) {
            showToast("Error", "Internal Error Happening", "error");
    }else{
            location.reload();
    }
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
}



// Start of Pop Up Model 
async function openRolePopup(element) {
    const loadingMessage = document.getElementById('loadingMessage');
    loadingMessage.textContent="Loading.....";
    loadingMessage.style.display = 'none';

    const fullName     = element.getAttribute('data-fullName');
    const userName     = element.getAttribute('data-userName');
    const currentRoles = await getCurrentRole(userName);
    console.log(fullName);
    const userSpan           = document.getElementById('roleModal').querySelector('#popup-profile-name');
    userSpan.textContent     = fullName;
    document.getElementById("userName").value=userName;
    // dispay the popup 
    document.getElementById("roleModal").style.display = "block";
  
    let current_role     = currentRoles.length > 0 
    ? currentRoles.map(role => role.name).join(", ") 
    : "";
    const URL          = getBaseUrl();
    const fullURL      = `${URL}/api/role`;

    const response         = await fetch(fullURL,{
    method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      }
    });
    if(!response.ok){
        const data = response.json();
         throw new Error(errorData.message || 'Failed to fetch roles');
    }
    const roles = await response.json();
        let rolesHtml = "";
        roles.forEach(role => {
            let checked = (current_role == role.name) ? "checked" : "";
            rolesHtml += `
                <div class="form-check">
                    <label class="form-check-label" for="role-${role.id}">
                        ${role.name}
                    </label>
                    <input class="form-check-input" type="radio" value="${role.id}" name ="roleId" id="role-${role.id}" ${checked}>
                </div>
            `;
        });
            document.getElementById("role-list").innerHTML = rolesHtml;

}

async function getCurrentRole(userName) {
        const URL          = getBaseUrl();
        const fullURL      = `${URL}/api/users/roles/${userName}`;
        const response     = await fetch(fullURL,{
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
        });
        if(!response.ok){
            const data = response.json();
            throw new Error(errorData.message || 'Failed to fetch roles');
        }
        const roles = await response.json();
        return roles;
}

function closeModal() {
    document.getElementById("roleModal").style.display = "none";
}

window.onclick = function(event) {
    let modal = document.getElementById("roleModal");
    if (event.target === modal) {
        closeModal();
    }
}

async function changeRole(userName , roleId) {
         const URL          = getBaseUrl();
        const fullURL      = `${URL}/api/users/${userName}/${roleId}`;
        const response     = await fetch(fullURL,{
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        }
        });
        if(!response.ok){
             throw new Error('Failed to update role');
        }
          const message = await response.text();
        }

async function save(element){
    let message         = "Role Updated..... ";
    
    try{
        const userName       = document.getElementById("userName").value;
        const roleId         = document.querySelector('input[name="roleId"]:checked').value;
        const loadingMessage = document.getElementById('loadingMessage');
        loadingMessage.textContent="Loading.....";
        element.disabled = true;
        loadingMessage.style.display = 'block';
       await changeRole(userName,roleId);
    }catch(error){
         console.log('Error: ' + error.message);
         message  = "Internal Error "
    }finally {
            element.disabled = false;
            loadingMessage.textContent = message;
            closeModal();
    }
}
// End of Pop up 


let currentProfileIdToDelete = null;

function showDeleteConfirmation(button) {
    currentProfileIdToDelete = button.dataset.id;
    const modal = document.getElementById('deleteConfirmationModal');
    modal.style.display = 'block';
    
    // Reset confirmation input
    document.getElementById('confirmText').value = '';
    document.getElementById('confirmDeleteBtn').disabled = true;
    
    // Add typing validation
    document.getElementById('confirmText').addEventListener('input', function() {
        const confirmBtn = document.getElementById('confirmDeleteBtn');
        confirmBtn.disabled = this.value.toUpperCase() !== 'DELETE';
    });
}

function closeDeleteModal() {
    const modal = document.getElementById('deleteConfirmationModal');
    modal.style.display = 'none';
    currentProfileIdToDelete = null;
}

async function confirmDelete() {
    if (!currentProfileIdToDelete) return;
    
    const confirmBtn = document.getElementById('confirmDeleteBtn');
    const originalContent = confirmBtn.innerHTML;
    
    // Show loading state
    confirmBtn.innerHTML = '<div class="youth-loading"></div> Deleting...';
    confirmBtn.disabled = true;
    
    try {
        const URL = getBaseUrl(); 
        const response = await fetch(URL + "/api/profile/delete/" + currentProfileIdToDelete, {
            method: "DELETE",
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.text();
        console.log(data);
        
        // Show success message
        showYouthSuccessMessage();
        
        // Close modal and reload page after success
        setTimeout(() => {
            closeDeleteModal();
            location.reload();
        }, 1500);
        
    } catch (error) {
        console.error("Error deleting profile:", error);
        showYouthErrorMessage();
        
        // Reset button state on error
        confirmBtn.innerHTML = originalContent;
        confirmBtn.disabled = false;
    }
}

function showYouthSuccessMessage() {
    const successMsg = document.createElement('div');
    successMsg.className = 'youth-success';
    successMsg.innerHTML = `
        <i class="fas fa-check-circle"></i>
        <div>
            <div style="font-weight: 500;">Account Deleted</div>
            <div style="font-size: 0.85em; opacity: 0.9;">User account has been removed</div>
        </div>
    `;
    document.body.appendChild(successMsg);
    
    setTimeout(() => {
        successMsg.remove();
    }, 3000);
}

function showYouthErrorMessage() {
    const errorMsg = document.createElement('div');
    errorMsg.className = 'youth-error';
    errorMsg.innerHTML = `
        <i class="fas fa-exclamation-triangle"></i>
        <div>
            <div style="font-weight: 500;">Delete Failed</div>
            <div style="font-size: 0.85em; opacity: 0.9;">Please try again</div>
        </div>
    `;
    document.body.appendChild(errorMsg);
    
    setTimeout(() => {
        errorMsg.remove();
    }, 3000);
}

// Close modal when clicking outside
window.addEventListener('click', function(event) {
    const modal = document.getElementById('deleteConfirmationModal');
    if (event.target === modal) {
        closeDeleteModal();
    }
});