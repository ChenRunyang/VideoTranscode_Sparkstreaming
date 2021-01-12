<template>
  <div class="container">
    <h4>修改账号</h4>
    <el-form
      ref="form"
      :model="form"
      label-width="80px"
    >
      <el-form-item label="账户名称">
        <el-input v-model="form.name" placeholder="请输入账号名称"></el-input>
      </el-form-item>
      <el-form-item label="原密码">
        <el-input v-model="form.oldPassword" placeholder="请输入原密码"></el-input>
      </el-form-item>
      <el-form-item label="新密码">
        <el-input v-model="form.newPassword" placeholder="请输入新密码"></el-input>
      </el-form-item>
      <el-form-item label="手机号码">
        <el-input v-model="form.contactWay" placeholder="请输入手机号"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.contactWay" placeholder="请输入邮箱"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          @click="onSubmit"
          class="btn-1"
        >提交</el-button>
        <el-button
          type="primary"
          class="btn-2"
          @click="cancel"
        >取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {
        name: "",
        oldPassword: "",
        newPassword: "",
        email: "",
        phone: "",
      }
    };
  },
  methods: {
    onSubmit() {
      
         $.post("http://localhost:1002/getchangeuser", this.form, data => {
        if(data.message=="账号信息修改成功")
        {
            alert("账号信息修改成功");
        }
        else 
        {
            alert("账号或密码不存在，请重新输入");
        }
      });
      console.log(this.form);
     
    },

    cancel(){
        this.$router.push({ path: "/videoManage" });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "~scss_vars";
.container {
  -webkit-border-radius: 5px;
  border-radius: 5px;
  -moz-border-radius: 5px;
  background-clip: padding-box;
  margin: 0px auto;
  width: 350px;
  height: 480px;
  padding: 35px 35px 15px 35px;
  background: #fff;
  .title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }
  .btn-1 {
    background-color: #ff6a6a;
    border: none;
    margin: 20px 30px;
  }
  .btn-2 {
    background-color: gray;
    border: none;
  }
}
</style>