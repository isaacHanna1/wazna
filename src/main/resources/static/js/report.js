// start the daily report


// this one for get ALl bounce active and not active 
// param 
// evaluationType   = PO (postive) , (NE) negaitve
async function getBonuceTypeActiveAndNotActive(evaluationType){

    const URL           = getBaseUrl();
    const fullURL       = `${URL}/api/bonusType/All?evaluationType=${evaluationType}`; 
    try {
        const response  = await fetch(fullURL, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
        });
        if (!response.ok) {
        throw new Error("Internal Server Error");
        }
        const data = await response.json();
        return data
    } catch (error) {
        console.error("Failed to load churches:", error.message);
    }
}

const evaluationTypeFilter  = document.getElementById("waznaType");
const bonusTypeFilter       = document.getElementById("bounce-type-filter");


// When evaluation type changes → load bonus types
evaluationTypeFilter.addEventListener("change", async () => {
    
    const evaluationType            = evaluationTypeFilter.value;
    buildBounceType(evaluationType);
});
window.addEventListener("DOMContentLoaded", async () => {    
    buildBounceType("All");
});

async function  buildBounceType(evaluationType){
    const bonusTypes                = await getBonuceTypeActiveAndNotActive(evaluationType);
    bonusTypeFilter.innerHTML       = "";
    const defaultOption             = document.createElement("option");
    defaultOption.value             = "All";
    defaultOption.textContent       = "الكل";
    bonusTypeFilter.appendChild(defaultOption);

    bonusTypes.forEach(b => {
        const option = document.createElement("option");
        option.value = b.id;          
        option.textContent = b.description;  
        bonusTypeFilter.appendChild(option);
    });
}
// end daily report 


const userNameInput                     = document.getElementById('userName');
const profileId                         = document.getElementById("profileId");
userNameInput.addEventListener("blur",()=>{
if(userNameInput.value ==""){
    userNameInput.value= "";
    profileId.value    = "";
}
});
// Handle user search with animation
function handleUserSearch(event) {
    const userName = event.target.value;

    if (userName.length >= 3) {
        showSuggestList(userName);
    }
}
// Initialize event listeners
function initializeEventListeners() {
    userNameInput.addEventListener('input', handleUserSearch);
}

// Initialize when page loads
document.addEventListener('DOMContentLoaded', async  function() {
    initializeEventListeners();    
});

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
        console.log(data);
        // Convert each user into a clean object
        const users = data.map(user => {
            const fullName = `${user.firstName || ''} ${user.lastName || ''}`.trim();
            return {
                id:user.id,
                phone: user.phone,
                userName:user.userName,
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

function selectTheUser(userJson) {
    const user = JSON.parse(decodeURIComponent(userJson));

    document.getElementById("profileId").value = user.id;
    userNameInput.value = user.name;
    document.getElementById("userInfoBox").style.display = "none";
}