// assets/js/board-read.js

document.addEventListener("DOMContentLoaded", function () {
  const passwordModalElem = document.getElementById("passwordModal");
  const passwordModal = passwordModalElem
    ? new bootstrap.Modal(passwordModalElem)
    : null;
  const guestPwInput = document.getElementById("guestPassword");
  const confirmBtn = document.getElementById("btnConfirmPw");

  let currentBno = "";
  let currentBoardPw = "";
  let actionType = "";

  document
    .getElementById("actionBtnArea")
    ?.addEventListener("click", function (e) {
      const btn = e.target.closest("button");
      if (!btn) return;

      currentBno = btn.getAttribute("data-bno");
      currentBoardPw = btn.getAttribute("data-pw");
      const isGuest = btn.getAttribute("data-guest") === "true";
      const isAdmin = btn.getAttribute("data-role") === "ADMIN"; // 🌟 관리자 여부 확인

      // 🌟 수정 로직 분기
      if (btn.classList.contains("btn-modify")) {
        // 관리자거나 로그인을 한 본인인 경우 (isGuest가 아님) 바로 이동
        if (isAdmin || !isGuest) {
          location.href = `/board/modify?bno=${currentBno}`;
        } else {
          // Guest인 경우에만 모달 띄움
          actionType = "modify";
          passwordModal.show();
        }
      }

      // 🌟 삭제 로직 분기
      else if (btn.classList.contains("btn-remove")) {
        if (isAdmin || !isGuest) {
          if (confirm("정말로 삭제하시겠습니까?")) {
            document.getElementById("removeForm").submit();
          }
        } else {
          // Guest인 경우에만 모달 띄움
          actionType = "remove";
          passwordModal.show();
        }
      }
    });

  // 모달 확인 버튼 로직은 동일
  confirmBtn?.addEventListener("click", function () {
    const inputPw = guestPwInput.value;
    if (inputPw === currentBoardPw) {
      if (actionType === "modify") {
        location.href = `/board/modify?bno=${currentBno}&password=${inputPw}`;
      } else if (actionType === "remove") {
        document.getElementById("removePassword").value = inputPw;
        document.getElementById("removeForm").submit();
      }
      passwordModal.hide();
    } else {
      alert("비밀번호가 일치하지 않습니다.");
    }
  });

  passwordModalElem?.addEventListener("hidden.bs.modal", function () {
    guestPwInput.value = "";
  });
});
