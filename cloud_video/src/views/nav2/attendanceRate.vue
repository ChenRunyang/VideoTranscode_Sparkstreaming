<template>
  <div>
    <h4>趋势图</h4>
    <el-tabs
      v-model="activeName"
      @tab-click="handleClick"
    >
      <el-tab-pane
        label="实到人数"
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
            prop="totalNum"
            label="应到人数"
            width="180"
          ></el-table-column>
          <el-table-column
            prop="realNum"
            label="实到人数"
          ></el-table-column>
          <el-table-column
            prop="absenceNum"
            label="缺勤人数"
          ></el-table-column>
          <el-table-column
            prop="absenceRate"
            label="缺勤率"
          ></el-table-column>
          <el-table-column
            prop="avgTime"
            label="平均考勤时长"
          ></el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane
        label="缺勤人数"
        name="second"
      >
        <child2 v-if="isChildUpdate2"></child2>
      </el-tab-pane>
      <el-tab-pane
        label="缺勤率"
        name="third"
      >
        <child3 v-if="isChildUpdate3"></child3>
        <a>缺勤率</a>
      </el-tab-pane>
      <el-tab-pane
        label="平均考勤时长"
        name="fourth"
      >
        <child4 v-if="isChildUpdate4"></child4>
        <a>考勤时长</a>
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
            签到用户: data.realNum,
            签到率: data.realNum / data.totalNum
          };
          chart.push(json);
        });
        that.chartData = { columns: ["日期", "签到用户"], rows: chart };
      }
    });
  },
  data() {
    this.chartSettings = {
      metrics: ["签到用户"],
      dimension: ["日期"]
    };
    return {
      activeName: "first",
      isChildUpdate1: true,
      isChildUpdate2: false,
      isChildUpdate3: false,
      isChildUpdate4: false,

      chartData: {
        columns: ["日期", "签到用户"],
        rows: [
          { 日期: "1/1", 签到用户: 97, 签到率: 0.97 },
          { 日期: "1/2", 签到用户: 100, 签到率: 1 },
          { 日期: "1/3", 签到用户: 99, 签到率: 0.99 }
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
