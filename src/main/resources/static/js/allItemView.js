const hiddenAutoCompleteList = document.getElementById("hiddenAutoCompleteList");
const itemId = document.getElementById("itemId");
document.getElementById('itemName').addEventListener('input', debounce(async function(e) {
    const keyword = e.target.value.trim();
    if (keyword.length > 0) {
        const data = await search(keyword);
        hiddenAutoCompleteList.classList.remove("dis_none");            
        hiddenAutoCompleteList.classList.add("hiddenList");
        data.forEach(item => {
            const listItem         = document.createElement('li');
            listItem.textContent   = `${item.itemName}`;
            listItem.style.padding = '5px';
            listItem.style.cursor  = 'pointer';
            hiddenAutoCompleteList.append(listItem); 
            listItem.addEventListener("click",()=>{
                document.getElementById('itemName').value =`${item.itemName}`;
                itemId.value                              = `${item.id}`;
                hiddenAutoCompleteList.innerHTML ="";
                hiddenAutoCompleteList.classList.add("dis_none");
            });
        });
    }else{
        hiddenAutoCompleteList.innerHTML    = "" ;
        hiddenAutoCompleteList.classList.add("dis_none");
    } 
}, 300));

async function search(keyword){

        const URL               =  getBaseUrl();
        const fullURL           = `${URL}/market/item/all/find?keyword=${keyword}`;
        try {
            const response      = await fetch(fullURL, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
            });
            if (!response.ok) {
                    showToast("Error", "Internal Error Happening", "error");
            }else{
                const data        =  await response.json();
                return data;

            }
        } catch (error) {
            console.error('Error:', error);
            throw error;
        }

}
document.addEventListener('click', function(e) {
    if(e.target != hiddenAutoCompleteList ){
        hiddenAutoCompleteList.classList.add("dis_none");
    }
});

function changeStatus(obj){
    obj.value = obj.checked ? "true" : "false";

}
async function exceute(btn){
      let obj       = [];
      let createObj = null;
      let id        = 0;
      let status    = false;
      const tr      = document.querySelectorAll(".table tr"); 
       btn.innerHTML = `<div class="loading-spinner"></div>Loading...`
      tr.forEach((tr)=>{
      const inputs = tr.querySelectorAll("input");
      inputs.forEach(input => {
        if(input.name.includes("id")){
          id = input.value;
         }else if(input.name.includes("status")){
          status = input.value;
         }
    });
        createObj = {id:`${id}`,status:`${status}`};
        obj.push(createObj);
});
    const jsonString = JSON.stringify(obj);
    console.log(jsonString);
    const URL               =  getBaseUrl();
    const fullURL           = `${URL}/market/item/update`;

          try {
            const response      = await fetch(fullURL, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body:jsonString
            });
            if (!response.ok) {
                showToast("Error", "Internal Error Happening", "error");
            }else{
                showToast( "Update Status",`Updated Done Sucessfully`, "success", );
            }
        } catch (error) {
            console.error('Error:', error);
            throw error;
        }
        finally{
                   btn.innerHTML = ``;
                   btn.textContent ="Exceute";
        }

}

function viewImage(URL){
    
}