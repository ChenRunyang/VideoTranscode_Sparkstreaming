<template>
  <ve-line :data="chartData">
  </ve-line>
</template>
<script>
import GLOBAL from "@/static/adminInfo";
export default {
  // created() {
  //    let that = this;
  //    $.get(`http://localhost:1005/api/getfinish`, data => {
  //       var datajson = JSON.parse(data);
  //       let chart = [];
  //       for(var i=0;i<data.length;i++)
  //       {
  //         chart.push(
  //         {
  //           日期:datajson[i].data,
  //           转码任务:datajson[i].num
  //         })
  //       }
  //       that.chartData = { columns: ["日期", "转码任务"], rows: chart };
  //    });
  //  },

  name: "child2",
  data() {
    return {
      chartData: {
        columns: ["日期", "转码任务"],
        rows: [
            { 日期: "2020-12-31 ", 转码任务: 1 },
            { 日期: "2021-01-01 ", 转码任务:  0},
            { 日期: "2021-01-02 ", 转码任务: 0},
            { 日期: "2021-01-03 ", 转码任务: 1 }
        ]
      }
    };
  }
};
</script>
