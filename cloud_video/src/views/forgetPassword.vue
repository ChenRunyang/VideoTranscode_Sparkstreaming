<template>
  <div class="container">
    <el-form
      ref="ruleForm2"
      label-position="left"
      label-width="0px"
      class="demo-ruleForm login-container"
    >
      <h3 class="title">忘记密码</h3>
      <el-form-item prop="account">
        <el-input
          type="text"
          auto-complete="off"
          placeholder="账户名"
        ></el-input>
      </el-form-item>
      <el-form-item
        prop="checkId"
        style="float:left;width:218px;margin-right:30px;"
      >
        <el-input
          type="text"
          auto-complete="off"
          placeholder="验证码"
        ></el-input>
      </el-form-item>
      <el-button
        type="primary"
        style="float:left;background-color:#FF6A6A;border:none;"
      >获取验证码</el-button>
      <el-form-item prop="newPwd">
        <el-input
          type="text"
          auto-complete="off"
          placeholder="新密码"
        ></el-input>
      </el-form-item>
      <el-form-item prop="oldPwd">
        <el-input
          type="text"
          auto-complete="off"
          placeholder="确认密码"
        ></el-input>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button
          type="danger"
          style="width:100%;border:none;"
        >确定</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style lang="scss" scoped>
@import "~scss_vars";
.container {
  background-image: url("../assets/bg1.jpg");
  width: 100%;
  height: calc(100% - 50px);
  position: relative;
  .login-container {
    position: absolute;
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-border-radius: 5px;
    background-clip: padding-box;
    left: 550px;
    top: 150px;
    width: 350px;
    height: 400px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
    .avatar {
      width: 80px;
      height: 80px;
      margin-left: 130px;
      margin-bottom: 20px;
    }
    .title {
      margin: 0px auto 40px auto;
      text-align: center;
      color: #505458;
    }
    .checkId {
      display: inline;
    }
    .remember {
      margin: 0px 0px 35px 0px;
    }
  }
}
</style>