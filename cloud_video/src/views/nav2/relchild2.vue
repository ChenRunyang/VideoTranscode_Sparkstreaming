<template>
  <ve-line :data="chartData"></ve-line>
</template>
<script>
export default {
  created() {
    let that = this;
    // $.get("http://localhost:3000/api/checkData/attendenceData", data => {
    //   let chart = [];
    //   data.forEach(element => {
    //     let json = {
    //       日期: element.date,
    //       缺勤用户: element.absenceNum,
    //       缺勤率: element.absenceNum / element.totalNum
    //     };
    //     chart.push(json);
    //   });
    //   that.chartData = { columns: ["日期", "缺勤用户"], rows: chart };
    // });
  },
  name: "child2",
  data() {
    return {
      chartData: {
        columns: ["日期", "系统总人数"],
        rows: [
          { 日期: "2021-01-03 11:16", 系统总人数: 4 },
          { 日期: "2021-01-03 11:17", 系统总人数: 3 },
          { 日期: "2021-01-03 11:18", 系统总人数: 4 }
        ]
      }
    };
  }
};
</script>      