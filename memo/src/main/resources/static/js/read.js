/**
 * URL 쿼리 파라미터에서 특정 키의 값을 가져오는 함수
 */
function getQueryParam(key) {
  return new URLSearchParams(window.location.search).get(key);
}

/**
 * ISO 8601 문자열을 'yyyy-MM-dd HH:mm' 형식으로 포맷하는 함수
 */
function formatDateTime(isoString) {
  if (!isoString) return "";
  const date = new Date(isoString);
  return date
    .toLocaleDateString("ko-KR", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
      hour12: false,
    })
    .replace(/\. /g, "-")
    .replace(/\./, "")
    .trim();
}

/**
 * REST API를 통해 메모 상세 정보를 가져와 화면에 표시하는 함수
 */
async function fetchAndDisplayMemo() {
  const memoId = getQueryParam("id");
  const memoContentDiv = document.getElementById("memoContent");

  if (!memoId) {
    memoContentDiv.innerHTML = `
            <div class="alert alert-danger w-100" role="alert">
              메모 ID가 URL에 지정되지 않았습니다.
            </div>
          `;
    return;
  }

  const url = `/rest/${memoId}`; // @GetMapping("/{id}") 매핑

  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const dto = await response.json(); // MemoDTO를 JSON으로 받음

    if (!dto || !dto.id) {
      throw new Error("존재하지 않거나 불러올 수 없는 메모입니다.");
    }

    const createDateTimeFormatted = formatDateTime(dto.createDateTime);
    const updateDateTimeFormatted = formatDateTime(dto.updateDateTime);
    const updateInfoHtml = dto.updateDateTime
      ? `
            <small class="text-secondary">
              (수정됨: <span>${updateDateTimeFormatted}</span>)
            </small>
            `
      : "";

    // HTML 템플릿 생성 및 삽입
    memoContentDiv.innerHTML = `
            <div class="card shadow-sm w-100">
              <div class="card-body">
                <h6 class="card-subtitle mb-2 text-muted">
                  번호: <span id="memoIdDisplay">${dto.id}</span> | 작성일:
                  <span id="createTimeDisplay">${createDateTimeFormatted}</span>
                </h6>
                <hr />
                <p class="card-text fs-5" id="memoTextDisplay">${dto.text}</p>
                ${updateInfoHtml}
              </div>
            </div>
          `;

    // 버튼 활성화 및 링크 설정
    const modifyBtn = document.getElementById("modifyBtn");
    const removeBtn = document.getElementById("removeBtn");

    modifyBtn.style.display = "inline-block";
    removeBtn.style.display = "inline-block";

    // 수정 버튼 링크 설정
    modifyBtn.href = `/rmemo/modify?id=${dto.id}`;

    // 🌟 삭제 ID 숨겨진 필드에 저장
    document.getElementById("memoIdData").value = dto.id;
  } catch (error) {
    console.error("메모 상세 정보를 불러오는 중 오류 발생:", error);
    memoContentDiv.innerHTML = `
            <div class="alert alert-danger w-100" role="alert">
              메모 로드 실패: ${error.message}
            </div>
          `;
  }
}

/**
 * 🌟🌟🌟 RESTful DELETE 요청 처리 함수 🌟🌟🌟
 */
async function handleMemoDelete() {
  // 🌟 ID를 숨겨진 필드에서 가져옴
  const memoId = document.getElementById("memoIdData").value;
  if (!memoId) {
    alert("삭제할 메모 ID를 찾을 수 없습니다.");
    return;
  }

  const confirmed = confirm(
    `정말로 ID ${memoId}번 메모를 삭제하시겠습니까?\n삭제된 데이터는 복구할 수 없습니다.`
  );

  if (confirmed) {
    const url = `/rest/remove/${memoId}`; // @DeleteMapping("{id}") 엔드포인트

    try {
      const response = await fetch(url, {
        method: "DELETE", // 🌟 DELETE 메서드 사용
      });

      if (response.ok) {
        // 서버에서 "success" 문자열 반환 예상
        const result = await response.text();

        Swal.fire({
          title: "메모 삭제 완료",
          text: `ID ${memoId}번 메모가 성공적으로 삭제되었습니다.`,
          icon: "success",
          draggable: true,
          timer: 1500,
          showConfirmButton: false,
        }).then(() => {
          window.location.href = "/rmemo/list";
        });
      } else {
        // 4xx, 5xx 에러 처리
        alert(`메모 삭제에 실패했습니다. 상태 코드: ${response.status}`);
        console.error("Delete Failed:", response.status);
      }
    } catch (error) {
      console.error("DELETE 요청 중 오류 발생:", error);
      alert("삭제 처리 중 통신 오류가 발생했습니다.");
    }
  }
}

// 페이지 로드 시 이벤트 리스너 등록
document.addEventListener("DOMContentLoaded", function () {
  // 1. 상세 정보 로드 함수 실행
  fetchAndDisplayMemo();

  // 2. 🌟 삭제 버튼 (<a> 태그)에 RESTful 삭제 함수 연결
  const removeBtn = document.getElementById("removeBtn");
  if (removeBtn) {
    // <a> 태그이므로 기본 동작을 막을 필요는 없지만,
    // href가 없으므로 클릭 이벤트를 처리
    removeBtn.addEventListener("click", handleMemoDelete);
  }
});
