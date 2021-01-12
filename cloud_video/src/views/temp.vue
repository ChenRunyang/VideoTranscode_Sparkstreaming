<template>
  <div>
    <h4>趋势图</h4>
    <div class="graph">
      这里放置统计图
    </div>
    <el-table
      :data="tableData"
      stripe
      style="width: 100%"
    >
      <el-table-column
        prop="date"
        label="日期"
        width="180"
      >
      </el-table-column>
      <el-table-column
        prop="totalNum"
        label="应到人数"
        width="180"
      >
      </el-table-column>
      <el-table-column
        prop="realNum"
        label="实到人数"
      >
      </el-table-column>
      <el-table-column
        prop="absenceNum"
        label="缺勤人数"
      >
      </el-table-column>
      <el-table-column
        prop="absenceRate"
        label="缺勤率"
      >
      </el-table-column>
      <el-table-column
        prop="avgTime"
        label="平均考勤时长"
      >
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [
        {
          date: "2018-12-24",
          totalNum: "池子健",
          realNum: "中国科学技术大学",
          absenceNum: "100",
          absenceRate: "0.9",
          avgTime: "34小时"
        },
        {
          date: "2018-12-24",
          totalNum: "池子健",
          realNum: "中国科学技术大学",
          absenceNum: "100",
          absenceRate: "0.9",
          avgTime: "34小时"
        },
        {
          date: "2018-12-24",
          totalNum: "池子健",
          realNum: "中国科学技术大学",
          absenceNum: "100",
          absenceRate: "0.9",
          avgTime: "34小时"
        },
        {
          date: "2018-12-24",
          totalNum: "池子健",
          realNum: "中国科学技术大学",
          absenceNum: "100",
          absenceRate: "0.9",
          avgTime: "34小时"
        },
        {
          date: "2018-12-24",
          totalNum: "池子健",
          realNum: "中国科学技术大学",
          absenceNum: "100",
          absenceRate: "0.9",
          avgTime: "34小时"
        }
      ]
    };
  }
};
</script>

<style lang="scss" scoped>
.graph {
  width: 100%;
  height: 300px;
  background-color: #eeeeee;
  margin-bottom: 20px;
  text-align: center;
  line-height: 300px;
}
</style>
