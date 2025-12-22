const token = document.querySelector('meta[name="_csrf"]').content;
const header = document.querySelector('meta[name="_csrf_header"]').content;

document.addEventListener("DOMContentLoaded", function () {
  const bnoElem = document.querySelector('input[name="bno"]');
  if (!bnoElem) return;

  const bno = bnoElem.value;
  const modalElem = document.getElementById("modifyModal");
  const modifyModal = modalElem ? new bootstrap.Modal(modalElem) : null;

  // 🌟 공통 비밀번호 확인 함수 (SweetAlert2 사용)
  function checkPassword(correctPw, callback) {
    Swal.fire({
      title: "비밀번호 확인",
      text: "게시글 작성 시 설정한 비밀번호를 입력하세요.",
      input: "password",
      inputPlaceholder: "Password",
      showCancelButton: true,
      confirmButtonText: "확인",
      cancelButtonText: "취소",
    }).then((result) => {
      if (result.isConfirmed) {
        if (result.value === correctPw) {
          callback(result.value); // 일치하면 콜백 실행
        } else {
          Swal.fire("오류", "비밀번호가 일치하지 않습니다.", "error");
        }
      }
    });
  }

  function loadReplies() {
    fetch(`/reply/list/${bno}`)
      .then((res) => {
        if (!res.ok) throw new Error("네트워크 응답에 문제가 있습니다.");
        if (res.url.includes("/member/login")) {
          console.warn("인증이 필요하거나 세션이 만료되었습니다.");
          return;
        }
        return res.text();
      })
      .then((html) => {
        if (!html) return;
        const listArea = document.getElementById("replyListArea");
        if (listArea) {
          listArea.replaceWith(new Range().createContextualFragment(html));
          addModifyEvents();
        }
      })
      .catch((err) => console.error("댓글 로드 실패:", err));
  }

  function addModifyEvents() {
    document.querySelectorAll(".btn-modify-reply").forEach((btn) => {
      btn.onclick = function () {
        const rno = this.getAttribute("data-rno");
        const text = this.getAttribute("data-text");
        const replayer = this.getAttribute("data-replayer");
        const replayerEmail = this.getAttribute("data-email");
        const correctPw = this.getAttribute("data-pw");
        const isAdmin = this.getAttribute("data-role") === "ADMIN";
        const isMine = this.getAttribute("data-is-mine") === "true";

        const performModify = (inputPw) => {
          document.getElementById("modalRno").value = rno;
          document.getElementById("modalText").value = text;
          modalElem.setAttribute("data-replayer", replayer);
          modalElem.setAttribute("data-email", replayerEmail); // 🌟 이메일 정보 모달에 보관
          modalElem.setAttribute("data-password", inputPw || ""); // 🌟 입력받은 비번 보관
          if (modifyModal) modifyModal.show();
        };

        if (isAdmin || isMine) {
          performModify();
        } else if (replayerEmail === "guest") {
          checkPassword(correctPw, (inputPw) => performModify(inputPw));
        } else {
          Swal.fire(
            "권한 없음",
            "타인의 회원 댓글은 수정할 수 없습니다.",
            "warning"
          );
        }
      };
    });

    document.querySelectorAll(".btn-remove-reply").forEach((btn) => {
      btn.onclick = function () {
        const rno = this.getAttribute("data-rno");
        const replayerEmail = this.getAttribute("data-email");
        const correctPw = this.getAttribute("data-pw");
        const isAdmin = this.getAttribute("data-role") === "ADMIN";
        const isMine = this.getAttribute("data-is-mine") === "true";

        const performRemove = (inputPw) => {
          if (!confirm("정말 삭제하시겠습니까?")) return;

          // 🌟 삭제 시 데이터 가공
          const removeData = { bno: bno };
          if (replayerEmail === "guest" && inputPw) {
            removeData.password = inputPw;
          }

          fetch(`/replies/${rno}`, {
            method: "DELETE",
            headers: { "Content-Type": "application/json", [header]: token },
            body: JSON.stringify(removeData), // 🌟 조건부 데이터 전송
          }).then((res) => {
            if (res.ok) loadReplies();
          });
        };

        if (isAdmin || isMine) {
          performRemove();
        } else if (replayerEmail === "guest") {
          checkPassword(correctPw, (inputPw) => performRemove(inputPw));
        } else {
          Swal.fire(
            "권한 없음",
            "타인의 회원 댓글은 삭제할 수 없습니다.",
            "warning"
          );
        }
      };
    });
  }

  // 등록 처리
  document.getElementById("replyRegisterForm").onsubmit = function (e) {
    e.preventDefault();

    const replayerEmail = this.replayerEmail.value;
    const passwordValue = this.password ? this.password.value : null;

    // 🌟 전송 데이터 객체 생성
    const replyData = {
      bno: bno,
      replayer: this.replayer.value,
      replayerEmail: replayerEmail,
      text: this.text.value,
    };

    // 🌟 손님일 때만 password 필드 추가
    if (replayerEmail === "guest" && passwordValue) {
      replyData.password = passwordValue;
    }

    fetch("/replies", {
      method: "POST",
      headers: { "Content-Type": "application/json", [header]: token },
      body: JSON.stringify(replyData), // 🌟 가공된 객체 전송
    }).then((res) => {
      if (res.ok) {
        this.text.value = "";
        if (this.password) this.password.value = "";
        loadReplies();
      }
    });
  };

  // 수정 처리
  document.getElementById("replyModifyForm").onsubmit = function (e) {
    e.preventDefault();
    const rno = document.getElementById("modalRno").value;
    const replayerEmail = modalElem.getAttribute("data-email");
    const password = modalElem.getAttribute("data-password");

    // 🌟 전송 데이터 객체 생성
    const modifyData = {
      bno: bno,
      rno: rno,
      text: document.getElementById("modalText").value,
      replayer: modalElem.getAttribute("data-replayer"),
      replayerEmail: replayerEmail,
    };

    // 🌟 손님일 때만 password 필드 추가 (null이면 전송 안함)
    if (replayerEmail === "guest" && password && password.trim() !== "") {
      modifyData.password = password;
    }

    fetch(`/replies/${rno}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json", [header]: token },
      body: JSON.stringify(modifyData), // 🌟 가공된 객체 전송
    }).then((res) => {
      if (res.ok) {
        modifyModal.hide();
        loadReplies();
      } else {
        Swal.fire("오류", "수정 요청에 실패했습니다. (400)", "error");
      }
    });
  };

  loadReplies();
});
