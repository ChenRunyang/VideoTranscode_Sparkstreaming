<template>
  <div>
    <h4>直播人数统计</h4>
    <el-tabs
      v-model="activeName"
      @tab-click="handleClick"
    >
      <el-tab-pane
        label="观看直播人数统计"
        name="first"
      >
        <child1
          v-if="isChildUpdate1"
          :data="chartData"
        ></child1>
      </el-tab-pane>
      <el-tab-pane
        label="系统总人数"
        name="second"
      >
        <child2 v-if="isChildUpdate2"></child2>
        <a>系统总人数</a>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import firstchild from "./relchild1";
import secondchild from "./relchild2";

import $ from "jquery";
export default {
  created() {
    let that = this;
    let count = 0;
    $.get("http://localhost:3000/api/checkData/attendenceData", data => {
      if (data.length != count) {
        that.tableData = data;
        count = data.length;
        let chart = [];
        data.forEach(element => {
          let json = {
            日期: data.date,
            已转码任务: data.realNum,
          };
          chart.push(json);
        });
        that.chartData = { columns: ["日期", "已转码任务"], rows: chart };
      }
    });
  },
  data() {
    this.chartSettings = {
      metrics: ["转码任务"],
      dimension: ["日期"]
    };
    return {
      activeName: "first",
      isChildUpdate1: true,
      isChildUpdate2: false,
      isChildUpdate3: false,
      isChildUpdate4: false,

      chartData: {
        columns: ["日期", "已转码任务"],
        rows: [
          { 日期: "2020-12-31 ", 已转码任务: 1 },
            { 日期: "2021-01-01 ", 已转码任务:  0},
            { 日期: "2021-01-02 ", 已转码任务: 0},
            { 日期: "2021-01-03 ", 已转码任务: 1 }
        ]
      },

      tableData: []
    };
  },

  components: {
    child1: firstchild,
    child2: secondchild,
  },

  methods: {
    handleClick(tab) {
      if (tab.name == "first") {
        this.isChildUpdate1 = true;
        this.isChildUpdate2 = false;

      } else if (tab.name == "second") {
        this.isChildUpdate1 = false;
        this.isChildUpdate2 = true;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.graph {
  width: 100%;
  height: 250px;
  background-color: #eeeeee;
  margin-bottom: 20px;
  text-align: center;
  line-height: 300px;
  background-image: url("../../assets/graph2.jpg");
  background-size: 100% 250px;
}
.table {
  width: 100px;
  height: 200px;
}
</style>
