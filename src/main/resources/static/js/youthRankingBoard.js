let offset = 0;
const limit = 20;
let loading = false;
let searchMode = false;



async function loadYouth() {
    if (loading) return;
    loading = true;

    const response = await fetch(`/api/youth/rank/all?offset=${offset}&limt=${limit}`);
    const data = await response.json();

    if (data.length === 0) return;

    appendYouth(data);

    offset += limit;
    loading = false;

    ensureScrollable();
}

function appendYouth(list) {
    const container = document.getElementById("youth-container");

    list.forEach(yt => {
        const div = document.createElement("div");
        div.classList.add("youth-item");

        div.innerHTML = `
            <div class="rank-badge ${getRankClass(yt.rank)}">#${yt.rank}</div>

            <div class="youth-circle">
                <img src="${yt.photoPath}" alt="${yt.firstName} ${yt.lastName}">
                <div class="info-overlay">
                    <div class="name">${yt.firstName} ${yt.lastName}</div>
                    <div class="class">${yt.classService}</div>
                    <div class="class">${yt.point} Wazna</div>
                </div>
            </div>
        `;

        // Click on image to fetch transaction data
        div.querySelector(".youth-circle img").addEventListener("dblclick", async () => {
            const response = await fetch(`/api/youth/transaction/details/${yt.profileId}`);
            const data = await response.json();

            // Build HTML from transaction JSON
            let html = '';
            data.forEach(tx => {
                html += `
                     <p> ${formatDate(tx.transactionTime)}</p>
                    <p><strong>الوصف :</strong> ${tx.usedFor}</p>
                    <p><strong>عدد الوزنات :</strong> ${tx.points}</p>
                    <hr>
                `;
            });

            openPopup(html);
        });

        container.appendChild(div);
    });
}
function formatDate(dateString) {
    const [year, month, day] = dateString.split("T")[0].split("-");
    const date = new Date(year, month - 1, day);

    const weekday = date.toLocaleDateString("en-US", { weekday: "long" });
    const formattedDate = `${day}/${month}/${year}`;

    return `${weekday} — ${formattedDate}`;
}

// Popup JS
const popup = document.getElementById("transactionPopup");
const popupBody = document.getElementById("popup-body");
const popupClose = document.querySelector(".popup-close");

function openPopup(content) {
    popupBody.innerHTML = content;
    popup.style.display = "block";
}

popupClose.onclick = () => popup.style.display = "none";

window.onclick = (event) => {
    if (event.target === popup) popup.style.display = "none";
};



function getRankClass(rank) {
    if (rank === 1) return "rank-gold";
    if (rank === 2) return "rank-silver";
    if (rank === 3) return "rank-bronze";
    if (rank <= 10) return "rank-top10";
    if (rank <= 50) return "rank-top50";
    return "rank-other";
}

function ensureScrollable() {
    if (document.body.scrollHeight <= window.innerHeight) {
        loadYouth();
    }
}

// initial load
loadYouth();

// scroll listener
window.addEventListener("scroll", () => {
    if (searchMode) return;
    if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100) {
        loadYouth();
    }
});


/*Start the search */
const searchInput = document.getElementById("search");
searchInput.addEventListener("input", debounce(handleSearch, 500));
async function handleSearch() {
    const query = searchInput.value.trim();
    if (query !== "") {
        searchMode = true;
        const response = await fetch(`/api/youth/rank/find?userName=${encodeURIComponent(query)}`);
        const list = await response.json();
        const container = document.getElementById("youth-container");
        container.innerHTML = "";
        appendYouth(list);
        return;
    }
    searchMode = false;
    offset = 0;
    const container = document.getElementById("youth-container");
    container.innerHTML = "";
    loadYouth();
}

function debounce(func, delayTime) {
    let timeoutId;
    return function (...args) {
        clearTimeout(timeoutId);
        // set timout retun setTimeOut ID => 
        timeoutId = setTimeout(() => func.apply(this, args), delayTime);
    }
}
/*End the search */

/* Start Clear btn */
const clearBtn = document.getElementById("clearSearch");
clearBtn.addEventListener("click", () => {
    searchInput.value = "";
    handleSearch();
});

/*ُEnd Clear Btn */