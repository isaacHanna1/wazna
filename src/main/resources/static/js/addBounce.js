// Bounty points mapping
let bountyPoints = [];



let userData = [];

let selectedUser = null;
let isSearching = false;

// DOM elements
const userNameInput                     = document.getElementById('userName');
const userFullName                      = document.getElementById('userFullName');
const bountyTypeSelect                  = document.getElementById('bountyType');
const currentPointsDisplay              = document.getElementById('currentPoints');
const addPointsInput                    = document.getElementById('addPoints');
const addBtn                            = document.getElementById('addBtn');
const form                              = document.getElementById('bountyForm');
const userId                            = document.getElementById('userId');

// Initialize when page loads
document.addEventListener('DOMContentLoaded', async  function() {
    bountyPoints = await loadBonusType();
    initializeEventListeners();
    
});

// Initialize event listeners
function initializeEventListeners() {
    userNameInput.addEventListener('input', handleUserSearch);
    bountyTypeSelect.addEventListener('change', handleBountyTypeChange);
    form.addEventListener('submit', handleFormSubmit);
}

// Handle user search with animation
function handleUserSearch(event) {
    const phone = event.target.value;

    if (phone.length >= 3) {
        showSuggestList(phone);
        showSearchingAnimation();

    }
}


// Clear user selection
function clearUserSelection() {
    selectedUser = null;
    currentPointsDisplay.textContent = '0';
    addPointsInput.value = '';
    updateFormState();
}

// Handle bounty type change
function handleBountyTypeChange(event) {
    const bonusId = event.target.value;
    let points = 0; 
    if (bonusId){
        bountyPoints.find(bonus=>{
            if(bonus.id == bonusId)
            points = bonus.point;
        });
        addPointsInput.value = points;
        // Add animation to points input
        addPointsInput.style.transform = 'scale(1.05)';
        addPointsInput.style.background = 'linear-gradient(135deg, #dcfce7 0%, #22c55e 20%)';

        setTimeout(() => {
            addPointsInput.style.transform = 'scale(1)';
        }, 200);
    } else {
        addPointsInput.value = '';
    }

    updateFormState();
}

// Update form state
function updateFormState() {
    const isValid = bountyTypeSelect.value && addPointsInput.value;
    addBtn.disabled = !isValid;

    if (isValid) {
        addBtn.classList.add('btn-ready');
    } else {
        addBtn.classList.remove('btn-ready');
    }
}

// Handle form submission
function handleFormSubmit(event) {
    event.preventDefault();

    if (!bountyTypeSelect.value) {
        showErrorMessage('Please select a user and bounty type');
        return;
    }

    submitBounty();
}

// Submit bounty (integrate with Spring Boot)
function submitBounty() {

    let theProfileId  = userFullName.id
    let theUserId     = userId.innerText ;
    let bonusTypeId   = bountyType.value; 
    let points        = addPointsInput.value
   
    addPoints(theProfileId , theUserId , bonusTypeId);
    setTimeout(() => {
        handleSubmissionSuccess(points);
    }, 1000);
}

// Handle successful submission
function handleSubmissionSuccess(addPoints) {

    // Show success animation
    showSuccessMessage(` ${addPoints} points added successfully!`);

    resetForm();

    hideSubmittingAnimation();
}

// Animation functions
function showSearchingAnimation() {
    if (!isSearching) {
        isSearching = true;
        userNameInput.style.borderColor = '#3b82f6';
        userNameInput.style.background = 'linear-gradient(90deg, #eff6ff 0%, #dbeafe 50%, #eff6ff 100%)';
        userNameInput.style.backgroundSize = '200% 100%';
        userNameInput.style.animation = 'shimmer 1.5s infinite';
    }
}

function hideSearchingAnimation() {
    isSearching = false;
    userNameInput.style.animation = '';
    userNameInput.style.background = 'white';
    userNameInput.style.borderColor = '#e2e8f0';
}

function showSubmittingAnimation() {
    addBtn.innerHTML = '<span class="spinner"></span> Adding Points...';
    addBtn.disabled = true;
    addBtn.style.background = 'linear-gradient(135deg, #6b7280 0%, #9ca3af 100%)';
}

function hideSubmittingAnimation() {
    addBtn.innerHTML = '<span class="btn-icon">➕</span> Add Points';
    addBtn.disabled = false;
    addBtn.style.background = 'linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%)';
}

function showSuccessMessage(message) {
  const successDiv = document.createElement('div');
  successDiv.className = 'success-message';
  successDiv.textContent = message;

  document.body.appendChild(successDiv);

  // Trigger fade-in and scale after DOM render
  setTimeout(() => {
    successDiv.classList.add('show');
  }, 50);

  // Hide after 3 seconds
  setTimeout(() => {
    successDiv.classList.remove('show');
    setTimeout(() => {
      document.body.removeChild(successDiv);
    }, 300); // match CSS transition
  }, 2000);
}

function showErrorMessage(message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.textContent = message;

    document.body.appendChild(errorDiv);

    setTimeout(() => {
        errorDiv.style.opacity = '1';
        errorDiv.style.transform = 'translateY(0)';
    }, 100);

    setTimeout(() => {
        errorDiv.style.opacity = '0';
        errorDiv.style.transform = 'translateY(-20px)';
        setTimeout(() => {
            document.body.removeChild(errorDiv);
        }, 300);
    }, 3000);
}

function showNoUserFound() {
    userNameInput.style.borderColor = '#ef4444';
    userNameInput.style.animation = 'shake 0.5s ease-in-out';

    setTimeout(() => {
        userNameInput.style.borderColor = '#e2e8f0';
        userNameInput.style.animation = '';
    }, 1000);
}

function resetForm() {
    bountyTypeSelect.value = '';
    addPointsInput.value   = '';
    userFullName.value     = '';
    updateFormState();
}

async function showSuggestList(input){
  const infoBox = document.getElementById('userInfoBox');
 const query = input;
    infoBox.style.display = 'none';

    if (query.length === 0) {
      return;
    }
   userData = await fetchUserDate(query);
  if (!userData || userData.length === 0) {
    infoBox.innerHTML = "<ul><li>Not Found</li></ul>";
    infoBox.style.display = 'block';
    return;
  }

let elementConstruct = "<ul>";
  userData.forEach(user => {
    const userJson = encodeURIComponent(JSON.stringify(user));
    elementConstruct += `<li id="${user.id}" onclick='selectTheUser("${userJson}")'>${user.name}</li>`;
  });
  elementConstruct += "</ul>";

  infoBox.innerHTML = elementConstruct;
  infoBox.style.display = 'block';

  // Optional: hide box on blur
 userNameInput.addEventListener('blur', () => {
   setTimeout(() => {
     infoBox.style.display = 'none';
     if(userNameInput.value == ""){
        userFullName.value = "";
     }
   }, 200);
 });
}

function selectTheUser(userStr){
    const user              = JSON.parse(decodeURIComponent(userStr));
    const infoBox           = document.getElementById('userInfoBox');
    userFullName.value      = '';
    userFullName.id         = '';
    userNameInput.value     = '';
    userFullName.value      = `${user.name}`;
    userFullName.id         = `${user.id}`;
    userNameInput.value     = `${user.phone}`;
    infoBox.style.display   = 'none';
    currentPointsDisplay.innerText =`${user.currentPoints}`;
    userNameInput.focus();
}

async function fetchUserDate(phone) {
    const URL = getBaseUrl();

    try {
        const response = await fetch(`${URL}/api/youth/point/${phone}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        });

        if (!response.ok) {
            throw new Error("Error when fetching the user data");
        }

        const data = await response.json();

        // Convert each user into a clean object
        const users = data.map(user => {
            const fullName = `${user.firstName || ''} ${user.lastName || ''}`.trim();
            return {
                id:user.id,
                phone: user.phone,
                name: fullName,
                currentPoints: user.points || 0
            };
        });

        return users;

    } catch (error) {
        console.error("Fetch error:", error);
        return [];
    }
}


async function loadBonusType() {
    const URL          = getBaseUrl();
    try {
        const response = await fetch(`${URL}/api/bonusType`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        });

        if (!response.ok) {
            throw new Error("Error when fetching the user data");
        }

        const data     = await response.json();

        // Convert each user into a clean object
       const  bountyPoints = data.map(bonus => {
            let bonusDesc = [bonus.description]
            return {
                'id':bonus.id,
                [bonus.description]: bonus.point,
                'point': bonus.point
            };
        });

        return bountyPoints;

    } catch (error) {
        console.error("Fetch error:", error);
        return [];
    }
}

async function addPoints(profileId, userId, bonusTypeId) {
    const URL = getBaseUrl(); // adjust this to your context
    try {
        const response = await fetch(`${URL}/api/youth/point/${profileId}/${userId}/${bonusTypeId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error("Failed to add points");
        }

        console.log("Points added successfully");

    } catch (error) {
        console.error("Error while adding points:", error);
    }
}
