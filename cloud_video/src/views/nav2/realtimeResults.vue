  <template>
  <div>
    <h4>实时考勤结果</h4>
    <el-table
      v-if="tableData.length"
      :data="tableData"
      style="wtimeth: 100%"
      class="table"
    >
      <el-table-column
        prop="time"
        label="日期"
        wtimeth="180"
      ></el-table-column>
      <!-- <el-table-column prop="imgPath" label="图片" sortable wtimeth="180">
        <template slot-scope="scope">
          <img :src="scope.row.imgPath" alt style="wtimeth: 50px;height: 50px">
        </template>
      </el-table-column>-->
      <el-table-column
        prop="name"
        label="姓名"
        wtimeth="180"
      ></el-table-column>
      <el-table-column
        prop="sex"
        label="性别"
        wtimeth="180"
      ></el-table-column>
      <el-table-column
        prop="group"
        label="群组"
        wtimeth="180"
      ></el-table-column>
      <el-table-column
        prop="ispass"
        label="是否考勤"
        wtimeth="180"
      ></el-table-column>
    </el-table>
    <Loading
      class="loading"
      v-if="!tableData.length"
    ></Loading>
  </div>
</template>

  <script>
import $ from "jquery";
import Loading from "../loading.vue";
export default {
  created() {
    let that = this;
    let count = 0;
    setInterval(() => {
      $.get("http://localhost:3000/api/checkData/realtimeData", data => {
        if (data.length != count) {
          that.tableData = data;
          count = data.length;
        }
      }),
        1000;
    });
  },
  components: {
    Loading
  },
  data() {
    return {
      tableData: []
    };
  }
};
</script>

<style scoped>
.table {
  width: 100%;
}
.loading {
  margin-top: 30px;
}
</style>
