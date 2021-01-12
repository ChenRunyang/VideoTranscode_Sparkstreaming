<template>
  <ve-line :data="chartData"></ve-line>
</template>
<script>
export default {
  name: "child2",
  created() {
    let that = this;
    $.get("http://localhost:3000/api/checkData/attendenceData", data => {
      let chart = [];
      data.forEach(element => {
        let json = {
          日期: element.date,
          平均考勤时长: element.avgTime
        };
        chart.push(json);
      });
      that.chartData = { columns: ["日期", "平均考勤时长"], rows: chart };
    });
  },
  data() {
    return {
      chartData: {
        columns: ["日期", "平均考勤时长"],
        rows: [
          // { 日期: "1/1", 平均考勤时长: 60, 签到率: 0.97 },
          // { 日期: "1/2", 平均考勤时长: 50, 签到率: 1 },
          // { 日期: "1/3", 平均考勤时长: 55, 签到率: 0.99 }
        ]
      }
    };
  }
};
</script>