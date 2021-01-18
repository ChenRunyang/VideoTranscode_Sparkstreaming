<template>
  <ve-line :data="chartData"></ve-line>
</template>
<script>
export default {
  // created() {
  //  let that = this;
  //  let chart = [];
  //  setInterval(
  //    ()=>{
  //       $.get(`http://localhost:1005/api/getcurrect`, data => {
  //       var d = new Date();
  //       if(chart.length>=20)
  //       {
  //         chart.slice(0,1);
  //       }
  //       chart.push(
  //         {
  //           日期:d.getTime.toString(),
  //           转码任务:data.num
  //         }
  //       )
  //       that.chartData = { columns: ["日期", "当前任务队列"], rows: chart };
  //    });
  //    },5000)    //每5秒查询一次，检测当前的任务数量
  // },

  name: "child2",
  data() {
    return {
      chartData: {
        columns: ["日期", "当前任务队列"],
        rows: [
          { 日期: "1/1", 当前任务队列: 6 },
          { 日期: "1/2", 当前任务队列: 0 },
          { 日期: "1/3", 当前任务队列: 1 }
        ]
      }
    };
  }
};
</script>      