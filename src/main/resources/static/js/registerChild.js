
// start load the class depend on stage 
document.getElementById("serviceStage").addEventListener("change", function () {
    const serviceStage = document.getElementById("serviceStage");
    const value = serviceStage.value;
    const classSelect = document.getElementById("serviceClass");

    classSelect.innerHTML = '<option value="" selected disabled>Select Service Class</option>';

    let classes = [];

    if (value === "1") { // Prep
        classes = ["أولى", "تانية", "تالتة","رابعة","فيما فوق"];
    } else if (value === "2") { // Sec
        classes = ["أولى", "تانية", "تالتة"];
    } else if (value === "3") { // College
        classes = ["أولى", "تانية", "تالتة", "رابعة", "خامسة", "سادسة"];
    }

    // أضف القيم الجديدة للقائمة
    classes.forEach((className, index) => {
        const option = document.createElement("option");
        option.value = index + 1;
        option.textContent = className;
        classSelect.appendChild(option);
    });
});

// Start Load the church depend on diocese
// Attach event listener to dioceses select
document.getElementById('dioceses').addEventListener('change', async function() {
    console.log("called");
  const selectedDioceseId = this.value;
  const response = await loadChurchName(selectedDioceseId);
   await builtTheDom(response);
   const churchId        = document.getElementById("Church").value;
   const responseMeeting = await loadMeetingByChurchID(churchId);
   builtTheDomOfMeetings(responseMeeting);
   const fatherResponse = await loadPeriestByDiocesesID(selectedDioceseId);
   builtTheDomOfPeriest(fatherResponse);
   
});
async function loadChurchName(dioceseId) {
  const URL = getBaseUrl();
  const fullURL = `${URL}/api/church/${dioceseId}`; 

  try {
    const response = await fetch(fullURL, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      }
    });

    if (!response.ok) {
      throw new Error("Internal Server Error");
    }
    return response;
  } catch (error) {
    console.error("Failed to load churches:", error.message);
  }
}

async function builtTheDom(response){

    const churchEle           = document.getElementById("Church");
    churchEle.innerHTML = "";

 
    const church              = await response.json();
    church.forEach(church => {
            const option = document.createElement('option');
            option.value = church.id;
            option.textContent = church.churchName;
            churchEle.appendChild(option);
    });
}

// End  Load the church depend on diocese

//Start load Meetings 

document.getElementById('Church').addEventListener('change', async function() {
  const selectedChurch = this.value;
  const response = await loadMeetingByChurchID(selectedChurch);
   builtTheDomOfMeetings(response);
});

async function loadMeetingByChurchID(churchId){

  const URL = getBaseUrl();
  const fullURL = `${URL}/api/meeting/${churchId}`; 

  try {
    const response = await fetch(fullURL, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      }
    });

    if (!response.ok) {
      throw new Error("Internal Server Error");
    }
    return response;
  } catch (error) {
    console.error("Failed to load churches:", error.message);
  }
}




async function builtTheDomOfMeetings(response){
    const meetingEle           = document.getElementById("meeting");
        meetingEle.innerHTML = "";
    console.log(response);
    const meeting              = await response.json();
    meeting.forEach(meeting => {
            const option = document.createElement('option');
            option.value = meeting.id;
            option.textContent = meeting.description;
            meetingEle.appendChild(option);
    });
}

// ENd load Meetings


// start load periest 
async function loadPeriestByDiocesesID(diocesesID){

  const URL = getBaseUrl();
  const fullURL = `${URL}/api/periest/${diocesesID}`; 

  try {
    const response = await fetch(fullURL, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      }
    });

    if (!response.ok) {
      throw new Error("Internal Server Error");
    }
    return response;
  } catch (error) {
    console.error("Failed to load churches:", error.message);
  }
}
async function builtTheDomOfPeriest(response){
    const fatherPeriestEle           = document.getElementById("fatherPeriest");
          fatherPeriestEle.innerHTML = "";



    const fatherPeriest              = await response.json();
    fatherPeriest.forEach(fatherPeriest => {
            const option = document.createElement('option');
            option.value = fatherPeriest.id;
            option.textContent = fatherPeriest.name;
            fatherPeriestEle.appendChild(option);
    });
}
// end 


// ask for generte code for userName and Password 
async function generateCode() {
  const URL = getBaseUrl();

  try {
    const response = await fetch(URL + "/api/generate-register-code", {
      method: 'GET',
      headers: { 'Accept': 'text/plain' }
    });

    if (!response.ok) {
      console.error("Failed to fetch code:", response.statusText);
      return;
    }

    const data = await response.text();
    const usernameEle = document.getElementById("username");
    const passwordEle = document.getElementById("password");

    usernameEle.value = data;
    passwordEle.value = data;

    usernameEle.setAttribute("readonly", true);
    passwordEle.setAttribute("readonly", true);
  } catch (err) {
    console.error("Error:", err);
  }
}