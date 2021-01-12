<template>
  <div class="container">
    <div class="left">
      <el-form
        ref="ruleForm2"
        label-position="left"
        label-width="0px"
        class="demo-ruleForm login-container"
      >
        <h3 class="title">录制控制台(先输入群组和视频名)</h3>
        <el-form-item prop="name">
          <el-input type="text" auto-complete="off" placeholder="视频名称" v-model="name"></el-input>
        </el-form-item>

        <el-form-item prop="groupSelect">
          <el-select v-model="group" placeholder="请选择群组" style="width:350px" >
              <el-option 
              v-for="group in this.groupList"
              :label="group.label"
              :value="group.value"
              :key="group.value">
              </el-option>
            </el-select>
        </el-form-item>

        <el-form-item>
          <el-button class="btn-1" type="primary" @click="livestart">开始直播</el-button>
          <el-button class="btn-1" type="primary" @click="start">开始录制</el-button>
          <el-button class="btn-2" type="primary" @click="cancel">视频操作</el-button>
          <el-button type="primary" class="btn-2" @click="end">结束录制</el-button>
          <el-button type="danger" class="btn-3" @click="takeshot">截屏</el-button>
          <a style=" margin: 60px 20px 100px 30px;">{{state}}</a>
        </el-form-item>
      </el-form>
    </div>
    <div class="right">
      <div class="top" >
        <video ref="v" style="width: 640px;height: 480px;display: none;"></video>
        <canvas ref="canvas" class="imgCon1"></canvas>
        <br>
        <canvas ref="photo" class = "imgCon1" hidden='true'></canvas>
        <p class="imgDesc"></p>
      </div>

      <div class="bottom">
        <el-form
          ref="ruleForm2"
          label-position="left"
          label-width="0px"
          class="demo-ruleForm login-container"
        >
          <h3 class="title">设备设置</h3>
          <el-form-item prop="isUse" :model=formInline>
            <el-select v-model="value">
              <el-option 
              v-for="camera in this.cameraList"
              :label="camera.label"
              :value="camera.value"
              :key="camera.value">
              </el-option>
            </el-select>
          
           &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
            <el-checkbox v-model="checked">有声录制</el-checkbox>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script>
import run from "@/static/shot";
import GLOBAL from "@/static/adminInfo"
import { throws } from "assert";
import Vue from "vue";
import axios from "axios";
import $ from "jquery";
import 'videojs-contrib-hls.js/src/videojs.hlsjs'
export default {
  data() {
    return {
      checked:false,
      videoPlaying: false,
      name: "",
      myCanvas:null,
      myContents:null,
      formInline:{
      },
      cameraList:[],
      value:'',
      previewImg: "",
      dataurl: "",
      chunks:[],
      recorder:'',
      state:"空闲中",
      downloadUrl:'',
      groupList:[],
      group: '',
      picNum: 1,
            }
  },


  mounted() {
      this.getDevices();
      this.$refs.v.src=this.value;
      this.videoPlaying = this.queryShot();
  },

  created(){
    this.getgroups();
  },

  watch:{
    checked(){
      this.queryShot();
    },

    value(){
      this.$refs.v.src=this.value;
        this.videoPlaying=this.queryShot();
    }
  },

  methods: {

    queryShot() {
      let v = this.$refs.v,
          audioUse=this.checked,
        canvas = this.$refs.canvas;
      run(v, canvas,audioUse);
      this.$forceUpdate();
    },
    
    takeshot() {
       let canvas = this.$refs.canvas;
       let photo = this.$refs.photo;
       let ctx = photo.getContext("2d");
       photo.width=canvas.width;
       photo.height=canvas.height;
       ctx.drawImage(canvas, 0, 0,canvas.width,canvas.height,0,0,photo.width,photo.height);  // 将video中的数据绘制到canvas里
       this.picupload();
    },

    start(){
        var options = {};
        if (typeof MediaRecorder.isTypeSupported == 'function') {               //检测浏览器支持类型
        if (MediaRecorder.isTypeSupported('video/webm;codecs=h264')) {
          options.mimeType = 'video/webm;codecs=h264';
         } else if (MediaRecorder.isTypeSupported('video/webm;codecs=vp8')) {
           options.mimeType = 'video/webm;codecs=vp8';
         } else if (MediaRecorder.isTypeSupported('video/webm;codecs=vp9')) {
           options.mimeType = 'video/webm;codecs=vp9';
         }
         let canvas=this.$refs.canvas;
         this.chunks=[];
         let stream = this.$refs.v.srcObject;
         this.recorder =new MediaRecorder(stream,options);
         this.state="正在录制视频。。。"
         
         this.recorder.start(2000);
         this.recorder.ondataavailable=(e)=>{
          this.chunks.push(e.data);
         }
      }

    },

    cancel(){
      this.$router.push({ path: "/videoManage" });
    },

    end(){
          if(this.chunks.length){
            this.videoupload();
          }
          setTimeout(() => {
            this.state="空闲中"
          }, 1000);
          this.recorder.stop();
    },

    getDevices () {
            return new Promise((resolve, reject) => {
                if (!navigator.mediaDevices || !navigator.mediaDevices.enumerateDevices) {
                    window.alert("不支持 mediaDevices.enumerateDevices()")
                }
                navigator.mediaDevices.enumerateDevices().then(devices => {
                    console.log(devices);
                    this.cameraList = [];
                    devices.forEach((device, index) => {
                        if (device.kind && device.kind === 'videoinput') {
                            this.cameraList.push({
                                value: device.deviceId,
                                label: device.label
                            })
                            this.value=this.cameraList[0].value;
                            console.log(this.value)
                        }  
                    })
                    resolve()
                }).catch((err) => {
                    console.log(err.name + ": " + err.message)
                    reject()
                })
            })
        },

    picupload() {
      let dataURL = this.$refs.photo.toDataURL("image/jpeg");
      console.log(dataURL);
      let blob = this.dataURLtoFile(dataURL, "camera.jpg"); // base64 转图片file
       let seq=this.picNum+"";
      this.picNum++;
      let formData = new FormData();
      formData.append("file", blob);
      formData.append("videoname",this.name);
      formData.append("seq",seq);
      formData.append("admin",GLOBAL.admin);
        $.ajax({
          type: "post",
          url: `http://localhost:1005/api/pictureupdate`,
          data: formData,
          processData: false,
          contentType: false,
          success: function(data) {
            alert(data.message);
          },
          error: function() {
            alert("错误");
          }
        });
    },

    videoupload(){
      let blob=new Blob(this.chunks,{ type: 'video/mp4'} );
      this.downloadUrl = window.URL.createObjectURL(blob);
      let formData = new FormData();
      formData.append("file", blob);
      formData.append("name",this.name);
      formData.append("groupname",this.group);
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

    livestart(){
        this.$router.push({ path: "/liveInfo" })
    },

    getgroups(){
        $.ajax({
            type: "get",
            //url: `http://localhost:1002/getlogin?name=${
              url: `http://localhost:1003/api/getgroupInfo?user=${GLOBAL.admin}`,
            success: data => {
              console.log(data);
              if (data.message == "群组数据传递成功") {
                data.groupdata.forEach(element => {
                  this.groupList.push({value:element,label:element});
                });
                console.log(this.groupList);
                this.group=this.groupList[0].value;
              } else {
                alert("没有群组数据，请创建群组");
                this.$router.push({ path: "/groupsettings" });
              }
            },
            error: function() {
              alert("错误");
            }
          });
    },

    dataURLtoFile(dataurl, filename) {
      let arr = dataurl.split(",");
      let mime = arr[0].match(/:(.*?);/)[1];
      let bstr = atob(arr[1]);
      let n = bstr.length;
      let u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      return new File([u8arr], filename, { type: mime });
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
      margin-top: 65px;
      margin-left: 40px;
      width: 350px;
      height: 500px;
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
         margin: 60px 20px 8px 30px;
      }
      .btn-3 {
        width: 300px;
        border: none;
        margin: 0 25px;
      }
    }
  }
  .right {
    flex: 4;
    .top {
       margin-top: 40px;
      .takeshotBtn {
        float: left;
        margin-left: 60px;
        margin-top: 50px;
        background-color: #ffb6c1;
        color:white;
        border: none;
      }
      height: 330px;
      .imgCon1 {
        clear: both;
        width: 600px;
        height: 400px;
        margin: 20px 10px 10px 60px;
        float: left;
        border: solid #eee 1px;
        border-radius: 10px;
        // background-image: url("../../assets/face2.jpg");
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

