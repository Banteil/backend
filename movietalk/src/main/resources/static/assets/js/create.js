/**
 * 영화 등록 전용 (create.js)
 */
$(document).ready(function () {
  const createForm = $("#createForm");

  createForm.on("submit", function (e) {
    e.preventDefault(); // 일단 멈춤

    // 제목 유효성 검사 예시
    if ($("input[name='title']").val().trim() === "") {
      alert("영화 제목을 입력하세요.");
      return;
    }

    let str = "";
    // upload.js가 만든 li들을 순회하며 hidden input 생성
    $("#commonUploadResult ul li").each(function (idx, obj) {
      const target = $(obj);
      str += `
                <input type="hidden" name="mImages[${idx}].imgName" value="${target.data(
        "name"
      )}">
                <input type="hidden" name="mImages[${idx}].uuid" value="${target.data(
        "uuid"
      )}">
                <input type="hidden" name="mImages[${idx}].path" value="${target.data(
        "path"
      )}">
                <input type="hidden" name="mImages[${idx}].ord" value="${idx}">
            `;
    });

    $(this).append(str); // 생성된 hidden 태그들을 폼 안에 추가
    this.submit(); // 실제 서버로 전송
  });
});
