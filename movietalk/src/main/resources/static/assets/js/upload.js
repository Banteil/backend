const fileInput = document.querySelector("[name='file']");
const uploadResultUl = document.querySelector(".uploadResult ul"); // 자주 쓰이므로 변수화

const showUploadImages = (files) => {
  let tags = "";
  files.forEach((file) => {
    // data-name 속성에 삭제할 파일 경로를 미리 심어둡니다.
    tags += `
    <li data-name="${file.imgName}" data-path="${file.path}" data-uuid="${file.uuid}" class="flex items-center space-x-4 mb-2 p-2 border rounded">
        <a href="/upload/display?fileName=${file.imageURL}" target="_blank"> 
            <img src="/upload/display?fileName=${file.thumbnailURL}" class="w-20 h-20 object-cover"> 
        </a>
        <span class="text-sm flex-1 text-gray-600">${file.imgName}</span>
        <a href="${file.imageURL}" class="remove-btn text-red-500 hover:text-red-700">
            <i class="fa-solid fa-xmark"></i>
        </a>
    </li>`;
  });
  uploadResultUl.insertAdjacentHTML("beforeend", tags);
};

// 파일 선택 시 자동 업로드
fileInput.addEventListener("change", (e) => {
  const files = e.target.files;
  if (files.length === 0) return;

  const formData = new FormData();
  for (let idx = 0; idx < files.length; idx++) {
    formData.append("uploadFiles", files[idx]);
  }

  fetch("/upload/upload", {
    method: "post",
    body: formData,
  })
    .then((res) => res.json())
    .then((data) => {
      showUploadImages(data);
      fileInput.value = "";
    })
    .catch((err) => console.error("Upload Error:", err));
});

// [추가] 삭제 이벤트 (이벤트 위임)
uploadResultUl.addEventListener("click", (e) => {
  const removeBtn = e.target.closest(".remove-btn");
  if (!removeBtn) return;

  e.preventDefault();
  const fileName = removeBtn.getAttribute("href");
  const targetLi = removeBtn.closest("li");

  if (confirm("삭제하시겠습니까?")) {
    const formData = new FormData();
    formData.append("fileName", fileName);

    fetch("/upload/remove", {
      method: "post",
      body: formData,
    })
      .then((res) => {
        if (res.ok) targetLi.remove();
      })
      .catch((err) => console.error(err));
  }
});
