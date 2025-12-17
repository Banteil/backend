$(function () {
  // -----------------------------------------------------------------------
  // sales overview
  // -----------------------------------------------------------------------

  var options_sales_overview = {
    series: [
      {
        name: "Ample Admin",
        data: [355, 390, 300, 350, 390, 180],
      },
      {
        name: "Pixel Admin",
        data: [280, 250, 325, 215, 250, 310],
      },
    ],
    chart: {
      type: "bar",
      height: 275,
      toolbar: {
        show: false,
      },
      foreColor: "#adb0bb",
      fontFamily: "inherit",
      sparkline: {
        enabled: false,
      },
    },
    grid: {
      show: false,
      borderColor: "transparent",
      padding: {
        left: 0,
        right: 0,
        bottom: 0,
      },
    },
    plotOptions: {
      bar: {
        horizontal: false,
        columnWidth: "25%",
        endingShape: "rounded",
        borderRadius: 5,
      },
    },
    colors: ["var(--bs-primary)", "var(--bs-secondary)"],
    dataLabels: {
      enabled: false,
    },
    yaxis: {
      show: true,
      min: 100,
      max: 400,
      tickAmount: 3,
    },
    stroke: {
      show: true,
      width: 5,
      lineCap: "butt",
      colors: ["transparent"],
    },
    xaxis: {
      type: "category",
      categories: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
      axisBorder: {
        show: false,
      },
    },
    fill: {
      opacity: 1,
    },
    tooltip: {
      theme: "dark",
    },
    legend: {
      show: false,
    },
  };

  const salesOverviewElem = document.querySelector("#sales-overview");

  // 🌟 [수정 포인트] 요소가 존재할 때만 차트를 생성하고 렌더링합니다.
  if (salesOverviewElem) {
    var chart_column_basic = new ApexCharts(
      salesOverviewElem, // 위에서 찾은 변수 사용
      options_sales_overview
    );
    chart_column_basic.render();
    console.log("차트 렌더링 완료");
  } else {
    // 요소가 없어도 에러를 던지지 않고 그냥 조용히 넘어갑니다.
    console.log("차트 요소를 찾을 수 없어 렌더링을 건너뜁니다.");
  }
});
