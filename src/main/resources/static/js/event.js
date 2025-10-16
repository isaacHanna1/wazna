
// Compress Image before Upload 
document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("event-form");
  const imageInput = document.getElementById("image");

  console.log("called")
    form.addEventListener("submit", async (e) => {
      const file = imageInput.files[0];
      if (file) {
        e.preventDefault(); // temporarily stop
        const compressed = await compressImage(file, 800, 0.7);
        const compressedFile = new File([compressed], file.name, { type: "image/jpeg" });

        // Replace the original image in the input field
        const dataTransfer = new DataTransfer();
        dataTransfer.items.add(compressedFile);
        imageInput.files = dataTransfer.files;
        form.submit(); 
        console.log("submitted")

      }
    });
});