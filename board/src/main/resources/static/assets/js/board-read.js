document.addEventListener("DOMContentLoaded", function () {
  document
    .getElementById("actionBtnArea")
    ?.addEventListener("click", function (e) {
      const btn = e.target.closest("button");
      if (!btn) return;

      const currentBno = btn.getAttribute("data-bno");
      const isGuest = btn.getAttribute("data-guest") === "true";
      const isAdmin = btn.getAttribute("data-role") === "ADMIN";

      // 🌟 수정 로직
      if (btn.classList.contains("btn-modify")) {
        if (isAdmin || !isGuest) {
          location.href = `/board/modify?bno=${currentBno}`;
        } else {
          // 확장된 authUtils 호출 방식
          authUtils.verifyPassword({
            url: "/board/check-password",
            idValue: currentBno,
            idKey: "bno",
            successCallback: (validPw) => {
              location.href = `/board/modify?bno=${currentBno}&password=${validPw}`;
            },
          });
        }
      }

      // 🌟 삭제 로직
      else if (btn.classList.contains("btn-remove")) {
        const performRemove = (inputPw) => {
          if (confirm("정말로 삭제하시겠습니까?")) {
            const removeForm = document.getElementById("removeForm");
            if (inputPw)
              document.getElementById("removePassword").value = inputPw;
            removeForm.submit();
          }
        };

        if (isAdmin || !isGuest) {
          performRemove();
        } else {
          authUtils.verifyPassword({
            url: "/board/check-password",
            idValue: currentBno,
            idKey: "bno",
            successCallback: (validPw) => {
              performRemove(validPw);
            },
          });
        }
      }
    });
});
