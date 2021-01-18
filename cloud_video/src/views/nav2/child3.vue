<template>
  <ve-line :data="chartData"></ve-line>
</template>
<script>
export default {
  name: "child2",
  // created() {
  //   let that = this;
  //    $.get(`http://localhost:1005/api/getfail`, data => {
  //       var datajson = JSON.parse(data);
  //       let chart = [];
  //       for(var i=0;i<data.length;i++)
  //       {
  //         chart.push(
  //         {
  //           日期:datajson[i].data,
  //           转码失败数:datajson[i].num
  //         })
  //       }
  //       that.chartData = { columns: ["日期", "转码失败数"], rows: chart };
  //    });
  //  },

  data() {
    return {
      chartData: {
        columns: ["日期", "转码失败数"],
        rows: [
          { 日期: "1/1", 转码失败数: 3 },
          { 日期: "1/2", 转码失败数: 0 },
          { 日期: "1/3", 转码失败数: 1 }
        ]
      }
    };
  }
};
</script>