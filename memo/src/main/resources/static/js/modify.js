// modify.js 파일 내용

// REST API 엔드포인트
const url = "/rest/modify";

// ... (선택자 변수 선언은 이전과 동일)
const form = document.querySelector("#memoForm");
const memoIdInput = document.querySelector("#memoId");
const memoTextInput = document.querySelector("#memoText");
const validationError = document.getElementById("validationError");
const messageDisplay = document.getElementById("messageDisplay");
// ...

form.addEventListener("submit", (e) => {
  e.preventDefault();

  // ... (유효성 검사 및 데이터 준비 로직 생략)
  const id = memoIdInput.value;
  const text = memoTextInput.value.trim();
  const memoData = { id: id, text: text };

  const submitBtn = form.querySelector('button[type="submit"]');
  submitBtn.disabled = true;

  // Fetch API를 사용한 PUT 요청
  fetch(url, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(memoData),
  })
    .then((res) => {
      if (!res.ok) {
        // 서버에서 에러 발생 시 JSON 또는 텍스트로 에러 메시지를 반환한다고 가정하고 처리
        return res.text().then((errText) => {
          throw new Error(
            errText || `서버 오류 발생! 상태 코드: ${res.status}`
          );
        });
      }

      // 🌟🌟🌟 수정된 부분: 서버에서 Long(ID)를 반환하므로 res.json() 대신 res.text() 사용 🌟🌟🌟
      return res.text();
    })
    .then((data) => {
      // data는 문자열 형태의 ID ("10"과 같은 형태)
      const updatedId = Number(data);
      console.log("메모 수정 완료. 반환된 ID:", updatedId);

      if (updatedId > 0) {
        // SweetAlert2 표시
        Swal.fire({
          title: "메모 수정 완료",
          text: `ID ${updatedId}번 메모가 성공적으로 수정되었습니다.`,
          icon: "success",
          draggable: true,
          timer: 1500,
          showConfirmButton: false,
        }).then(() => {
          // 수정 완료 후 상세 페이지로 리다이렉트 (반환된 ID 사용)
          window.location.href = `/rmemo/read?id=${updatedId}`;
        });
      } else {
        // ID가 반환되었지만 0 이하인 경우 (수정 실패로 간주 가능)
        messageDisplay.innerHTML = `<div class="alert alert-warning">⚠️ 수정은 요청되었으나 반환된 ID가 유효하지 않습니다.</div>`;
      }
    })
    .catch((err) => {
      console.error("수정 오류:", err);
      messageDisplay.innerHTML = `<div class="alert alert-danger">❌ 수정 중 오류가 발생했습니다: ${err.message}</div>`;
    })
    .finally(() => {
      submitBtn.disabled = false;
    });
});
