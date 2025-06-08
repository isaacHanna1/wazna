const html5QrCode = new Html5Qrcode("reader");

function startScan() {
  document.getElementById("reader").style.display = "block";
  document.getElementById("stop-btn").style.display = "inline";

  html5QrCode.start(
    { facingMode: "environment" },
    { fps: 10, qrbox: 250 },
    qrCodeMessage => {
      document.getElementById("result").value = qrCodeMessage;
      let userId   = document.getElementById("userId") ;
      if(userId){
      userId = userId.innerText;
      }
      stopScan(); // auto stop after successful scan
      sendQrCode(qrCodeMessage,userId);
    },
    errorMessage => {
      // Handle scan errors if needed
    }
  ).catch(err => {
     console.error("Scan start error:", err);
     showModal("Camera access failed. Please allow Browser to access the camera in iPhone settings.");
   });
}

function stopScan() {
  html5QrCode.stop().then(() => {
    document.getElementById("reader").style.display = "none";
    document.getElementById("stop-btn").style.display = "none";
  }).catch(err => {
    console.error("Stop error:", err);
  });
}
async function sendQrCode(code, id) {
  try {
    const baseURL = getBaseUrl();
    const response  = await fetch(baseURL+`/api/scanner/${code}/${id}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      },
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'An error occurred while processing your request.');
    }

    const data = await response.json();

    if (data.redirectURl && data.points !== undefined && data.balance !== undefined) {
      window.location.href = `${data.redirectURl}?points=${data.points}&balance=${data.balance}`;
    } else {
      throw new Error('Missing required fields in the response data.');
    }

  } catch (error) {
    console.error('Error:', error.message);
    showModal(error.message);
  }
}
