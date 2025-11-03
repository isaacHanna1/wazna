
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
    const pageInfo     = this.document.getElementById("pageInfo");
    //  Trigger stage change to populate class options // when change the data is getting placed in select of classes 
    serviceStage.dispatchEvent(new Event("change"));
if(pageInfo.value =="modify"){
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
}
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

/* Start  validation  */

function validateBeforeSubmit(event){
    event.preventDefault(); //  stop form from submitting

  const firstName       = document.getElementById("firstName");
  const lastName        = document.getElementById("lastName");
  const gender          = document.getElementById("gender");
  const username        = document.getElementById("username");
  const dioceses        = document.getElementById("dioceses");
  const serviceStage    = document.getElementById("serviceStage");
  const serviceClass    = document.getElementById("serviceClass");
  const address         = document.getElementById("address");
  const birthday        = document.getElementById("birthday")
  const image           = document.getElementById("image");
  const pageInfo        = document.getElementById("pageInfo");
  const fatherName      = document.getElementById("fatherName");
  const fatherTelephone = document.getElementById("fatherTelephone");
  const motherName      = document.getElementById("motherName");
  const motherTelephone = document.getElementById("motherTelephone");
  const fatherExists    = document.querySelector('input[name="familyInfo.fatherExists"]:checked');
  const motherExists    = document.querySelector('input[name="familyInfo.motherExists"]:checked');

  // check the first Name 
  if(firstName && firstName.value.trim() ==""){
    showToast("Error","Enter First Name - الاسم الاول مطلوب","error");
    return;
  }
    // check the name 
  if(lastName && lastName.value.trim() ==""){
    showToast("Error","Enter Last Name -  الاسم الاخير مطلوب ","error");
    return;
  }
  // check gender 
  if(gender && gender.value.trim() ==""){
    showToast("Error","Enter Gendre -  نوع ذكر / انثي   ","error");
    return;
  }

  if(username && username.value.trim()==""){
    showToast("Error","Enter User Name - اسمم المستخدم مطلوب ","error");
    return;
  }

  if(dioceses && dioceses.value.trim() == ""){
    showToast("Error","Choose Dioceses - اختر الايبارشية ", "error");
    return;
  }

   if(serviceStage && serviceStage.value.trim() == ""){
    showToast("Error","Choose Service Stage - مرحلة الخدمة مطلوبة ", "error");
    return;
  }
  if(serviceClass && serviceClass.value.trim()==""){
    showToast("Error","Choose Service Class - فصل المخدوم مطلوب ", "error");
    return;
  }
   if(address && address.value.trim() == ""){
    showToast("Error","Enter Address -  عنوان المخدوم مطلوب   ", "error");
    return;
  }
  if(birthday && birthday.value.trim()==""){
    showToast("Error","Enter Birthday -   ادخل تاريخ الميلاد المخدوم ", "error");
    return;
  }
  if(pageInfo && pageInfo.value=="insert" && image && image.value.trim()==""){
    showToast("Error","Enter  Profile Image -    ادخل الصورة الشخصية المخدوم   ", "error");
    return;
  }

  if(fatherName && fatherName.value.trim()==""){
    showToast("Error","Enter father Name  - اسم الأب مطلوب ", "error");
    return;
  }
   if (fatherExists && fatherExists.value === "true" && fatherTelephone.value.trim() === "") {
  showToast("Error","Enter Father Telephone - رقم التليفون الأب مطلوب", "error");
  return false;
}
 if(motherName && motherName.value.trim()==""){
    showToast("Error","Enter Mother Name   - اسم الأم مطلوب", "error");
    return;
  }
 if (motherExists && motherExists.value === "true" && motherTelephone.value.trim() === "") {
  showToast("Error","Enter Mother Telephone - رقم الأم مطلوب", "error");
  return false;
 }
  //all validations passed
  event.target.submit();
  return true;

}

/* End Validation */  