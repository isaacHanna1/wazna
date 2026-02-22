async function openModal(button) {

    const marketItemId        = button.getAttribute("data-id");
    const name                = button.getAttribute("data-name");
    const points              = button.getAttribute("data-points");
    const image               = button.getAttribute("data-image");

    document.getElementById("modalProductId").value = marketItemId;
    document.getElementById("modalName").innerText = name;
    document.getElementById("modalPoints").innerText = points;
    document.getElementById("modalImage").src = image;

    document.getElementById("buyModal").style.display = "block";
}

function closeModal() {
    document.getElementById("buyModal").style.display = "none";
}



async function addToCart(btn) {
  

    // 1. grab the button and disable it
    const originalText = btn.innerText;
    btn.disabled = true;
    btn.innerText = "⏳ جاري...";

    const marketItemId = document.getElementById("modalProductId").value;
    const cartItem = {
        "itemId": marketItemId,
        "itemCount": "1"
    };

    const baseURL = getBaseUrl();
    try {
        const response = await fetch(`${baseURL}/api/v1/cart`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(cartItem)
        });

        if (response.ok) {
            const result = await response.json();
            Toast.success('عملية ناجحة', 'تم الشراء بنجاح');
            console.log(result);
            updateCartAndPoints(result.points); 
        } else {
            const error = await response.text();
            Toast.error('عملية خاطئة', 'لا توجد وزنات كافية لاتمام عملية الشراء');
        }
    } catch (err) {
        console.error("Fetch error:", err);
        Toast.error('عملية خاطئة', 'حدث خطأ اثناء عملية الشراء');
    } finally {
        btn.disabled = false;
        btn.innerText = originalText;
        closeModal();
    }
}


// Call this after successful purchase
function updateCartAndPoints(itemPoints) {
    // 1. increase cart count
    const cartCount = document.querySelector(".cart-count");
    cartCount.innerText = parseInt(cartCount.innerText) + 1;

    // 2. minus points
    const pointsEl = document.querySelector(".market-info-bar span [th\\:text], .market-info-bar span span");
    const currentPoints = parseInt(pointsEl.innerText);
    pointsEl.innerText = currentPoints - itemPoints;
}