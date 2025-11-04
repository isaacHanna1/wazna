document.addEventListener("DOMContentLoaded", async function () {
  const evalSelect = document.getElementById("evaluationType");
  const parentSelect = document.getElementById("bonceParent");
  const pointInput = document.getElementById("point");
  const form = document.querySelector("form");

  // Injected by Thymeleaf if editing
  const selectedBonusHeadId = /*[[${selectedBonusHeadId}]]*/ null;

  // -------------------------------
  // 1️⃣ When Evaluation Type Changes
  // -------------------------------
  evalSelect.addEventListener("change", async function () {
    const type = this.value;
    if (!type) return;

    const baseUrl = getBaseUrl();
    try {
      const response = await fetch(`${baseUrl}/api/evaluation?evaluationType=${encodeURIComponent(type)}`);
      if (!response.ok) throw new Error("Failed to load bonus parents");

      const data = await response.json();
      loadBonusParentOptions(data, selectedBonusHeadId);
    } catch (error) {
      console.error(error);
    }

    // Automatically adjust point sign based on type
    adjustPointsByType(type);
  });

  // -------------------------------
  // 2️⃣ Adjust point value live when typing
  // -------------------------------
  pointInput.addEventListener("input", function () {
    const type = evalSelect.value;
    adjustPointsByType(type);
  });

  // -------------------------------
  // 3️⃣ Prevent form submission if invalid
  // -------------------------------
  form.addEventListener("submit", function (e) {
    const type = evalSelect.value;
    const point = Number(pointInput.value);

    if (type === "NE" && point >= 0) {
      e.preventDefault();
      showToast("Error", "For Negative Evaluation, the point must be a negative value.", "error");
      pointInput.focus();
      return;
    }
    if (type === "PO" && point <= 0) {
      e.preventDefault();
      showToast("Error", "For Positive Evaluation, the point must be a positive value.", "error");
      pointInput.focus();
      return;
    }
  });

  // -------------------------------
  // 4️⃣ Helper functions
  // -------------------------------
  function loadBonusParentOptions(data, selectedId) {
    parentSelect.innerHTML = '<option value="" disabled>Select</option>';
    data.forEach(bonusHead => {
      const option = document.createElement("option");
      option.value = bonusHead.id;
      option.textContent = bonusHead.name;
      if (selectedId && selectedId === bonusHead.id) {
        option.selected = true;
      }
      parentSelect.appendChild(option);
    });
  }

  function adjustPointsByType(type) {
    const val = Number(pointInput.value);
    if (!val) return;

    if (type === "NE" && val > 0) {
      pointInput.value = -Math.abs(val); // make sure it's negative
    } else if (type === "PO" && val < 0) {
      pointInput.value = Math.abs(val); // make sure it's positive
    }
  }

  // -------------------------------
  // 5️⃣ Auto-load if editing
  // -------------------------------
  if (evalSelect.value) {
    evalSelect.dispatchEvent(new Event("change"));
  }
});
