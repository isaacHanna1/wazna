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
    const userName     = element.getAttribute('data-phone');
    console.log(userName)
    const isEnabled     = document.getElementById("isEnabled").value === 'true';
    const URL           = getBaseUrl();
    const fullURL       = `${URL}/api/users/${userName}/status?enabled=${!isEnabled}`;
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