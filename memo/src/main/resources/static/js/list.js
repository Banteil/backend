// memoList.js 파일 내용

/**
 * ISO 8601 문자열을 'yyyy-MM-dd HH:mm' 형식으로 포맷하는 함수
 * (여러 곳에서 재사용할 수 있도록 외부에 함수로 정의)
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
 * 메모 목록을 REST API를 통해 가져와 테이블에 표시하는 함수
 */
async function fetchAndDisplayMemos() {
  const url = "/rest/list";
  const memoListBody = document.getElementById("memoListBody");

  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const memoList = await response.json();

    // 테이블 내용 초기화
    memoListBody.innerHTML = "";

    if (memoList.length === 0) {
      memoListBody.innerHTML = `
                <tr>
                  <td colspan="4" class="text-center">저장된 메모가 없습니다.</td>
                </tr>
              `;
      return;
    }

    memoList.forEach((memo) => {
      const row = document.createElement("tr");
      row.style.cursor = "pointer";
      // 주의: 상세 페이지 주소는 현재 URL에 맞춰 /rmemo/read 로 변경되어 있습니다.
      row.onclick = () => {
        window.location.href = `/rmemo/read?id=${memo.id}`;
      };

      const createdDate = formatDateTime(memo.createDateTime);

      // 내용 미리보기 (20자 제한)
      const textPreview =
        memo.text.length > 20 ? memo.text.substring(0, 20) + "..." : memo.text;

      row.innerHTML = `
                <td>${memo.id}</td>
                <td>${textPreview}</td>
                <td>${createdDate}</td>
                <td>${memo.updateDateTime ? "O" : "X"}</td>
              `;
      memoListBody.appendChild(row);
    });
  } catch (error) {
    console.error("메모 목록을 불러오는 중 오류 발생:", error);
    memoListBody.innerHTML = `
            <tr>
              <td colspan="4" class="text-center text-danger">데이터 로드 실패: ${error.message}</td>
            </tr>
          `;
  }
}

// 페이지 로드 시 목록 가져오기 함수 실행
document.addEventListener("DOMContentLoaded", fetchAndDisplayMemos);
