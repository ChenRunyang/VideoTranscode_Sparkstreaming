<template>
  <div class="container">
    <div class="left">
      <el-form
        ref="ruleForm2"
        label-position="left"
        label-width="0px"
        class="demo-ruleForm login-container"
      >
          <h3 class="title">直播控制台</h3>
        <el-form-item prop="name">
          <el-input type="text" auto-complete="off" placeholder="推流地址" v-model="serverurl"></el-input>
          <el-input type="text" auto-complete="off" placeholder="直播间名称" v-model="name"></el-input>
          <el-input type="text" auto-complete="off" placeholder="直播间密码(可选)" v-model="password"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="danger" class="btn-3" @click="startlive">开始直播</el-button>
           <el-button type="danger" class="btn-1" @click="takeshot">截屏</el-button>
          <el-button type="primary" class="btn-2" @click="stoplive">结束直播</el-button>
          <a>{{state}}</a>
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
      ext: "",
      myCanvas:null,
      formInline:{
      },
      cameraList:[],
      value:'',
      dataurl: "",
      chunks:[],
      recorder:'',
      state:"空闲中",
      serverurl:'',
      password:'',
      downloadUrl:'',
      timer:'',
      firstblockflag:true,
      picNum:0,
      videoseq:0,
            }
  },


  mounted() {
      this.getDevices();
      this.$refs.v.src=this.value;
      this.videoPlaying = this.queryShot();
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

    startlive(){
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
         this.firstblockflag=true;
         this.chunks=[];
         let stream = this.$refs.v.srcObject;
         this.videoseq=0;
         this.picNum=0;
         this.recorder =new MediaRecorder(stream,options);
         $.get(`http://localhost:1005/api/startlive/?user=${GLOBAL.admin}&name=${this.name}&password=${this.password}&serverurl=${this.serverurl}`, data => {
           console.log(data);
           if(data.message=="成功")
           {
             this.state="正在直播";
           }
         });
         this.timer = setInterval(this.blockupdate,5000);                     //每隔5秒钟上传一下视频文件
      }

    },

    blockupdate(){
      console.log("update 1");
      if(!this.firstblockflag)           //首次上传chunk没有数据，无需上传
      {
        console.log("not first block");
        if(this.chunks.length){
            this.livedataupload();
          }                         //先上传视频文件
        this.chunks=[];
        this.recorder.stop();  
      }
      this.recorder.start(2000);
      this.firstblockflag=false;  
      this.recorder.ondataavailable=(e)=>{
        this.chunks.push(e.data);
      }
    },


         stoplive(){
          if(this.chunks.length){
            console.log(this.chunks.length);
            this.livedataupload();
          }
          this.recorder.stop();
          clearInterval(this.timer);
          $.get(`http://localhost:1005/api/stoplive/?room=${GLOBAL.admin}&password=${this.password}`, data => {
          console.log(data);
          if(data.message=="成功")
          {
             this.state="空闲中";
          }
         });
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

    livedataupload(){
      let blob=new Blob(this.chunks,{ type: 'video/mp4'} );
      let vseq=this.videoseq+"";
      this.videoseq++;
      let formData = new FormData();
      formData.append("file", blob);
      formData.append("name",this.name);
      formData.append("seq",vseq);
      formData.append("admin",GLOBAL.admin);
        $.ajax({
          type: "post",
          url: `http://localhost:1005/api/liveupdate`,
          data: formData,
          processData: false,
          contentType: false,
          success: function(data) {
            this.state="已上传";
          },
          error: function() {
            //alert("错误");
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
        width: 300px;
        margin: 0 25px;
      }
      .btn-2 {
        background-color: gray;
         width: 300px;
        border: none;
         margin: 0 25px;
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

