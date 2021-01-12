//判断浏览器是否支持HTML5 Canvas
function run(video, canvas, name, sex, res) {
    let timer;
    // //这段代 主要是获取摄像头的视频流并显示在Video 签中
    // window.addEventListener("DOMContentLoaded", function () {

    //     let context = canvas.getContext("2d"),
    //         videoObj = {
    //             "video": true
    //         },
    //         errBack = function (error) {
    //             console.log("Video capture error: ", error.code);
    //         };
    //     //拍照按钮
    //     // 		$("#snap").click(function () {
    //     // 			context.drawImage(video, 0, 0, 330, 250);
    //     // 			})
    //     //拍照每秒一次
        if (!timer)
            timer = setInterval(function () {
                context.drawImage(video, 0, 0, 330, 250)
                CatchCode();
            }, 1000);
    //     //navigator.getUserMedia这个写法在Opera中好像是navigator.getUserMedianow
    //     //更新兼容火狐浏览器
    //     if (navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia) {
    //         navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia;
    //         navigator.getUserMedia(videoObj, function (stream) {
    //             video.srcObject = stream;
    //             video.play();
    //         }, errBack);
    //     }

    // }, false);
    if (navigator.mediaDevices === undefined) {
        navigator.mediaDevices = {};
    }
    if (navigator.mediaDevices.getUserMedia === undefined) {
        navigator.mediaDevices.getUserMedia = function (constraints) {
            // 首先，如果有getUserMedia的话，就获得它
            var getUserMedia =
                navigator.webkitGetUserMedia ||
                navigator.mozGetUserMedia ||
                navigator.msGetUserMedia;

            // 一些浏览器根本没实现它 - 那么就返回一个error到promise的reject来保持一个统一的接口
            if (!getUserMedia) {
                return Promise.reject(new Error("getUserMedia is not implemented in this browser"));
            }
            // 否则，为老的navigator.getUserMedia方法包裹一个Promise
            return new Promise(function (resolve, reject) {
                getUserMedia.call(navigator, constraints, resolve, reject);
            });
        };
    }
    const constraints = {
        video: true,
        audio: false,
    };
    let videoPlaying = false;
    let promise = navigator.mediaDevices.getUserMedia(constraints);
    let context = canvas.getContext("2d")

    promise
        .then((stream) => {
            //track = stream.getTracks()[0];
            // 旧的浏览器可能没有srcObject
            if ("srcObject" in video) {
                video.srcObject = stream;
            } else {
                // 防止再新的浏览器里使用它，它已经不再支持了
                video.src = window.URL.createObjectURL(stream);
            }
            video.onloadedmetadata = function (e) {
                video.play();
                videoPlaying = true;
            };
            if (!timer)
                timer = setInterval(function () {
                    let context = canvas.getContext("2d")
                    context.drawImage(video, 0, 0, 330, 250)
                    CatchCode();
                }, 1000);
        })
        .catch((err) => {
            console.error(err.name + ": " + err.message);
        });

    function dataURLtoFile(dataurl, filename) {
        let arr = dataurl.split(",");
        let mime = arr[0].match(/:(.*?);/)[1];
        let bstr = atob(arr[1]);
        let n = bstr.length;
        let u8arr = new Uint8Array(n);
        while (n--) {
            u8arr[n] = bstr.charCodeAt(n);
        }
        return new File([u8arr], filename, {
            type: mime
        });
    }

    function detect() {
        if (window.FaceDetector == undefined) {
            console.error("Face Detection not supported");
            return;
        }

        let faceDetector = new FaceDetector({
            fastMode: true,
        });
        //console.time("detect");
        return faceDetector
            .detect(video)
            .then((faces) => {
                //console.log(faces);
                context.lineWidth = 2;
                context.strokeStyle = "red";
                faces.forEach((value) => {
                    let item = value.boundingBox;
                    context.rect(
                        Math.floor(item.x * scale),
                        Math.floor(item.y * scale),
                        Math.floor(item.width * scale),
                        Math.floor(item.height * scale),
                    );
                    context.stroke();
                    // context.drawImage(video, 0, 0, 330, 250)
                    // CatchCode();
                });
                //console.timeEnd("detect");
            })
            .catch((e) => {
                console.error("Boo, Face Detection failed: " + e);
            });
    }

    //if (!timer) timer = setInterval(detect, 1000);

    //上传服务器
    function CatchCode() {
        //获取浏览器页面的画布对象
        //以下开始编 数据
        let dataURL = canvas.toDataURL("image/jpeg");
        //console.log(dataURL);

        let blob = dataURLtoFile(dataURL, "camera.jpg"); // base64 转图片file
        let fd = new FormData();
        fd.append("file", blob);
        //将图像转换为base64数据
        $.ajax({
            type: "post",
            url: `http://localhost:3000/api/face/singin`,
            data: fd,
            processData: false,
            contentType: false,
            success: function (data) {
                console.log(data);
                //   if(data.error_msg!="SUCCESS")
                //alert(data.error_msg)
                if (data.error_msg == "SUCCESS") {
                    name = data.result.user_list[0].user_id;
                    sex = data.result.user_list[0].user_info.sex;
                    if (sex = data.result.user_list[0].score >= 91)
                        res = "签到成功"
                    else
                        res = "签到失败"
                    // clearInterval(timer);
                    // timer = null;
                }
            },
            error: function () {
                alert("错误");
            }
        });
    }
}
export default run;