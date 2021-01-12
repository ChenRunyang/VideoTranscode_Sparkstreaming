<template>
  <div class="container">
    <div class="left">
      <el-form
        ref="ruleForm2"
        label-position="left"
        label-width="0px"
        class="demo-ruleForm login-container"
      >
        <h3 class="title">成员信息</h3>
        <el-form-item prop="name">
          <el-input
            type="text"
            auto-complete="off"
            placeholder="账户名称"
          ></el-input>
        </el-form-item>

        <el-form-item prop="sex">
          <el-input
            type="text"
            auto-complete="off"
            placeholder="性别"
          ></el-input>
        </el-form-item>

        <el-form-item prop="tel">
          <el-input
            type="text"
            auto-complete="off"
            placeholder="手机号"
          ></el-input>
        </el-form-item>

        <el-form-item>
          <el-button
            class="btn-1"
            type="primary"
          >重试</el-button>
          <el-button
            type="primary"
            class="btn-2"
          >取消</el-button>
        </el-form-item>

        <el-form-item>
          <el-button
            type="danger"
            class="btn-3"
          >确认</el-button>
        </el-form-item>

      </el-form>
    </div>
    <div class="right">
      <div class="top">
        <el-button
          type="info"
          class="takeshotBtn"
        >拍照</el-button>
        <div class="imgCon1" id="imgCon1">
        </div>
        <div class="imgCon2" id="imgCon2">
        </div>
        <p class="imgDesc">以摄像头实时拍摄图像为准</p>
      </div>

      <div class="bottom">
        <el-form
          ref="ruleForm2"
          label-position="left"
          label-width="0px"
          class="demo-ruleForm login-container"
        >
          <h3 class="title">设备设置</h3>
          <el-form-item prop="isUse">
            <el-checkbox>使用外接摄像头</el-checkbox>
          </el-form-item>
          <div class="block">
            <span class="demonstration">拍摄置信度</span>
            <el-slider
              v-model="value"
              :format-tooltip="formatTooltip"
            ></el-slider>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      value: 48
    };
  },
  methods: {
    formatTooltip(val) {
      return val / 100;
    }
  }
};
</script>

<style lang="scss" scoped>
.container {
  display: flex;
  flex-direction: row;
  .left {
    flex: 2;
    .login-container {
      margin-top: 100px;
      margin-left: 40px;
      width: 350px;
      height: 400px;
      padding: 35px 35px 15px 35px;
      background: #fff;
      border: 1px solid #eaeaea;
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
      .btn-1 {
        background-color: #ffb6c1;
        border: none;
        width: 120px;
        margin: 60px 20px 8px 30px;
      }
      .btn-2 {
        background-color: gray;
        width: 120px;
        border: none;
      }
      .btn-3 {
        width: 280px;
        border: none;
        margin: 0 25px;
      }
    }
  }
  .right {
    flex: 4;
    .top {
      .takeshotBtn {
        float: left;
        margin-left: 60px;
        margin-top: 50px;
        background-color: #ffb6c1;
        border: none;
      }
      height: 330px;
      .imgCon1 {
        clear: both;
        width: 300px;
        height: 300px;
        margin: 20px 10px 10px 60px;
        float: left;
        border: solid #eee 1px;
        border-radius: 10px;
        background-image: url("../../assets/face2.jpg");
      }
      .imgCon2 {
        width: 300px;
        height: 300px;
        margin: 20px 10px 10px 20px;
        float: left;
        border: solid #eee 1px;
        border-radius: 10px;
        background-image: url("../../assets/face2.jpg");
      }
      .imgDesc {
        clear: both;
        width: 600px;
        height: 30px;
        color: #333;
        font-size: 12px;
        // text-align: center;
        margin-left: 300px;
      }
    }
    .bottom {
      clear: both;
      .login-container {
        margin-top: 10px;
        margin-left: 40px;
        width: 600px;
        height: 400px;
        padding: 35px 35px 15px 35px;
        background: #fff;
        border: 1px solid #eaeaea;
        .title {
          margin: 0px auto 40px auto;
          text-align: center;
          color: #505458;
        }
      }
    }
  }
}
</style>

