<template>
  <div>
    <div>
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item class="item1">
          <el-input v-model="formInline.user" placeholder="输入搜索条件"></el-input>
        </el-form-item>
        <el-form-item label="搜索功能">
          <el-select v-model="formInline.search" placeholder="请选择搜索方式">
            <el-option label="用户名搜索" value="1"></el-option>
            <el-option label="群组搜索" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="每页显示">
          <el-select v-model="formInline.region" placeholder="5">
            <el-option label="1" value="1"></el-option>
            <el-option label="2" value="2"></el-option>
            <el-option label="3" value="3"></el-option>
            <el-option label="4" value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit" class="btn">搜索</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div>
      <h4>用户列表</h4>
      <el-table :data="Schdata" style="width: 100%">
        <el-table-column prop="imgPath" label="视频缩略图" width="200">
          <template slot-scope="scope">
            <img :src="scope.row.imgPath" alt style="width: 50px;height: 50px">
          </template>
        </el-table-column>
        <el-table-column prop="name" label="文件名" width="200"></el-table-column>
        <el-table-column prop="time" label="视频时长" width="200"></el-table-column>
        <el-table-column prop="groupName" label="所属群组" width="200"></el-table-column>
        <el-table-column prop="updateTime" label="上传时间" width="200"></el-table-column>
        <el-table-column prop="size" label="视频长宽" width="200"></el-table-column>
        <el-table-column label="操作" width="200">
           <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="see(scope.row, scope.$index)">观看</el-button>
          <el-button size="mini" type="danger" @click="see(scope.row,scope.$index)">操作</el-button>
          <el-button size="mini" type="danger" @click="del(scope.row, scope.$index)">删除</el-button>
        </template>
        </el-table-column>
      </el-table>
    </div>
    <el-button type="primary" class="addBtn" @click="clickHandler">录制视频</el-button>
    <el-upload action="111" :limit="1" :http-request="UploadFile" :show-file-list="false" :file-list="fileList" style="width:100px; float:left;margin-right: 100px;">
        <el-button  type="primary" class="addBtn" @click="clickUpdate">上传视频</el-button>
    </el-upload>
    <el-button type="primary" class="addBtn" @click="addGroup">添加群组</el-button>
  </div>
</template>

<script>
import $ from "jquery";
import GLOBAL from "@/static/adminInfo"
import axios from "axios";
export default {
  data() {
    return {
      formInline: {
        user: "",
        search: ""
      },
      Schdata: [],
      tableData: [],
      fileList:[],
    };
  },

  mounted: function() {
    let that = this;
    $.get(`http://localhost:1005/api/getvideoinfo/?user=${GLOBAL.admin}`, data => {
      console.log(data);
      that.tableData=JSON.parse(data);
      that.Schdata=JSON.parse(data);
    });
  },

  methods: {
    onSubmit() {
      this.Schdata = [];
      if(this.formInline.user=='')
      {
        this.Schdata=this.tableData;
      }
      else
      {
        for (var i = 0; i < this.tableData.length; i++) {
          if (this.formInline.search == 1) {
            if (this.tableData[i].name == this.formInline.user) {
              this.Schdata.push(this.tableData[i]);
              console.log("find");
            } else {
              console.log(this.formInline.user);
            }
          } else if (this.formInline.search == 2) {
            if (this.tableData[i].groupName == this.formInline.user) {
              this.Schdata.push(this.tableData[i]);
            } else {
              console.log("not find");
            }
          } else {
            console.log("error");
          }
        }
      }
    },

    clickHandler() {
      this.$router.push({ path: "/videoInfo" });
    },

    clickUpdater(){
      this.$$router.push({path: "/videoUpdate"});
    },

    del(index,i){
      $.get(`http://localhost:1005/api/delvideo/?user=${GLOBAL.admin}&name=${this.Schdata[i].name}`, data => {
      console.log(data);
      this.Schdata.splice(i,1);
    });
    },

    see(index,i){
      this.$router.push({path: '/views'});                      //传递当前视频名称
    },

    UploadFile(content){
      let formData = new FormData();
      formData.append("file", content.file);
      formData.append("name",content.file.name);
      formData.append("groupname",this.tableData[0].groupName);
      formData.append("admin",GLOBAL.admin);
        $.ajax({
          type: "post",
          url: `http://localhost:1005/api/videoupdate`,
          data: formData,
          processData: false,
          contentType: false,
          success: function(data) {
            this.state="已上传";
            alert(data.message);
          },
          error: function() {
            alert("错误");
          }
        });
    },

    addGroup(){
      this.$router.push({ path: "/groupsettings" });
    }
    
  }
};
</script>

<style lang="scss" scoped>
.btn {
  background-color: #ff6a6a;
  border: none;
}
.item1 {
  margin-right: 200px;
}
.addBtn {
  background-color: #ff6a6a;
  border: none;
  float: left;
  height: 30px;
  margin-right: 100px;
  margin-top: 100px;
}
.upBtn{
   background-color: #ff6a6a;
  border: none;
  float: left;
  width:  100px;
  height: 40px;
  margin-right: 150px;
  margin-top: 100px;
}
</style>
