
// Open cancel modal and fill data
function openCancelModal(cartId, itemName, imageUrl, points) {
    document.getElementById('cancelModalCartId').value = cartId;
    document.getElementById('cancelModalName').textContent  = itemName;
    document.getElementById('cancelModalPoints').textContent = points;
    document.getElementById('cancelModalImage').src = '/market/' + imageUrl;

    document.getElementById('cancelModal').style.display = 'flex';
}

// Close cancel modal
function closeCancelModal() {
    document.getElementById('cancelModal').style.display = 'none';
}

// Send cancel request to backend
function confirmCancel(btn) {

    const originalText = btn.innerText;
    btn.disabled = true;
    btn.innerText = "⏳ Loading...";

    const baseURL = getBaseUrl();
    const cartId = document.getElementById('cancelModalCartId').value;
    fetch(`${baseURL}/api/v1/cart/cancel/${cartId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
    })
    .then(res => {
        if (res.ok) {
            closeCancelModal();
            location.reload();
        } else {
            res.text().then(msg => Toast.error('عملية خاطئة', ' حاول مرة أخري , حدث خطأ اثناء العملية '));
        }
    })
    .catch(err => {
        console.error('Cancel error:', err);
        Toast.error('عملية خاطئة', ' حاول مرة أخري , حدث خطأ اثناء العملية ');
    });
}

window.addEventListener('click', function (e) {
    const modal = document.getElementById('cancelModal');
    if (e.target === modal) closeCancelModal();
});