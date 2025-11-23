let offset      = 0;
const limit     = 20;
let loading     = false;

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
                    <div class="class">${yt.point}  Wazna </div>
                </div>
            </div>
        `;

        container.appendChild(div);
    });
}

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
    if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100) {
        loadYouth();
    }
});
