$(document).ready(function () {
  const imgModalElement = document.getElementById("imgModal");
  const myModal = new bootstrap.Modal(imgModalElement);

  // 1. 이미지 클릭 이벤트
  $(document).on("click", ".uploadResult li img", function () {
    const file = $(this).closest("li").data("full");

    if (!file) {
      alert("이미지 경로를 찾을 수 없습니다.");
      return;
    }

    const modalImg = $("#imgModal .modal-body img");
    modalImg.attr("src", "/upload/display?fileName=" + file);

    // [수정] HTML 요소에 심어둔 data-title 값을 가져옴
    const movieTitle = $("#movieTitle").data("title");
    $("#imgModal .modal-title").text(movieTitle);

    myModal.show();
  });

  // 2. 모달 닫힘 이벤트 (배경 정리)
  imgModalElement.addEventListener("hidden.bs.modal", function () {
    $("#imgModal .modal-body img").attr("src", "");
    $(".modal-backdrop").remove();
    $("body").removeClass("modal-open");
    $("body").css({
      overflow: "",
      "padding-right": "",
    });
  });

  // 3. 별점(starrr) 초기화
  if ($.fn.starrr) {
    $(".starrr").starrr({
      rating: 0,
      change: function (e, value) {
        if (value) {
          console.log("선택된 평점: ", value);
          $('#reviewForm input[name="grade"]').val(value);
        }
      },
    });
  }
});
