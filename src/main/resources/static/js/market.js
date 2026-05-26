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



async function addToCart(){

    const  marketItemId = document.getElementById("modalProductId").value ;
    const cartItem = {
            itemId: marketItemId,
            itemCount: 1
        };
        const baseURL = getBaseUrl(); 
        try {
        const response = await fetch(`${baseURL}/api/v1/cart`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(cartItem)
        });

        if (response.ok) {
            const result = await response.json();
            console.log("Added to cart successfully!");
            closeModal();
        } else {
            const error = await response.text();
            console.log("Error adding to cart: " + error);
        }
    } catch (err) {
        console.error("Fetch error:", err);
         console.log("Network error");
    }
}
