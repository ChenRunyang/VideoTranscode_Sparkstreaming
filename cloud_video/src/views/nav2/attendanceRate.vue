<template>
  <div>
    <h4>任务状态图</h4>
    <el-tabs
      v-model="activeName"
      @tab-click="handleClick"
    >
      <el-tab-pane
        label="已转码任务统计"
        name="first"
      >
        <child1
          v-if="isChildUpdate1"
          :data="chartData"
        ></child1>
        <el-table
          :data="tableData"
          stripe
          style="width: 100%"
        >
          <el-table-column
            prop="date"
            label="日期"
            width="180"
          ></el-table-column>
          <el-table-column
            prop="name"
            label="视频名"
            width="180"
          ></el-table-column>
          <el-table-column
            prop="time"
            label="转码时间"
          ></el-table-column>
          <el-table-column
            prop="Inputformat"
            label="原格式"
          ></el-table-column>
          <el-table-column
            prop="Outputformat"
            label="转码格式"
          ></el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane
        label="当前任务队列"
        name="second"
      >
        <child2 v-if="isChildUpdate2"></child2>
        <a>当前任务队列</a>
      </el-tab-pane>
      <el-tab-pane
        label="转码失败数"
        name="third"
      >
        <child3 v-if="isChildUpdate3"></child3>
        <a>转码失败数</a>
      </el-tab-pane>
      <el-tab-pane
        label="视频转码时长"
        name="fourth"
      >
        <child4 v-if="isChildUpdate4"></child4>
        <a>视频转码时长</a>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import firstchild from "./child1";
import secondchild from "./child2";
import thirdchild from "./child3";
import fourthchild from "./child4";
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
    child3: thirdchild,
    child4: fourthchild
  },

  methods: {
    handleClick(tab) {
      if (tab.name == "first") {
        this.isChildUpdate1 = true;
        this.isChildUpdate2 = false;
        this.isChildUpdate3 = false;
        this.isChildUpdate4 = false;
      } else if (tab.name == "second") {
        this.isChildUpdate1 = false;
        this.isChildUpdate2 = true;
        this.isChildUpdate3 = false;
        this.isChildUpdate4 = false;
      } else if (tab.name == "third") {
        this.isChildUpdate1 = false;
        this.isChildUpdate2 = false;
        this.isChildUpdate3 = true;
        this.isChildUpdate4 = false;
      } else if (tab.name == "fourth") {
        this.isChildUpdate1 = false;
        this.isChildUpdate2 = false;
        this.isChildUpdate3 = false;
        this.isChildUpdate4 = true;
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
