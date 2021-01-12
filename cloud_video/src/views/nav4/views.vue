<template>
    <div class="top">
        <div class="left">
            <div class="player-container">
                <div  class="videoplay">
                <video-player class="vjs-custom-skin" :options="playerOptions"></video-player>
                </div>
            </div>
        </div>
        <div class="right">
            <div class="files">
                <el-table :data="Schdata">
                    <el-table-column label = "名称" prop="name" width= 60%></el-table-column>
                    <el-table-column label="操作" width= 40%>
                    <template slot-scope="scope">
                    <el-button size="mini" type="danger"  float='left' @click="downlord(scope.row, scope.$index)">下载</el-button>
                    <el-button size="mini" type="danger" float='left' @click="view(scope.row,scope.$index)">观看</el-button>
                    </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
        <div class="bottom">
         <el-tabs tab-position="left" style="width: 400px;">
              <el-tab-pane label="视频转码">
                  <el-select v-model="codec" style="width:40%;">
                       <el-option 
                           v-for="codec in this.codecList"
                           :label="codec.label"
                           :value="codec.value"
                           :key="codec.label">
                        </el-option>
                  </el-select>
                  <el-select v-model="format" style="width:40%;">
                      <el-option
                          v-for="format in this.formatList"
                          :label="format.label"
                          :value="format.value"
                          :key="format.label">
                      </el-option>
                  </el-select>
                  <el-button @click="transConf">确认</el-button>
              </el-tab-pane>
              <el-tab-pane label="添加水印">
                 <el-input v-model="marking" placeholder="请输入水印文字(英文)" style="width: 40%"></el-input>
                 <el-input v-model="markingSize" placeholder="请输入水印大小" style="width:40%"></el-input>
                 <el-button @click="markingConf">确认</el-button>
              </el-tab-pane>
              <el-tab-pane label="视频剪辑">
                  <el-input v-model="starttime" placeholder="请输入开始时间(s)" style ="width: 30%"></el-input>
                  <el-input v-model="endtime" placeholder="请输入结束时间(s)" style ="width: 30%"></el-input>
                  <a>{{alltime}}</a>
                  <el-button @click="cutConf">确认</el-button>
              </el-tab-pane>
         </el-tabs>
        </div>
    </div>
</template>

<script>
    //引入video样式
    import 'video.js/dist/video-js.css'
    import 'vue-video-player/src/custom-theme.css'
    //引入hls.js
    import 'videojs-contrib-hls.js/src/videojs.hlsjs'

    import html2canvas from 'html2canvas'
    import GLOBAL from "@/static/adminInfo"

    export default {
        name: 'HLS',
        data() {
            return {
                codecList:[
                    {
                        label:'H264',
                        value:'H264',
                    },
                    {
                        label:'AVS',
                        value:'AVS',
                    },{
                        label:'VP8',
                        value:'VP8',
                    },{
                        label:'WMV',
                        value:'WMV',
                    },
                    {
                        label:'H265',
                        value:'H265',
                    }
                ],
                codec:'',
                formatList:[
                    {
                        label:'流畅',
                        value:'流畅',
                    },
                    {
                        label:'标清',
                        value:'标清'
                    },
                    {
                        label:'高清',
                        value:'高清',
                    },
                ],
                format:'',
                playerOptions: {
                    playbackRates: [0.7, 1.0, 1.5, 2.0], //播放速度
                    autoplay: true, //如果true,浏览器准备好时开始回放。
                    controls: true, //控制条
                    preload: 'auto', //视频预加载
                    muted: false, //默认情况下将会消除任何音频。
                    loop: false, //导致视频一结束就重新开始。
                    language: 'zh-CN',
                    aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
                    fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
                    sources: [{
                        type: 'application/x-mpegURL',
                        src: '',
                    }],
                    poster: "http://static.smartisanos.cn/pr/img/video/video_03_cc87ce5bdb.jpg", //你的封面地址
                    width: document.documentElement.clientWidth,
                    notSupportedMessage: '无法播放' //允许覆盖Video.js无法播放媒体源时显示的默认信息。
                },
                previewImg: "",
                dataurl: "",
                currentId : 0,
                barrageLoop: true,
                Schdata:[{
                    name:"wo",
                },
                {
                    name:"ni",
                }
                ],
                marking:'',
                markingSize:'',
                alltime:'',
                currectvideo:'',
            }
        },
        methods: {
            getVideoPic() {
                let video = document.getElementsByClassName('vjs-tech')[0];
                console.log(video);
                let canvas = document.createElement('canvas');
                let w = window.innerWidth;

                let h = window.innerWidth / 16 * 9
                canvas.width = w;
                canvas.height = h;
                console.log(canvas)
                const ctx = canvas.getContext('2d')

                ctx.drawImage(video, 0, 0, w, h);
                ctx.drawImage(video, 0, 0, w, h);
                this.previewImg = canvas.toDataURL('image/png');
                console.log(this.previewImg);
            },

            clickdata(row,event,column)
            {
                alert(row,event,column);
            },

            markingConf(){

            },

            transConf(){
                $.get(`http://localhost:1005/api/transformvideo/?user=${GLOBAL.admin}&name=${this.currectvideo}`,data=>{
                    console.log(data);
                })
            },
          
            downlord(index,i){
                $.get(`http://localhost:1005/api/getvideofile/?user=${GLOBAL.admin}&name=${this.Schdata[i].name}`,data=>{
                    console.log(data);
                });
            },

            view(index,i){
                $.get(`http://localhost:1005/api/getvideopath/?user=${GLOBAL.admin}&name=${this.Schdata[i].name}`,data=>{
                    console.log(data);
                    if(data.message=="success")
                    {
                        this.playerOptions.sources[0].src="http://localhost:1005/"+data.path;
                        this.currectvideo=this.Schdata[i].name;
                    }
                    else
                    {
                        console.log("getvideopatherror");
                    }
                });
            },

        mounted: function() {
        let that = this;
        $.get(`http://localhost:1005/api/getvideopath/?user=${GLOBAL.admin}`, data => {
          console.log(data);
          that.Schdata=JSON.parse(data);
          this.currectvideo=this.Schdata[0].name;
          this.alltime = this.Schdata[0].videotime;
        });
      },
    }
};
</script>

<style lang="scss" scoped>
    .top{
        height:50%;
        .left{
            float: left;
            width:80%;
             .player-container{
                  margin-top: 15px;
                  width: 98%;
                  height: 45%;
                  overflow: hidden;
                  .videoplay{
                      z-index: 0;
                      position: relative;
                      left:0px;
                      top:0px;
                      }
                  .poster{
                       z-index: 1;
                       position: absolute;
                       top: 110px;
                       width:180px;
                       height: 200px;
                    }
                }
        }
        .right{
            float: left;
            position:relative;
            width:19%;
            border-right: 5px;
            .files{
                margin-top:15px;
                position: relative;
                left: 0px;
                top:0px;
                text-align:left;
                line-height: 30px;
                }
    
        }
        .bottom{
        position: fixed;
        top:80%;
        width:60%;
        height: 10%;
        .roomleft{
            width:30%;
            float: left;
        }
        .roomright{
            width:30%;
            left: 40%;
            margin-left:5px;
            float:left;
        }
        .up{
            margin-left: 15px;
            margin-top: 20px;
        }
    }
    }
</style>
