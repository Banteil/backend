document.addEventListener("DOMContentLoaded", function () {
  const bnoElem = document.querySelector('input[name="bno"]');
  if (!bnoElem) return;

  const bno = bnoElem.value;
  const modalElem = document.getElementById("modifyModal");
  const modifyModal = modalElem ? new bootstrap.Modal(modalElem) : null;

  // 🌟 서버로부터 HTML 조각을 받아와서 교체하는 함수
  function loadReplies() {
    fetch(`/reply/list/${bno}`)
      .then((res) => res.text()) // JSON이 아닌 TEXT(HTML)로 받음
      .then((html) => {
        const listArea = document.getElementById("replyListArea");
        listArea.innerHTML = html; // 받은 HTML 조각을 그대로 삽입
        addModifyEvents(); // 새로 생긴 버튼들에 이벤트 바인딩
      });
  }

  function addModifyEvents() {
    document.querySelectorAll(".btn-modify-reply").forEach((btn) => {
      btn.onclick = function () {
        const rno = this.getAttribute("data-rno");
        const text = this.getAttribute("data-text");
        const replayer = this.getAttribute("data-replayer");

        document.getElementById("modalRno").value = rno;
        document.getElementById("modalText").value = text;
        modalElem.setAttribute("data-replayer", replayer); // 400에러 방지용 저장
        if (modifyModal) modifyModal.show();
      };
    });
  }

  // 등록 처리
  document.getElementById("replyRegisterForm").onsubmit = function (e) {
    e.preventDefault();
    fetch("/replies", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        bno: bno,
        replayer: this.replayer.value,
        text: this.text.value,
      }),
    }).then((res) => {
      if (res.ok) {
        this.text.value = "";
        loadReplies(); // 등록 후 목록 갱신
      }
    });
  };

  // 수정 처리
  document.getElementById("replyModifyForm").onsubmit = function (e) {
    e.preventDefault();
    const rno = document.getElementById("modalRno").value;
    fetch(`/replies/${rno}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        bno: bno,
        text: document.getElementById("modalText").value,
        replayer: modalElem.getAttribute("data-replayer"),
      }),
    }).then((res) => {
      if (res.ok) {
        modifyModal.hide();
        loadReplies(); // 수정 후 목록 갱신
      }
    });
  };

  // 삭제 처리
  window.removeReply = function (rno) {
    if (!confirm("삭제하시겠습니까?")) return;
    fetch(`/replies/${rno}`, { method: "DELETE" }).then((res) => {
      if (res.ok) loadReplies(); // 삭제 후 목록 갱신
    });
  };

  loadReplies(); // 페이지 초기 진입 시 로드
});
