/*
	when the document, check all mandatory field to enter 
*/

// start validation when on blur form textbox 
document.addEventListener("DOMContentLoaded", function () {
    const firstNameEl = document.getElementById("firstName");
    const lastNameEl  = document.getElementById("lastName");
	const phoneEl     = document.getElementById("phone");
	const passwordEl  = document.getElementById("password");
	const passConfEl  = document.getElementById("confirmPassword");
	const birthdayEl  = document.getElementById("birthday");
	const addressEl   = document.getElementById("address");
	const imageEl     = document.getElementById("image");
	const serviceEl   = document.getElementById("serviceStage");
	const fatherEl    = document.getElementById("fatherPeriest");
	const genderEl    = document.getElementById("gender");
	const formEL      = document.getElementById('register-form');
	const mode        = document.getElementById('mode');

    if (firstNameEl) {
        firstNameEl.addEventListener("blur", function () {
            validateRequiredField(this, "fName-err", "First name is required");
        });
    }

    if (lastNameEl) {
        lastNameEl.addEventListener("blur", function () {
            validateRequiredField(this, "lName-err", "Last name is required");
        });
    }
	if(phoneEl){
		phoneEl.addEventListener("blur",function(){
			validateRequiredField(this,"phone-err","Phone is required");
		});
	}
	
	if(passwordEl){
		passwordEl.addEventListener("blur",function(){
			checkPassword();
		});
	}
	if(passConfEl){
		passConfEl.addEventListener("blur",function(){
			checkPasswordConfirmPassword();
		});
	}
	if(birthdayEl){
		birthdayEl.addEventListener("blur",function(){
			validateRequiredField(this,"birthday-err","birthday is required");
		});
	}
	
	if(addressEl){
			addressEl.addEventListener("blur",function(){
			validateRequiredField(this,"address-err","Address is required");
		});
	}
    if (imageEl) {
        if (mode.value !== "edit") {
            imageEl.addEventListener("blur", function () {
                validateRequiredField(this, "image-err", "Image is required");
            });
        }
    }
	if(serviceEl){
		serviceEl.addEventListener("blur",function(){
			validateRequiredSelect(this,"stage-err","Stage is required");
		});
	}
	if(fatherEl){
		fatherEl.addEventListener("blur",function(){
			validateRequiredSelect(this,"father-err","Father is required");
		});
	}
	
	if(genderEl){
		genderEl.addEventListener("blur",function(){
			validateRequiredSelect(this,"gender-err","Gender Is Required");
		});
	}
	
	
	 formEL.addEventListener('submit', function(event) {
        event.preventDefault();
        
        let isValid = checkBeforSubmit();
        if (isValid) {
            this.submit();
        }
    });
	
});

// End validation when on blur form textbox 



// start validation for password 

function checkPassword() {
    const passwordEl = document.getElementById("password");
    const passMsgErrID = document.getElementById("password-err");
    
    if (passwordEl && passMsgErrID) {
        const passwordValue = passwordEl.value;
        
        // Clear previous error
        passMsgErrID.innerText = "";
        passwordEl.classList.remove("invalid");
        
        if (passwordValue.length < 6) {
            passMsgErrID.innerText = "Please enter at least 6 characters";
            passwordEl.classList.add("invalid");
            return false;
        }
    }
    return true;
}

function checkPasswordConfirmPassword() {
    const confirmEl = document.getElementById("confirmPassword");
    const passwordEl = document.getElementById("password");
    const conPassMsgErrID = document.getElementById("confirmPass-err");
    
    if (confirmEl && conPassMsgErrID && passwordEl) {
        const confirmValue = confirmEl.value;
        const passwordValue = passwordEl.value;
        
        // Clear previous error
        conPassMsgErrID.innerText = "";
        confirmEl.classList.remove("invalid");
        
        if (confirmValue !== passwordValue) {
            conPassMsgErrID.innerText = "Confirm password does not match password";
            confirmEl.classList.add("invalid");
            return false;
        }
    }
    return true;
}

// End validation for password  


// Start Submit button 

function checkBeforSubmit() {
	
    let isValid = true;

    const firstNameEl = document.getElementById("firstName");
    const lastNameEl  = document.getElementById("lastName");
    const phoneEl     = document.getElementById("phone");
    const passwordEl  = document.getElementById("password");
    const passConfEl  = document.getElementById("confirmPassword");
    const birthdayEl  = document.getElementById("birthday");
    const addressEl   = document.getElementById("address");
    const imageEl     = document.getElementById("image");
    const serviceEl   = document.getElementById("serviceStage");
    const fatherEl    = document.getElementById("fatherPeriest");
    const genderEl    = document.getElementById("gender");
    const mode        = document.getElementById("mode");

    if (!validateRequiredField(firstNameEl, "fName-err", "First name is required")) isValid = false;
    if (!validateRequiredField(lastNameEl, "lName-err", "Last name is required")) isValid = false;
    if (!validateRequiredField(phoneEl, "phone-err", "Phone is required")) isValid = false;
    if (!checkPassword()) isValid = false;
    if (!checkPasswordConfirmPassword()) isValid = false;
    if (!validateRequiredField(birthdayEl, "birthday-err", "Birthday is required")) isValid = false;
    if (!validateRequiredField(addressEl, "address-err", "Address is required")) isValid = false;
    if(mode.value !="edit"){
        if (!validateRequiredField(imageEl, "image-err", "Image is required")) isValid = false;
    }
    if (!validateRequiredSelect(serviceEl, "stage-err", "Stage is required")) isValid = false;
    if (!validateRequiredSelect(fatherEl, "father-err", "Father is required")) isValid = false;
    if (!validateRequiredSelect(genderEl, "gender-err", "Gender is required")) isValid = false;

    return isValid; 
}
// End Submit button 

// Start Load the church depend on diocese
// Attach event listener to dioceses select
document.getElementById('dioceses').addEventListener('change', async function() {
  const selectedDioceseId = this.value;
  const response = await loadChurchName(selectedDioceseId);
   await builtTheDom(response);
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
        // Remove all options except the first one
    while (churchEle.options.length > 1) {
        churchEle.remove(1);
    }
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
     // Remove all options except the first one
    while (meetingEle.options.length > 1) {
        meetingEle.remove(1);
    }

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
     // Remove all options except the first one
    while (fatherPeriestEle.options.length > 1) {
        fatherPeriestEle.remove(1);
    }

    const fatherPeriest              = await response.json();
    fatherPeriest.forEach(fatherPeriest => {
            const option = document.createElement('option');
            option.value = fatherPeriest.id;
            option.textContent = fatherPeriest.name;
            fatherPeriestEle.appendChild(option);
    });
}
// end 