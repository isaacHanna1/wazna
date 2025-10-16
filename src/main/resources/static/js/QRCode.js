function generateQR(QRcode,rowNum) {
    const url = getBaseUrl(); 
    const QRLink = `${url}/api/scanner/${QRcode}`;
    const qrUrl = `https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=${encodeURIComponent(QRLink)}`;

    // Create a hidden link element to trigger download
    const link = document.getElementById("QR_"+rowNum);
    link.href = qrUrl;
    link.download = `QR_${QRcode}.png`; // the file name for the downloaded image
    link.target = "_blank"; // optional: open image before download

    // Trigger the download
    link.click();
}