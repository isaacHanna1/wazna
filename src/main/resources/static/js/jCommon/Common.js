function validateRequiredField(inputElement, msgErrId, message) {
    // Validate parameters
    if (!inputElement || !msgErrId) {
        console.error('Missing required parameters');
        return false;
    }

    const msgErrElement = document.getElementById(msgErrId);
    if (!msgErrElement) {
        console.error(`Error message element with ID ${msgErrId} not found`);
        return false;
    }

    const value = inputElement.value.trim();

    // Clear previous state
    msgErrElement.textContent = '';
    inputElement.classList.remove('invalid', 'valid');

    if (value === '') {
        // Show error state
        msgErrElement.textContent = message;
        inputElement.classList.add('invalid');
        return false;
    }

    // Show valid state
    inputElement.classList.add('valid');
    return true;
}

function validateRequiredSelect(selectElement, msgErrId, message) {
    // Validate parameters
    if (!selectElement) {
        console.error('Select element is required');
        return false;
    }

    const msgErrElement = document.getElementById(msgErrId);
    if (!msgErrElement) {
        console.error(`Error message element with ID ${msgErrId} not found`);
        return false;
    }

    // Clear previous state
    msgErrElement.textContent = '';
    selectElement.classList.remove('invalid', 'valid');

    // Get selected value (fixed typo from your original: selectElement.selectElement.value)
    const value = selectElement.value;

    if (value === '' || value === null || value === undefined) {
        // Show error state
        msgErrElement.textContent = message;
        selectElement.classList.add('invalid');
        return false;
    }

    // Show valid state
    selectElement.classList.add('valid');
    return true;
}



function showModal(message) {
    console.log("called");
    const modal = document.createElement('div');
    const modalContent = document.createElement('div');
    const closeButton = document.createElement('span');
    const errorMessage = document.createElement('p');
    
   
    modal.className = 'modal'; 
    modalContent.className = 'modal-content'; 
    console.log(modal);
  
    closeButton.innerHTML = '&times;';
    closeButton.className = 'close'; 
    
    // Set up error message
    errorMessage.textContent = message;
    
    // Append elements
    modalContent.appendChild(closeButton);
    modalContent.appendChild(errorMessage);
    modal.appendChild(modalContent);
    document.body.appendChild(modal);
    
  
    closeButton.onclick = function() {
    document.body.removeChild(modal);
    };
    
   
    modal.onclick = function(event) {
    if (event.target === modal) {
    document.body.removeChild(modal);
    }
    };
    }


    // get base URL for API 

function getBaseUrl() {
  return `${window.location.protocol}//${window.location.host}`;
}


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

// calling API for autoComplete the Profile of user (profile ID  , First_name , Last_name)

async function getProfile(keyword, churchId, meetingId) {
    const URL = getBaseUrl();
    try {
        const response = await fetch(`${URL}/api/profile/${keyword}?churchId=${churchId}&meetingId=${meetingId}`, {
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
        return data;

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

function debounce(func, delay) {
    let timeoutId;
    return function(...args) {
        clearTimeout(timeoutId);  
        timeoutId = setTimeout(() => {
            func.apply(this, args); 
        }, delay);
    };
}

// function is used when click a hyperlink to view a containt
 function viewImage() {
    const URL          = getBaseUrl();
    const url = URL+""+document.getElementById("URL").value;
    let alt   ="view";
    // create overlay
    const overlay     = document.createElement('div');
    overlay.className = 'overlay';
    const img         = document.createElement('img');
     img.src          = url
     img.alt          = alt;
     overlay.appendChild(img);
     img.className    = 'img-overlay';
     const btn        = document.createElement('button');
     btn.classList    ="img-overlay-close";
     btn.innerHTML    = '&times;';

 // close handlers
    function closeOverlay() {
      document.body.classList.remove('no-scroll');
      window.removeEventListener('keydown', onKey);
      overlay.remove();
    }
    function onKey(e) {
      if (e.key === 'Escape') closeOverlay();
    }

    // click outside image to close
    overlay.addEventListener('click', closeOverlay);
    // stop closing when clicking inside content
    btn.addEventListener('click', closeOverlay);
    window.addEventListener('keydown', onKey);

     overlay.appendChild(btn);


     document.body.appendChild(overlay);
  }

  // compress function 
  async function compressImage(file, maxWidth, quality) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    const img = new Image();

    reader.onload = (e) => {
      img.src = e.target.result;
    };

    img.onload = () => {
      const canvas  = document.createElement("canvas");
      const scale   = Math.min(1, maxWidth / img.width);
      canvas.width  = img.width * scale;
      canvas.height = img.height * scale;

      const ctx = canvas.getContext("2d");
      ctx.drawImage(img, 0, 0, canvas.width, canvas.height);

      canvas.toBlob(
        (blob) => resolve(blob),
        "image/jpeg",
        quality
      );
    };

    img.onerror = reject;
    reader.onerror = reject;

    reader.readAsDataURL(file);
  });
}