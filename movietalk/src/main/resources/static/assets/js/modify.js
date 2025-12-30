/**
 * 영화 수정 전용 (modify.js)
 */
$(document).ready(function () {
  const modifyForm = $("#modifyForm");

  // [1] 수정 완료 버튼 클릭
  $(".btn-modify").click(function (e) {
    e.preventDefault();

    if (!confirm("수정하시겠습니까?")) return;

    let str = "";
    // upload.js가 관리하는 모든 li(기존+신규)를 순회하며 hidden 생성
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

    modifyForm.append(str).submit();
  });

  // [2] 삭제 버튼 클릭 (영화 자체 삭제)
  $(".btn-remove").click(function () {
    if (confirm("정말로 영화를 삭제하시겠습니까?")) {
      const form = $("#removeForm");
      console.log("Form found:", form.length); // 3. 폼 요소를 찾았는지 확인
      form.submit();
    }
  });
});
