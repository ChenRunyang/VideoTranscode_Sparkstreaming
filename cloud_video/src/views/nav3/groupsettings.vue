<template>
  <div>
    <div style="margin-bottom: 20px">
      <el-input v-model="newname" placeholder="请输入群组名" style="width:200px;maging-right=5px;"></el-input>
      <el-input v-model="newdescribe" placeholder="请输入描述(可选)" style="width:200px;maging-right=5px"></el-input>
      <el-button @click="add" style="magin-right=5px">增加</el-button>
    </div>
 
    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="selectChange"
    >

      <el-table-column prop="name" label="名称" width="120"></el-table-column>
      <el-table-column prop="describe" label="描述" show-overflow-tooltip></el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="del(scope.row, scope.$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
 
<script>
// api
import tableApi from '@/api'
import GLOBAL from "@/static/adminInfo"
export default {
  data() {
    return {
      tableData: [],
      newname:'',
      newdescribe:'',
    }
  },

  mounted()
  {
      this.getgrouplist();
  },

  methods: {
    // 增加行
    add() {
      $.ajax({
            type: "get",
            //url: `http://localhost:1002/getlogin?name=${
              url: `http://localhost:1003/api/addgroup?name=${this.newname}&user=${GLOBAL.admin}&describe=${this.newdescribe}`,
            success: data => {
              console.log(data)
              if(data.message!="群组创建成功")
              {
                alert(data.message);
              }
              else
              {
                this.getgrouplist();
              }
            },
          });
    },
   
    // 获取表格数据
    getList() {
      tableApi
        .getList()
        .then(res => {
          if (res.status === 200) {
            this.tableData = res.data
          } else {
            this.$message.error('获取数据失败')
          }
        })
        .catch(e => {
          this.$message.error(e.message)
        })
    },

    del(index,i){
      $.ajax({
            type: "get",
            //url: `http://localhost:1002/getlogin?name=${
            url: `http://localhost:1003/api/deletegroup?name=${this.tableData[i].name}&user=${GLOBAL.admin}`,
            success: data => {
              console.log(data);
             if(data.message=="数组删除成功")
             {
               this.getgrouplist();
             }
             else
             {
               alert("数组删除失败");
             }
            },
          });
        console.log(this.tableData[i]);
    },

     getgrouplist(){
        $.ajax({
            type: "get",
            //url: `http://localhost:1002/getlogin?name=${
              url: `http://localhost:1003/api/getgroupList?user=${GLOBAL.admin}`,
            success: data => {
              this.tableData = [];
              var datajson=JSON.parse(data)
              console.log(datajson);
              for(var i=0;i<data.length;i++)
              {
                this.tableData.push(
                  {name:datajson[i].groupname,
                   describe:datajson[i].groupdescribe
                  }
                )
              }
            },
            error: function() {
              alert("错误");
            }
          });
    },
  }
}
</script>