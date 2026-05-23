async function openModal(button) {
    const marketItemId = button.getAttribute("data-id");
    const name         = button.getAttribute("data-name");
    const points       = button.getAttribute("data-points");
    const image        = button.getAttribute("data-image");

    document.getElementById("modalProductId").value  = marketItemId;
    document.getElementById("modalName").innerText   = name;
    document.getElementById("modalPoints").innerText = points;
    document.getElementById("modalImage").src        = image;

    document.getElementById("buyModal").style.display = "flex";
}

function closeModal() {
    document.getElementById("buyModal").style.display = "none";
}

async function addToCart(btn) {
    const originalText = btn.innerText;
    btn.disabled       = true;
    btn.innerText      = "⏳ Loading...";

    const marketItemId = document.getElementById("modalProductId").value;
    const cartItem     = { itemId: marketItemId, itemCount: "1" };
    const baseURL      = getBaseUrl();

    try {
        const response = await fetch(`${baseURL}/api/v1/cart`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(cartItem)
        });

        const data = await response.json();

        if (response.ok) {
            Toast.success('عملية ناجحة', 'تم الشراء بنجاح');
            updateCartAndPoints(data.points);
            setTimeout(() => location.reload(), 1500);
            return;
        }
        switch (data.error) {
            case 'STOCK_001':
                Toast.error("نفاذ المخزون", "هذا المنتج نفد من المخزون");
                break;
            default:
                Toast.error("خطأ", data.message || "حدث خطأ غير متوقع");
        }

    } catch (err) {
        console.error("Fetch error:", err);
        Toast.error('عملية خاطئة', 'حدث خطأ اثناء عملية الشراء');
    } finally {
        btn.disabled  = false;
        btn.innerText = originalText;
        closeModal();
    }
}

function updateCartAndPoints(itemPoints) {
    const cartCount     = document.querySelector(".cart-count");
    cartCount.innerText = parseInt(cartCount.innerText) + 1;

    const pointsEl      = document.querySelector(".market-info-bar span span");
    const currentPoints = parseInt(pointsEl.innerText);
    pointsEl.innerText  = currentPoints - itemPoints;
}