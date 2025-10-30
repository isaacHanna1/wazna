/*
	when the document, check all mandatory field to enter 
*/

// start validation when on blur form textbox 
document.addEventListener("DOMContentLoaded", function () {
    const firstNameEl  = document.getElementById("firstName");
    const lastNameEl   = document.getElementById("lastName");
    const phoneEl      = document.getElementById("phone");
    const passwordEl   = document.getElementById("password");
    const passConfEl   = document.getElementById("confirmPassword");
    const birthdayEl   = document.getElementById("birthday");
    const addressEl    = document.getElementById("address");
    const imageEl      = document.getElementById("image");
    const serviceEl    = document.getElementById("serviceStage");
    const fatherEl     = document.getElementById("fatherPeriest");
    const genderEl     = document.getElementById("gender");
    const formEL       = document.getElementById('register-form');
    const mode         = document.getElementById('mode');
    const serviceClass = document.getElementById('serviceClass');

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
  if(serviceClass){
		serviceClass.addEventListener("blur",function(){
			validateRequiredSelect(this,"serviceClass-err","serviceClass required - فصل المخدوم مطلوب ");
		});
	}
  
	
	
	 formEL.addEventListener('submit',  async function(event) {

    const form       = document.getElementById("register-form");
    const imageInput = document.getElementById("image");
      
    event.preventDefault();
        
        let isValid = checkBeforSubmit();
        if (!isValid) {
            console.log("Validation failed");
            return ;
        }
        const file = imageInput.files[0];
        if (file) {
        const compressed = await compressImage(file, 800, 0.7);
        const compressedFile = new File([compressed], file.name, { type: "image/jpeg" });

        // Replace the original image in the input field
        const dataTransfer = new DataTransfer();
        dataTransfer.items.add(compressedFile);
        imageInput.files = dataTransfer.files;
      }
      form.submit();

	
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

    const firstNameEl  = document.getElementById("firstName");
    const lastNameEl   = document.getElementById("lastName");
    const phoneEl      = document.getElementById("phone");
    const passwordEl   = document.getElementById("password");
    const passConfEl   = document.getElementById("confirmPassword");
    const birthdayEl   = document.getElementById("birthday");
    const addressEl    = document.getElementById("address");
    const imageEl      = document.getElementById("image");
    const serviceEl    = document.getElementById("serviceStage");
    const fatherEl     = document.getElementById("fatherPeriest");
    const genderEl     = document.getElementById("gender");
    const mode         = document.getElementById("mode");
    const serviceClass = document.getElementById('serviceClass');
    const serviceStage = document.getElementById("serviceStage");
    const dioceses     = document.getElementById("dioceses");
    const Church       = document.getElementById("Church");
    const meeting      = document.getElementById("meeting");
    const image        = document.getElementById("image");

  // Start checks
    if (!validateRequiredField(firstNameEl, "fName-err", "First name is required - الاسم الأول")) {
        console.log("❌ Failed: firstName");
        isValid = false;
    }
    if (!validateRequiredField(lastNameEl, "lName-err", "Last name is required - الاسم الاخير ")) {
        console.log("❌ Failed: lastName");
        isValid = false;
    }
    if (!validateRequiredSelect(genderEl, "gender-err", "Gender is required -  النوع")) {
        console.log("❌ Failed: gender");
        isValid = false;
    }
    if (!validateRequiredField(phoneEl, "phone-err", "Phone is required - رقم التليفون ")) {
        console.log("❌ Failed: phone");
        isValid = false;
    }
    if (!checkPassword()) {
        console.log("❌ Failed: checkPassword()");
        isValid = false;
    }
    if (!checkPasswordConfirmPassword()) {
        console.log("❌ Failed: checkPasswordConfirmPassword()");
        isValid = false;
    }
    if (!validateRequiredSelect(dioceses,"dioceses-err","Dioceses Required - الإيبارشية مطلوبة ")) {
        console.log("❌ Failed: dioceses");
        isValid = false;
    }
    if (!validateRequiredSelect(Church,"church-err","Church Required - الكنيسة مطلوبة ")) {
        console.log("❌ Failed: church");
        isValid = false;
    }
    if (!validateRequiredSelect(meeting,"meetings-err","Meeting Required - الاجتماع مطلوب ")) {
        console.log("❌ Failed: meeting");
        isValid = false;
    }
    if (!validateRequiredSelect(fatherEl, "father-err", "Father is required - أب الكاهن ")) {
        console.log("❌ Failed: father");
        isValid = false;
    }
    if (!validateRequiredSelect(serviceStage,"serviceStage-err","Service Stage Required - مرحلة العمرية مطلوبة")) {
        console.log("❌ Failed: serviceStage");
        isValid = false;
    }
    if (!validateRequiredSelect(serviceClass,"serviceClass-err","Service Class required - فصل المخدوم مطلوب ")) {
        console.log("❌ Failed: serviceClass");
        isValid = false;
    }
    if (!validateRequiredField(birthdayEl, "birthday-err", "Birthday is required - تاريخ الميلاد ")) {
        console.log("❌ Failed: birthday");
        isValid = false;
    }
    if (!validateRequiredField(addressEl, "address-err", "Address is required - العنوان مطلوب ")) {
        console.log("❌ Failed: address");
        isValid = false;
    }
    if(mode.value =="insert"){
        if (!validateRequiredField(image, "image-err", "Image is required الصورة المطلوبة")) {
            console.log("❌ Failed: image");
            isValid = false;
        }
    }

    console.log("✅ Final Result:", isValid);
    return isValid; 
}
// End Submit button 

// Start Load the church depend on diocese
// Attach event listener to dioceses select
document.getElementById('dioceses').addEventListener('change', async function() {
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

// Compress Image before Upload 
document.addEventListener("DOMContentLoaded", () => {
  

    form.addEventListener("submit", async (e) => {
      const file = imageInput.files[0];
      if (file) {
        e.preventDefault(); // temporarily stop
        const compressed = await compressImage(file, 800, 0.7);
        const compressedFile = new File([compressed], file.name, { type: "image/jpeg" });

        // Replace the original image in the input field
        const dataTransfer = new DataTransfer();
        dataTransfer.items.add(compressedFile);
        imageInput.files = dataTransfer.files;

        form.submit(); 
      }
    });
});

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

// Trigger once on page load (for edit mode)
window.addEventListener("DOMContentLoaded", function () {
    const serviceStage = document.getElementById("serviceStage");
    const savedClassId = document.getElementById("savedClassId");
    //  Trigger stage change to populate class options // when change the data is getting placed in select of classes 
    serviceStage.dispatchEvent(new Event("change"));
    // wait some time
    setTimeout(() => {
        const classSelect = document.getElementById("serviceClass");
        for (const option of classSelect.options) {
            if (savedClassId && option.value === savedClassId.value) {
                option.selected = true; 
                break;
            }
        }
    }, 50);
});
