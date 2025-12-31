$(document).ready(function () {
  const mno = $("input[name='mno']").val();
  const loginUserId = $("#loginUserId").val();
  const reviewForm = $("#reviewForm");

  // --- 1. 이미지 모달 로직 ---
  const imgModalElement = document.getElementById("imgModal");
  const myModal = new bootstrap.Modal(imgModalElement);

  $(document).on("click", ".uploadResult li img", function () {
    const file = $(this).closest("li").data("full");
    if (!file) return;

    $("#imgModal .modal-body img").attr(
      "src",
      "/upload/display?fileName=" + file
    );
    $("#imgModal .modal-title").text($("#movieTitle").data("title"));
    myModal.show();
  });

  imgModalElement.addEventListener("hidden.bs.modal", function () {
    $("#imgModal .modal-body img").attr("src", "");
  });

  // --- 2. 별점(starrr) 초기화 ---
  if ($.fn.starrr) {
    $(".starrr").starrr({
      rating: 0,
      change: function (e, value) {
        if (value) {
          $('#reviewForm input[name="grade"]').val(value);
        }
      },
    });
  }

  // --- 3. 리뷰 목록 가져오기 함수 (상단 통계 동기화 포함) ---
  function getMovieReviews() {
    function formatTime(str) {
      const date = new Date(str);
      return (
        date.getFullYear() +
        "/" +
        (date.getMonth() + 1) +
        "/" +
        date.getDate() +
        " " +
        date.getHours() +
        ":" +
        date.getMinutes()
      );
    }

    $.getJSON("/reviews/" + mno + "/all", function (list) {
      // --- 통계 계산 로직 추가 ---
      const reviewCount = list.length; // 전체 리뷰 개수
      let totalGrade = 0;

      $.each(list, function (idx, review) {
        totalGrade += parseFloat(review.grade);
      });

      // 평균 계산 (리뷰가 없을 경우 0.0 처리)
      const avgGrade =
        reviewCount > 0 ? (totalGrade / reviewCount).toFixed(1) : "0.0";

      // --- 상단 영화 정보 UI 동기화 ---
      $("input[name='reviewCnt']").val(reviewCount); // 리뷰 수 input 업데이트
      $("input[name='avg']").val(avgGrade); // 평점 input 업데이트
      $(".review-cnt").text(reviewCount); // 리뷰 영역 헤더 카운트 업데이트

      // --- 리뷰 목록 렌더링 ---
      let str = "";
      $.each(list, function (idx, review) {
        // 본인 글이거나 관리자인지 확인
        const canManage =
          loginUserId === review.email || loginUserRole === "ROLE_ADMIN";

        str += `
          <div class="p-4 mb-4 bg-gray-50 rounded-lg dark:bg-gray-700 shadow-sm border border-gray-100 dark:border-gray-600 review-row" data-rno="${
            review.rno
          }">
            <div class="flex justify-between items-center mb-2">
              <h5 class="text-sm font-bold text-purple-600 dark:text-purple-400">${
                review.nickname
              }</h5>
              <div class="flex items-center space-x-2">
                <span class="text-xs text-gray-500 review-date">${formatTime(
                  review.createDate
                )}</span>
                ${
                  canManage
                    ? `
                  <button class="text-xs text-blue-500 hover:underline modify-btn">수정</button>
                  <button class="text-xs text-red-500 hover:underline remove-btn">삭제</button>
                `
                    : ""
                }
              </div>
            </div>
            <div class="flex items-center mb-2">
              <span class="text-yellow-500 mr-2">★</span>
              <span class="font-semibold text-sm review-grade">${
                review.grade
              }</span>
            </div>
            <p class="text-sm text-gray-700 dark:text-gray-300 review-text">${
              review.text
            }</p>
          </div>`;
      });

      $(".reviewList").html(str);
    });
  }

  getMovieReviews();

  // --- 4. 리뷰 등록 로직 ---
  $(".reviewBtn").click(function () {
    // 로그인 체크
    if (!loginUserId) {
      if (
        confirm(
          "리뷰 작성을 위해 로그인이 필요합니다.\n로그인 페이지로 이동하시겠습니까?"
        )
      ) {
        location.href = "/member/login";
      }
      return;
    }

    const reviewText = $("textarea[name='text']").val();
    const grade = $("input[name='grade']").val();

    if (!reviewText.trim()) {
      alert("감상평을 입력해주세요.");
      return;
    }
    if (!grade || grade == 0) {
      alert("평점을 선택해주세요.");
      return;
    }

    const data = {
      mno: mno,
      grade: grade,
      text: reviewText,
      email: loginUserId,
    };

    $.ajax({
      url: "/reviews/" + mno,
      type: "POST",
      data: JSON.stringify(data),
      contentType: "application/json; charset=utf-8",
      dataType: "text",
      success: function (result) {
        alert("리뷰가 등록되었습니다.");
        // 폼 초기화
        $("textarea[name='text']").val("");
        $(".starrr").starrr("setRating", 0);
        // 리스트 새로고침
        getMovieReviews();
      },
      error: function (err) {
        alert("등록 실패: " + err.responseText);
      },
    });
  });

  // --- 5. 리뷰 삭제 로직 ---
  $(document).on("click", ".remove-btn", function () {
    const rno = $(this).closest(".review-row").data("rno");

    if (!confirm("리뷰를 삭제하시겠습니까?")) return;
    console.log(mno + "/" + rno);
    $.ajax({
      url: "/reviews/" + mno + "/" + rno,
      type: "DELETE",
      success: function (result) {
        alert("리뷰가 삭제되었습니다.");
        getMovieReviews();
      },
    });
  });

  // --- 6. 리뷰 수정 버튼 클릭 (리뷰 칸 내부에서 즉시 전환) ---
  $(document).on("click", ".modify-btn", function () {
    const row = $(this).closest(".review-row");
    const rno = row.data("rno");
    const oldText = row.find(".review-text").text();
    const oldGrade = row.find(".review-grade").text().trim();

    // 리뷰 내용을 textarea와 별점 선택 UI로 교체
    const editHtml = `
        <div class="mt-3 p-3 bg-white dark:bg-gray-800 rounded border border-blue-300 shadow-inner">
            <div class="flex items-center mb-2">
                <span class="text-xs font-semibold mr-2 text-gray-500">평점 수정:</span>
                <div class="starrr-edit" data-rating="${oldGrade}"></div>
                <input type="hidden" name="edit-grade" value="${oldGrade}">
            </div>
            <textarea name="edit-text" class="w-full p-2 text-sm border rounded dark:bg-gray-700 dark:text-white focus:outline-none focus:border-purple-500" rows="3">${oldText}</textarea>
            <div class="flex justify-end space-x-2 mt-2">
                <button class="px-3 py-1 text-xs text-gray-600 bg-gray-200 rounded hover:bg-gray-300 cancel-edit-btn">취소</button>
                <button class="px-3 py-1 text-xs text-white bg-blue-600 rounded hover:bg-blue-700 update-review-btn">수정 완료</button>
            </div>
        </div>
    `;

    // 기존 텍스트와 별점 영역을 숨기고 에디터 삽입
    row.find(".review-grade").parent().hide();
    row.find(".review-text").hide();
    row.append(editHtml);

    // 동적으로 생성된 에디터에 별점 라이브러리 입히기
    row.find(".starrr-edit").starrr({
      rating: oldGrade,
      change: function (e, value) {
        row.find("input[name='edit-grade']").val(value);
      },
    });
  });

  // --- 7. 수정 완료 버튼 클릭 ---
  $(document).on("click", ".update-review-btn", function () {
    const row = $(this).closest(".review-row");
    const rno = row.data("rno");
    const newText = row.find("textarea[name='edit-text']").val();
    const newGrade = row.find("input[name='edit-grade']").val();

    if (!newText.trim()) {
      alert("내용을 입력해주세요.");
      return;
    }

    const data = {
      rno: rno,
      mno: mno,
      text: newText,
      grade: newGrade,
      email: loginUserId,
    };

    $.ajax({
      url: `/reviews/${mno}/${rno}`,
      type: "PUT",
      data: JSON.stringify(data),
      contentType: "application/json; charset=utf-8",
      success: function (result) {
        alert("리뷰가 수정되었습니다.");
        getMovieReviews(); // 목록 새로고침
      },
    });
  });

  // --- 8. 수정 취소 ---
  $(document).on("click", ".cancel-edit-btn", function () {
    getMovieReviews(); // 그냥 목록을 다시 불러오면 원복됩니다.
  });
});
