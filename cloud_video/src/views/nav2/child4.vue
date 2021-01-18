<template>
  <ve-line :data="chartData"></ve-line>
</template>
<script>
export default {
  name: "child2",
  // created() {
  //   let that = this;
  //   let chart = [];
  //  setInterval(
  //    ()=>{
  //       $.get(`http://localhost:1005/api/gettime`, data => {
  //       var d = new Date();
  //       if(chart.length>=20)
  //       {
  //         chart.slice(0,1);
  //       }
  //       chart.push(
  //         {
  //           视频名:data.name,
  //           视频:data.time
  //         }
  //       )
  //       that.chartData = { columns: ["视频名", "转码时长"], rows: chart };
  //    });
  //    },5000)    //每5秒查询一次，检测当前的任务数量
  // },

  data() {
    return {
      chartData: {
        columns: ["视频名", "转码时长"],
        rows: [
          { 视频名: "1/1", 转码时长: 60 },
          { 视频名: "1/2", 转码时长: 50 },
          { 视频名: "1/3", 转码时长: 55 }
        ]
      }
    };
  }
};
</script>