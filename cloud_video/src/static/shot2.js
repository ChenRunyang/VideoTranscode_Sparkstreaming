function run(v, canvas, photo = "", take = false) {
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
    promise
        .then((stream) => {
            track = stream.getTracks()[0];
            // 旧的浏览器可能没有srcObject
            if ("srcObject" in v) {
                v.srcObject = stream;
            } else {
                // 防止再新的浏览器里使用它，它已经不再支持了
                v.src = window.URL.createObjectURL(stream);
            }
            v.onloadedmetadata = function (e) {
                v.play();

                videoPlaying = true;
            };
        })
        .catch((err) => {
            console.error(err.name + ": " + err.message);
        });

    return videoPlaying;
}

const takeshot = (canvas, photo, rect) => {
    let photoctx = photo.getContext("2d");
    photoctx.clearRect(0, 0, photo.width, photo.height);
    let rectX = rect.x - 50 > 0 ? rect.x - 50 : 0;
    let rectY = rect.y - 50 > 0 ? rect.y - 50 : 0;
    photoctx.drawImage(
        canvas,
        rectX,
        rectY,
        rect.width + 100,
        rect.height + 100,
        0,
        0,
        photo.width,
        photo.height,
    );
    // let data = canvas.toDataURL("image/webp");
    // this.$refs.photo.src = data;
};

export default run;