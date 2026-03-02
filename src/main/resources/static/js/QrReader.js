// Import ZXing (works in Safari, Chrome, etc.)
import { BrowserMultiFormatReader } from 'https://cdn.jsdelivr.net/npm/@zxing/browser@latest/+esm';


const videoElement  = document.getElementById('camera-video');
const scanResult    = document.getElementById('search-input');
const scanResultErr = document.getElementById('scan-result-err');
const startBtn      = document.getElementById('start-scan-btn');
const stopBtn       = document.getElementById('stop-scan-btn');

// If the camera section wasn't rendered by Thymeleaf, do nothing
if (!startBtn || !stopBtn || !videoElement) {
  // Camera section is hidden for this user — nothing to initialize
} else {

  let codeReader = null;
  let controls   = null;
  let usingNativeDetector = false;
  let detector   = null;
  let stream     = null;
  let rafId      = null;
  let isScanning = false;   // prevent multiple simultaneous scans

  /* ── Native BarcodeDetector path ── */
  async function startNativeDetector() {
    try {
      stream = await navigator.mediaDevices.getUserMedia({ video: { facingMode: 'environment' } });
      videoElement.srcObject = stream;
      videoElement.play();
      detector = new BarcodeDetector({ formats: ['qr_code', 'code_128', 'ean_13'] });
      usingNativeDetector = true;
      scanResultErr.textContent = 'Scanning...';
      scanLoop();
    } catch (err) {
      scanResultErr.textContent = 'Camera access denied: ' + err.message;
    }
  }

  async function scanLoop() {
    if (!detector || !videoElement || isScanning) return;
    try {
      const barcodes = await detector.detect(videoElement);
      if (barcodes.length > 0) {
        const code = barcodes[0].rawValue;
        if (scanResult) scanResult.value = code;
        await handleScannedCode(code);
        return; // stop loop after successful scan
      }
    } catch (err) {
      console.error('Detection error:', err);
    }
    rafId = requestAnimationFrame(scanLoop);
  }

  /* ── Stop helpers ── */
  function stopNative() {
    if (stream) stream.getTracks().forEach(t => t.stop());
    if (rafId)  cancelAnimationFrame(rafId);
    stream = null; rafId = null;
  }
  function stopZxing() {
    if (controls) { controls.stop(); controls = null; }
  }

  /* ── Start button ── */
  startBtn.addEventListener('click', async () => {
    scanResultErr.textContent = 'Initializing camera...';
    isScanning = false;

    if ('BarcodeDetector' in window) {
      startNativeDetector();
    } else {
      codeReader = new BrowserMultiFormatReader();
      try {
        controls = await codeReader.decodeFromVideoDevice(null, videoElement, async (result, err) => {
          if (result && !isScanning) {
            const code = result.getText();
            if (scanResult) scanResult.value = code;
            await handleScannedCode(code);
          }
        });
      } catch (e) {
        scanResultErr.textContent = 'Camera error: ' + e.message;
      }
    }
  });

  /* ── Stop button ── */
  stopBtn.addEventListener('click', () => {
    scanResultErr.textContent = 'Camera stopped.';
    usingNativeDetector ? stopNative() : stopZxing();
  });

  /* ── After a successful scan ── */
  async function handleScannedCode(code) {
    if (isScanning) return;
    isScanning = true;
    try {
      // Stop camera immediately so we don't double-scan
      usingNativeDetector ? stopNative() : stopZxing();

      await scannedPresent(code);

      await new Promise(r => setTimeout(r, 300));
    } catch (err) {
      console.error('Error after scanning:', err);
      if (scanResultErr) scanResultErr.textContent = 'Error while marking attendance: ' + err.message;
    } finally {
      isScanning = false;
    }
  }

  /* ── API call ── */
  async function scannedPresent(code) {
    try {
      const baseURL = getBaseUrl();

      const responseForUser = await fetch(baseURL + `/api/profile/search?phone=${code}`);
      if (!responseForUser.ok) {
        throw new Error('Network response was not ok: ' + responseForUser.status);
      }
      const userDataArray = await responseForUser.json();

      if (!userDataArray || userDataArray.length === 0) {
        showToast('خطأ', 'لم يتم العثور على المستخدم', 'error');
        return;
      }

      const userData  = userDataArray[0];
      const userId    = userData.userId;
      const meetingCode = document.getElementById('meetingCode').value;

      if (!meetingCode) {
        showToast('خطأ', 'لابد من اختيار كود الاجتماع', 'error');
        return;
      }

      const response = await fetch(baseURL + `/api/scanner/${meetingCode}/${userId}`);
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error('Error: ' + errorData.message);
      }

      const user = await response.json();
      showToast(
        'عملية ناجحة',
        `أحسنت ${user.firstName}! لقد تم تسجيل حضورك وكسبت ${user.points} نقاط Wazna. استمر على هذا الأداء الرائع!`,
        'success'
      );
      updateAttendanceTable(user);

    } catch (error) {
      showToast('Error', error.message, 'error');
    }
  }

} // end if(startBtn)