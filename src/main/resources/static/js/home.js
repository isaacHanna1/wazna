

// Start Events
/* Start On Load Page Event */
document.addEventListener('DOMContentLoaded',getTopRankedYouth());
/* End On Load Page Event */

// End  Events
async function getTopRankedYouth() {

    try{
      const baseURL = getBaseUrl();
    const response  = await fetch(`${baseURL}`+"/api/youth/rank/top?limit=10&offset=0", {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      },
    });
   
    if(!response.ok){
        console.log("Error Happened Get Leader Board");
        const errorDate = await response.data.json().carch(()=>null);
        throw new Error (`Faild To Load Leader Board ${response.status} :${errorDate?.message || `Un Known Error`}`);
        
    }
        const data = await response.json();
        console.log(data);
        constructLeaderBoard(data);
    }catch (error ){
    console.error('Failed to fetch leaderboard:', error);
    }
}

function constructLeaderBoard(data) {
  const leaderboard = document.querySelector(".container .top");
  leaderboard.innerHTML = data.map((item) => `
    <div class="leaderboard-item ${item.rank < 3 ? 'top-3' : ''}">
      <div class="leaderboard-rank">
        <span class="${item.rank < 3 ? 'rank-number top-3' : 'rank-number'}">${item.rank}</span>
        <span style="font-weight: 500">${item.firstName} ${item.lastName}</span>
      </div>
      <span class="leaderboard-points">${item.point}</span>
    </div>
  `).join('');
}

