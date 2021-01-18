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
            { 日期: "1/1", 转码任务: 97 },
            { 日期: "1/2", 转码任务: 100},
            { 日期: "1/3", 转码任务: 99 }
        ]
      }
    };
  }
};
</script>
