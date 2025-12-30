/**
 * 범용 업로드 및 섬네일 출력 (upload.js)
 */
$(document).ready(function () {
  const fileInput = document.querySelector("#commonFileInput");
  const uploadResultUl = document.querySelector("#commonUploadResult ul");

  if (!fileInput || !uploadResultUl) return;

  // [1] 섬네일 출력 함수 (하단 이름 + 우측 x 버튼 구조)
  const showUploadImages = (files) => {
    let tags = "";
    files.forEach((file) => {
      tags += `
<li data-name="${file.imgName}" data-path="${file.path}" data-uuid="${file.uuid}">
    <div class="img-container">
        <img src="/upload/display?fileName=${file.thumbnailURL}">
    </div>
    <div class="info-container">
        <span class="file-name" title="${file.imgName}">${file.imgName}</span>
        <button type="button" data-file="${file.imageURL}" class="common-remove-btn">
            <svg style="width:14px; height:14px;" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
            </svg>
        </button>
    </div>
</li>`;
    });
    uploadResultUl.insertAdjacentHTML("beforeend", tags);
  };

  // [2] 파일 선택 시 자동 업로드 (기존 로직 동일)
  fileInput.addEventListener("change", (e) => {
    const files = e.target.files;
    if (files.length === 0) return;

    const formData = new FormData();
    for (let idx = 0; idx < files.length; idx++) {
      formData.append("uploadFiles", files[idx]);
    }

    fetch("/upload/upload", { method: "post", body: formData })
      .then((res) => res.json())
      .then((data) => {
        showUploadImages(data);
        fileInput.value = "";
      })
      .catch((err) => console.error("Upload Error:", err));
  });

  // [3] 삭제 버튼 클릭 (이벤트 위임)
  $(uploadResultUl).on("click", ".common-remove-btn", function (e) {
    e.preventDefault();
    const targetLi = $(this).closest("li");
    const fileName = $(this).data("file");

    if (confirm("이 포스터를 삭제하시겠습니까?")) {
      const formData = new FormData();
      formData.append("fileName", fileName);

      fetch("/upload/remove", { method: "post", body: formData })
        .then((res) => {
          if (res.ok) targetLi.remove();
        })
        .catch((err) => console.error(err));
    }
  });
});
