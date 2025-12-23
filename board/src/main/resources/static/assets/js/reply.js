const token = document.querySelector('meta[name="_csrf"]').content;
const header = document.querySelector('meta[name="_csrf_header"]').content;

document.addEventListener("DOMContentLoaded", function () {
  const bnoElem = document.querySelector('input[name="bno"]');
  if (!bnoElem) return;

  const bno = bnoElem.value;
  const modalElem = document.getElementById("modifyModal");
  const modifyModal = modalElem ? new bootstrap.Modal(modalElem) : null;

  function loadReplies() {
    console.log("보드 넘버 : ", bno);
    fetch(`/reply/list/${bno}`)
      .then((res) => {
        if (!res.ok) throw new Error("네트워크 응답 오류");
        return res.text();
      })
      .then((html) => {
        const listArea = document.getElementById("replyListArea");
        if (listArea) {
          listArea.replaceWith(new Range().createContextualFragment(html));
          addModifyEvents();
        }
      });
  }

  function addModifyEvents() {
    // 수정 버튼
    document.querySelectorAll(".btn-modify-reply").forEach((btn) => {
      btn.onclick = function () {
        const rno = this.getAttribute("data-rno");
        const text = this.getAttribute("data-text");
        const replayer = this.getAttribute("data-replayer");
        const replayerEmail = this.getAttribute("data-email");
        const isAdmin = this.getAttribute("data-role") === "ADMIN";
        const isMine = this.getAttribute("data-is-mine") === "true";

        const performModify = (inputPw) => {
          document.getElementById("modalRno").value = rno;
          document.getElementById("modalText").value = text;
          const modalElem = document.getElementById("modifyModal");
          modalElem.setAttribute("data-replayer", replayer);
          modalElem.setAttribute("data-email", replayerEmail);
          modalElem.setAttribute("data-password", inputPw || "");

          const modifyModal =
            bootstrap.Modal.getInstance(modalElem) ||
            new bootstrap.Modal(modalElem);
          modifyModal.show();
        };

        if (isAdmin || isMine) {
          performModify();
        } else if (replayerEmail === "guest") {
          // 🌟 확장된 authUtils 호출 방식
          authUtils.verifyPassword({
            url: "/replies/check-password",
            idValue: rno,
            idKey: "rno",
            successCallback: (validPw) => performModify(validPw),
          });
        } else {
          Swal.fire("권한 없음", "본인의 댓글만 수정 가능합니다.", "warning");
        }
      };
    });

    // 삭제 버튼
    document.querySelectorAll(".btn-remove-reply").forEach((btn) => {
      btn.onclick = function () {
        const rno = this.getAttribute("data-rno");
        const replayerEmail = this.getAttribute("data-email");
        const isAdmin = this.getAttribute("data-role") === "ADMIN";
        const isMine = this.getAttribute("data-is-mine") === "true";

        const performRemove = (inputPw) => {
          if (!confirm("정말 삭제하시겠습니까?")) return;
          const removeData = { bno: bno };
          if (replayerEmail === "guest" && inputPw)
            removeData.password = inputPw;

          fetch(`/replies/${rno}`, {
            method: "DELETE",
            headers: { "Content-Type": "application/json", [header]: token },
            body: JSON.stringify(removeData),
          }).then((res) => {
            if (res.ok) loadReplies();
          });
        };

        if (isAdmin || isMine) {
          performRemove();
        } else if (replayerEmail === "guest") {
          // 🌟 확장된 authUtils 호출 방식
          authUtils.verifyPassword({
            url: "/replies/check-password",
            idValue: rno,
            idKey: "rno",
            successCallback: (validPw) => performRemove(validPw),
          });
        } else {
          Swal.fire("권한 없음", "본인의 댓글만 삭제 가능합니다.", "warning");
        }
      };
    });
  }

  // 등록 처리
  document.getElementById("replyRegisterForm").onsubmit = function (e) {
    e.preventDefault();
    const replayerEmail = this.replayerEmail.value;
    const passwordValue = this.password ? this.password.value : null;

    const replyData = {
      bno: bno,
      replayer: this.replayer.value,
      replayerEmail: replayerEmail,
      text: this.text.value,
    };
    if (replayerEmail === "guest" && passwordValue)
      replyData.password = passwordValue;

    fetch("/replies", {
      method: "POST",
      headers: { "Content-Type": "application/json", [header]: token },
      body: JSON.stringify(replyData),
    }).then((res) => {
      if (res.ok) {
        this.text.value = "";
        if (this.password) this.password.value = "";
        loadReplies();
      }
    });
  };

  // 수정 완료 처리 (모달 내부 폼)
  document.getElementById("replyModifyForm").onsubmit = function (e) {
    e.preventDefault();
    const rno = document.getElementById("modalRno").value;
    const replayerEmail = modalElem.getAttribute("data-email");
    const password = modalElem.getAttribute("data-password");

    const modifyData = {
      bno: bno,
      rno: rno,
      text: document.getElementById("modalText").value,
      replayer: modalElem.getAttribute("data-replayer"),
      replayerEmail: replayerEmail,
    };
    if (replayerEmail === "guest" && password) modifyData.password = password;

    fetch(`/replies/${rno}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json", [header]: token },
      body: JSON.stringify(modifyData),
    }).then((res) => {
      if (res.ok) {
        modifyModal.hide();
        loadReplies();
      }
    });
  };

  loadReplies();
});
