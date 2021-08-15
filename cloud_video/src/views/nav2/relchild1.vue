<template>
  <ve-line :data="chartData">
  </ve-line>
</template>
<script>
export default {
  created() {
  //   let that = this;
  //   $.get("http://localhost:3000/api/checkData/attendenceData", data => {
  //     let chart = [];
  //     data.forEach(element => {
  //       let json = {
  //         日期: element.date,
  //         签到用户: element.realNum,
  //         签到率: element.realNum / element.totalNum
  //       };
  //       chart.push(json);
  //     });
  //     that.chartData = { columns: ["日期", "签到用户"], rows: chart };
  //   });
   },
  name: "child2",
  data() {
    return {
      chartData: {
        columns: ["日期", "转码任务"],
        rows: [
            { 日期: "2021-01-03 10:11", 转码任务: 3 },
            { 日期: "2021-01-03 10:12", 转码任务: 2},
            { 日期: "2021-01-03 10:13", 转码任务: 2 },
            { 日期: "2021-01-03 10:14", 转码任务: 1 }
        ]
      }
    };
  }
};
</script>
