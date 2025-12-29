// 등록 클릭 시(form submit)
document.querySelector("#createForm").addEventListener("submit", (e) => {
  e.preventDefault(); // 일단 멈춤

  const attachInfos = document.querySelectorAll(".uploadResult li");
  let result = "";

  attachInfos.forEach((obj, idx) => {
    // MovieImageDTO의 필드들: imgName, uuid, path
    result += `
      <input type="hidden" name="mImages[${idx}].imgName" value="${obj.dataset.name}">
      <input type="hidden" name="mImages[${idx}].uuid" value="${obj.dataset.uuid}">
      <input type="hidden" name="mImages[${idx}].path" value="${obj.dataset.path}">
      <input type="hidden" name="mImages[${idx}].ord" value="${idx}">
    `;
  });

  // 생성된 hidden 태그들을 폼 안에 추가
  e.target.insertAdjacentHTML("beforeend", result);

  // 실제 서버로 전송
  e.target.submit();
});
