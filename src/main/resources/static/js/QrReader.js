
  // Import ZXing (works in Safari, Chrome, etc.)
  import { BrowserMultiFormatReader } from 'https://cdn.jsdelivr.net/npm/@zxing/browser@latest/+esm';

  const videoElement  = document.getElementById('camera-video');
  const scanResult    = document.getElementById('search-input');
  const scanResultErr = document.getElementById('scan-result-err');
  const startBtn      = document.getElementById('start-scan-btn');
  const stopBtn       = document.getElementById('stop-scan-btn');

  let codeReader = null;
  let controls = null; 
  let usingNativeDetector = false;
  let detector = null;
  let stream = null;
  let rafId = null;

  async function startNativeDetector() {
    try {
      stream = await navigator.mediaDevices.getUserMedia({ video: { facingMode: 'environment' } });
      videoElement.srcObject = stream;
      videoElement.play();

      detector = new BarcodeDetector({ formats: ['qr_code', 'code_128', 'ean_13'] });
      usingNativeDetector = true;
      scanResultErr.value = 'Scanning...';
      scanLoop();
    } catch (err) {
      scanResultErr.textContent = 'Camera access denied: ' + err.message;
    }
  }

  async function scanLoop() {
    if (!detector || !videoElement) return;
    try {
      const barcodes = await detector.detect(videoElement);
      if (barcodes.length > 0) {
        const code = barcodes[0].rawValue;
        scanResult.value =code;
       await  handleScannedCode(code);
      }
    } catch (err) {
      console.error('Detection error:', err);
    } finally {
      rafId = requestAnimationFrame(scanLoop);
    }
  }


  startBtn.addEventListener('click', async () => {
    scanResultErr.textContent = 'Initializing camera...';

    if ('BarcodeDetector' in window) {
      startNativeDetector();
    } else {
      codeReader = new BrowserMultiFormatReader();
      try {
        controls = await codeReader.decodeFromVideoDevice(null, videoElement, async  (result, err) => {
          if (result) {
            const code = result.getText();
            scanResult.value =  code;
            await handleScannedCode(code);
          }
        });
      } catch (e) {
        scanResultErr.textContent = 'Camera error: ' + e.message;
      }
    }
  });

  stopBtn.addEventListener('click', () => {
    scanResultErr.textContent = 'Camera stopped.';
    if (usingNativeDetector) {
      if (stream) stream.getTracks().forEach(t => t.stop());
      cancelAnimationFrame(rafId);
    } else if (controls) {
      controls.stop(); 
    }
  });


  async function handleScannedCode(code) {
  try {
    generateUser(code);

    // Optionally stop camera to prevent multiple scans
    if (usingNativeDetector && stream) {
      stream.getTracks().forEach(t => t.stop());
      cancelAnimationFrame(rafId);
    } else if (controls) {
      controls.stop();
    }

    // Wait a short moment to let the UI update
    await new Promise(r => setTimeout(r, 300));
  } catch (err) {
    console.error("Error after scanning:", err);
    scanResultErr.textContent = "Error while marking attendance: " + err.message;
  }
}
