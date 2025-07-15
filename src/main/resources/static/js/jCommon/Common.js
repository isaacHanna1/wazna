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
    const modal = document.createElement('div');
    const modalContent = document.createElement('div');
    const closeButton = document.createElement('span');
    const errorMessage = document.createElement('p');
    
   
    modal.className = 'modal'; 
    modalContent.className = 'modal-content'; 
    
  
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