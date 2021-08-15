<template>
  <div class="container">
    <el-form
      :model="ruleForm2"
      ref="ruleForm2"
      label-position="left"
      label-width="0px"
      class="demo-ruleForm login-container"
    >
      <img class="avatar" src="../assets/avatar.png" alt="no img">
      <h3 class="title">流 媒 体 转 码 系 统</h3>
      <el-form-item prop="account">
        <el-input type="text" v-model="ruleForm2.account" auto-complete="off" placeholder="账户名"></el-input>
      </el-form-item>
      <el-form-item prop="checkPass">
        <el-input
          type="password"
          v-model="ruleForm2.checkPass"
          auto-complete="off"
          placeholder="密码"
        ></el-input>
      </el-form-item>
      <el-checkbox class="remember">记住密码</el-checkbox>
      <span class="el-upload__tip otherLinks">
        <a class="link" @click="register">注册</a>
        <a class="link" @click="forget">忘记密码</a>
      </span>
      <el-form-item style="width:100%;">
        <!-- <el-button type="danger" style="width:100%;" @click="login" :loading="logining">登录</el-button> -->
        <el-button type="danger" style="width:100%;" @click="login">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { requestLogin } from "../api/api";
import $ from "jquery";
import GLOBAL from "@/static/adminInfo"

export default {
  data() {
    return {
      logining: false,
      ruleForm2: {
        account: "",
        checkPass: ""
      }
    };
  },
  methods: {
    // 此处拦截了页面跳转，未知原因
    register: function() {
      console.log("注册");
      this.$router.push({ path: "/register" });
    },
    forget: function() {
      this.$router.push({ path: "/forget" });
    },
    // login() {
    //   this.$router.push({ path: "/videoManage" });
    // }
    login(ev) {
      let that = this;
      this.$refs.ruleForm2.validate(valid => {
        if (valid) {
          this.logining = true;
          var loginParams = {
            username: this.ruleForm2.account,
            password: this.ruleForm2.checkPass
		  };
		  
		  //password加密
          $.ajax({
            type: "get",
            //url: `http://localhost:1002/getlogin?name=${
              url: `http://localhost:1003/api/loginserver?name=${
              loginParams.username
            }&password=${Buffer.from(loginParams.password).toString("base64")}`,
            success: data => {
              console.log(data);
              if (data.message == "登陆成功") {
                sessionStorage.setItem(
                  "user",
                  JSON.stringify(loginParams.username)
                );
                GLOBAL.admin=data.adminid;
                that.$router.push({ path: "/videoManage" });
              } else {
                that.$router.push({ path: "/login" });
                that.$message({
                  message: data,
                  type: "error"
                });
              }
            },
            error: function() {
              alert("错误");
            }
          });
          //   requestLogin(loginParams).then(data => {
          //     this.logining = false;
          //     let { msg, code, user } = data;
          //     if (code !== 200) {
          //       this.$message({
          //         message: msg,
          //         type: "error"
          //       });
          //     } else {
          //       sessionStorage.setItem("user", JSON.stringify(user));
          //       this.$router.push({ path: "/peopleManage" });
          //     }
          //   });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
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
    .otherLinks {
      margin-left: 180px;
      .link {
        text-decoration: none;
        color: #333;
        &:hover {
          cursor: pointer;
        }
      }
    }
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
    .remember {
      margin: 0px 0px 35px 0px;
    }
  }
}
</style>