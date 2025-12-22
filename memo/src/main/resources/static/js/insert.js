// modify.js 파일 내용

// REST API 엔드포인트: @PutMapping("/rest/modify")
const url = "/rest/modify";

// HTML의 <form id="memoForm">을 선택
const form = document.querySelector("#memoForm");

// ID와 TEXT 입력 필드를 명시적으로 선택
const memoIdInput = document.querySelector("#memoId");
const memoTextInput = document.querySelector("#memoText");
const validationError = document.getElementById("validationError");
const messageDisplay = document.getElementById("messageDisplay");

// 폼 제출 이벤트 리스너
form.addEventListener("submit", (e) => {
  e.preventDefault(); // 기본 폼 제출 방지

  // 이전 오류 및 메시지 초기화
  validationError.innerHTML = "";
  messageDisplay.innerHTML = "";

  // ID와 TEXT 값 가져오기
  const id = memoIdInput.value;
  const text = memoTextInput.value.trim();

  if (!text) {
    validationError.innerHTML = "메모 내용은 비워둘 수 없습니다.";
    return;
  }

  // 전송할 데이터 객체 (ID와 TEXT 모두 필요)
  const memoData = {
    id: id,
    text: text,
  };

  // 버튼 비활성화 (중복 제출 방지)
  const submitBtn = form.querySelector('button[type="submit"]');
  submitBtn.disabled = true;

  // Fetch API를 사용한 PUT 요청
  fetch(url, {
    method: "PUT", // 🌟 HTTP 메서드를 PUT으로 변경
    headers: {
      "Content-Type": "application/json",
    },
    // 🌟 ID와 TEXT를 JSON 본문에 포함
    body: JSON.stringify(memoData),
  })
    .then((res) => {
      // HTTP 상태 코드 확인
      if (!res.ok) {
        // 유효성 검사 오류(400) 또는 기타 서버 오류 처리
        return res.json().then((err) => {
          // 오류 상세 정보를 반환한다고 가정
          throw new Error(
            err.message || `서버 오류 발생! 상태 코드: ${res.status}`
          );
        });
      }

      // 서버에서 수정된 MemoDTO (JSON 객체)를 반환할 것으로 예상
      return res.json();
    })
    .then((updatedDto) => {
      console.log("메모 수정 완료:", updatedDto);

      if (updatedDto && updatedDto.id) {
        Swal.fire({
          title: "메모 수정 완료",
          text: `ID ${updatedDto.id}번 메모가 수정되었습니다.`,
          icon: "success",
          draggable: true,
          timer: 1500, // 1.5초 후 자동 닫힘
          showConfirmButton: false,
        }).then(() => {
          // 수정 완료 후 상세 페이지로 리다이렉트
          window.location.href = `/rmemo/read?id=${updatedDto.id}`;
        });
      }
    })
    .catch((err) => {
      console.error("수정 오류:", err);
      messageDisplay.innerHTML = `<div class="alert alert-danger">❌ 수정 중 오류가 발생했습니다: ${err.message}</div>`;
    })
    .finally(() => {
      // 버튼 다시 활성화
      submitBtn.disabled = false;
    });
});
