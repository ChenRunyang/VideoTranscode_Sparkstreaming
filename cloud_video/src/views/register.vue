<template>
  <div class="container">
    <div class="form">
      <el-form ref="form" :model="form" label-width="80px">
        <h3 class="title">用户注册</h3>
        <el-form-item label="账户名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.sex" placeholder="请选择您的性别">
            <el-option label="男" value="male"></el-option>
            <el-option label="女" value="female"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="recheckPassword"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="上传头像">
          <!-- <el-upload
            class="upload-demo"
            action="/api/register/img"
            :auto-upload="false"
            :limit="1"
            ref="upload"ns
            :http-request="onSubmit"
            multiple
          >
            <el-button size="small" style="background-color:#eee;margin-right:10px" round>点击上传</el-button>
            <span class="n/el-upload__tip">请上传jpg/png文件，且不超过1M</span>
          </el-upload>-->
          <el-upload
            class="upload-demo"
            action="http://localhost:3000/api/login/file"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            :on-success="imgSuccess"
            multiple
            :limit="3"
            :on-exceed="handleExceed"
            :file-list="fileList"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="danger" @click="onSubmit" class="btn-1">提交</el-button>
          <el-button type="primary" class="btn-2" @click="onCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import $ from "jquery";
export default {
  data() {
    return {
      fileList: [],
      form: {
        name: "",
        password: "",
        sex: "",
        email: "",
        phone:"",
      },
      recheckPassword:"",
    };
  },

  methods: {
    onSubmit() {
      if(this.recheckPassword!=this.form.password){
        alert("密码不一致，请重新输入");
      }
     else{
         $.post("http://localhost:1002/getregist", this.form, data => {
        if(data.message=="注册成功")
        {
            alert("注册成功");
            this.$router.push({ path: "/login" });
        }
        else
        {
            alert("用户名冲突，请重新输入");
        }
      });
      console.log(this.form);
     }
      
    },
    onCancel() {
      this.$router.push({ path: "/login" });
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 3 个文件，本次选择了 ${
          files.length
        } 个文件，共选择了 ${files.length + fileList.length} 个文件`
      );
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    imgSuccess(res, file, fileList) {
      this.form.file = res;
      console.log(res);
      console.log(file);
      console.log(fileList); // 这里可以获得上传成功的相关信息
    }
  }
};
</script>

<style lang="scss" scoped>
@import "~scss_vars";
.container {
  background-image: url("../assets/bg1.jpg");
  width: 100%;
  height: calc(100% - 50px);
  position: relative;
  .form {
    position: absolute;
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-border-radius: 5px;
    background-clip: padding-box;
    left: 550px;
    top: 80px;
    width: 350px;
    height: 520px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
    .title {
      margin: 0px auto 40px auto;
      text-align: center;
      color: #505458;
    }
    .btn-1 {
      border: none;
      margin: 20px 30px;
    }
    .btn-2 {
      background-color: gray;
      border: none;
    }
  }
}
</style>