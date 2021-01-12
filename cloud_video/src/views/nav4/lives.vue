<template>
    <div class="top">
        <div class="left">
            <div class="player-container">
                <div  class="videoplay">
                <video-player class="vjs-custom-skin" :options="playerOptions"></video-player>
                </div>
                <div class="poster" v-if="danmu">
                     <vue-baberrage
                     :isShow= "barrageIsShow"
                     :barrageList = "barrageList"
                     :loop = "barrageLoop"
                     >
                    </vue-baberrage>
                </div>
            </div>
        </div>
        <div class="right">
                <div class = "zhibo">
                  <div class="imfo">
                      <a>{{user_count}}</a>  
                  </div>
                   <el-table :data="Schdata" :show-header="false" height="120">
                       <el-table-column prop="name"></el-table-column>
                   </el-table>
                   <div class="imfo">
                      <a>弹幕消息</a>  
                  </div>
                   <el-table :data="msgList" :show-header="false" height="200">
                       <el-table-column prop="user_id"></el-table-column>
                       <el-table-column prop="msg"></el-table-column>
                   </el-table>
                   <div class="inputBox">
                       <el-input v-model="inputData" placeholder="输入弹幕"></el-input>
                   </div>
                   <div class="inputButton">
                       <el-button @click="updatedanmu">发</el-button>
                   </div>
                   <el-button @click="addToList" style="width : 95%;margin-top:3px;">点击拍照</el-button>
                </div>
        </div>
        <div class="bottom">
         <el-tabs tab-position="left" style="width: 400px;">
              <el-tab-pane label="观看电视">
                  <el-select v-model="tvChannel" style="width:40%;">
                       <el-option 
                           v-for="tvChannel in this.tvList"
                           :label="tvChannel.label"
                           :value="tvChannel.value"
                           :key="tvChannel.label">
                        </el-option>
                  </el-select>
              </el-tab-pane>
              <el-tab-pane label="观看直播">
                  <div class="roomleft">
                  输入房间号：
                 <el-input placeholder="输入房间ID" v-model="roomID">
                 </el-input>
                  </div>
                  <div class="roomright">
                  输入密码(可选)：
                 <el-input placeholder="输入密码" v-model="roomPassword" type="password">
                 </el-input>
                  </div>
                  <el-button class="up" @click="updateroom">确认</el-button>
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
    import { MESSAGE_TYPE } from 'vue-baberrage'
    import GLOBAL from "@/static/adminInfo"

    export default {
        name: 'HLS',
        data() {
            return {
                tvList:[
                    {
                        label:'CCTV-1',
                        value:'http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8',
                    },
                    {
                        label:'CCTV-5',
                        value:'http://ivi.bupt.edu.cn/hls/cctv5phd.m3u8',
                    },{
                        label:'湖南卫视',
                        value:'http://ivi.bupt.edu.cn/hls/hunanhd.m3u8',
                    },{
                        label:'安徽卫视',
                        value:'http://ivi.bupt.edu.cn/hls/ahhd.m3u8',
                    },
                ],
                tvChannel:'',
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
                msg: 'Hello vue-baberrage',
                barrageIsShow: true,
                currentId : 0,
                barrageLoop: true,
                barrageList: [],
                danmu:true,
                Schdata:[
                ],
                inputData:'',
                msgList:[],
                roomID:'',
                pwdpass:true,
                roomPassword:'',
                user_count:'直播间成员',
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

            updatedanmu(){
                if(!this.pwdpass)
                {
                    alert("对不起，直播间密码错误");
                }
                else
                {
                    let that=this;
                     that.websocket.send(this.inputData);
                }
            },

            addToList (){
              
            },

            initWebSocket(){
            var tmp=GLOBAL.admin
            if ('WebSocket' in window) {
                this.websocket = new WebSocket(`ws://localhost:1004/websocket?${this.roomID}&${tmp}`);
            }else if ('MozWebSocket' in window) {
                 this.websocket = new WebSocket(`ws://localhost:1004/websocket?${this.roomID}&${tmp}`);
           }
            else {
                console.log('当前浏览器 Not support websocket');
               /* let that = this;
                 this.interval = window.setInterval(function(){ //每隔30秒钟发送一次心跳，避免websocket连接因超时而自动断开
                   that.forgeWebSocket();
                },30000);*/
            }
/*        let that = this;
        this.interval = window.setInterval(function(){ //每隔30秒钟发送一次心跳，避免websocket连接因超时而自动断开
                    if(that.websocket!=null){
                        that.websocket.send("HeartBeat");
                        console.log("发送心跳包：HeartBeat");
                    }
                },30000);
 */
            
            //连接发生错误的回调方法
            this.websocket.onerror = function (ev) {
                console.log("WebSocket连接发生错误");
            };

            //连接成功建立的回调方法
            this.websocket.onopen = function (ev) {
                console.log("WebSocket连接成功");
            }

            //接收到消息的回调方法
            this.websocket.onmessage = this.messageon;

            //连接关闭的回调方法
            this.websocket.onclose = function (ev) {
                console.log("WebSocket连接关闭");
                this.websocket=null;
            }
        },
        
       messageon(event) {
              var tmp=String(event.data);
              console.log(tmp);
              if(tmp.charAt(0)=='0')
              {
                  var arraytmp=tmp.slice(1,tmp.length).split(":");
                  this.user_id=arraytmp[0];
                  this.msg=arraytmp[1];
                  if(this.msgList.length>=30)
                  {
                      this.msgList=[];
                      this.barrageList=[];
                  }
                  this.msgList.push({msg:arraytmp[0],user_id:arraytmp[1]});
                  this.barrageList.push({
              id: ++this.currentId,
              msg: arraytmp[0],
              time: 5,
              type: MESSAGE_TYPE.NORMAL
              });
              } else if(tmp.charAt(0)=='1')
              {
                  this.user_count='直播间成员'+tmp.charAt(1);
              } else if(tmp.charAt(0)=='2')
              {
                  this.Schdata=[];
                  var userlist=tmp.slice(1,tmp.length).split(",");
                  console.log(userlist);
                  userlist.pop();
                  userlist.forEach(element => {
                      this.Schdata.push({name:element});
                  });
              }
            },
        
        updateroom()
        {
            $.get(`http://localhost:1005/api/watchlive/?user=${this.roomID}&password=${this.roomPassword}`, data => {
            if(data.message !="未找到")
            {
                console.log(data.message);
                this.pwdpass=true;
                this.playerOptions.sources[0].src=data.message;
            }
           });

            if(this.pwdpass)
            {
                this.initWebSocket();
                console.log("websocketinit");
            }
            else
            {
                alert("对不起，房间号或密码错误，请重新输入");
            }
        }
        },

        created:function(){
            this.tvChannel=this.tvList[0].value;
            this.playerOptions.sources[0].src=this.tvChannel;
            console.log(this.playerOptions.sources.src);
        },
        
        watch:{
            tvChannel(){
                this.playerOptions.sources[0].src=this.tvChannel;
            }
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
            .zhibo{
                margin-top:15px;
                position: relative;
                left: 0px;
                top:0px;
                text-align:center;
                line-height: 30px;
                display: table-cell !important;
                    .imfo{
                         background-color: #939393;
                         line-height: 30px;
                    }
                    .inputBox{
                        width:80%;
                        margin-top: 2px;
                        float: left;
                    }
                    .inputButton{
                        width:18%;
                         margin-top: 2px;
                        float: left;
                    }
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
