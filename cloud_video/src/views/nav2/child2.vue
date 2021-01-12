<template>
  <ve-line :data="chartData"></ve-line>
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
          缺勤用户: element.absenceNum,
          缺勤率: element.absenceNum / element.totalNum
        };
        chart.push(json);
      });
      that.chartData = { columns: ["日期", "缺勤用户"], rows: chart };
    });
  },
  name: "child2",
  data() {
    return {
      chartData: {
        columns: ["日期", "缺勤用户"],
        rows: [
          // { 日期: "1/1", 缺勤用户: 3, 缺勤率: 0.03 },
          // { 日期: "1/2", 缺勤用户: 0, 缺勤率: 0 },
          // { 日期: "1/3", 缺勤用户: 1, 缺勤率: 0.01 }
        ]
      }
    };
  }
};
</script>