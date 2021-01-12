<template>
  <ve-line :data="chartData">
  </ve-line>
</template>
<script>
export default {
  created() {
    let that = this;
    $.get("http://localhost:3000/api/checkData/attendenceData", data => {
      let chart = [];
      data.forEach(element => {
        let json = {
          日期: element.date,
          签到用户: element.realNum,
          签到率: element.realNum / element.totalNum
        };
        chart.push(json);
      });
      that.chartData = { columns: ["日期", "签到用户"], rows: chart };
    });
  },
  name: "child2",
  data() {
    return {
      chartData: {
        columns: ["日期", "签到用户"],
        rows: [
          //   { 日期: "1/1", 签到用户: 97, 签到率: 0.97 },
          //   { 日期: "1/2", 签到用户: 100, 签到率: 1 },
          //   { 日期: "1/3", 签到用户: 99, 签到率: 0.99 }
        ]
      }
    };
  }
};
</script>
