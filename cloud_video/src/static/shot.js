var track;

function run(v, canvas, audioUse,photo = "",take = false) {
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
		audio: audioUse
	};

	let videoPlaying = false;
	let promise = navigator.mediaDevices.getUserMedia(constraints);
	console.log(constraints.audio);

	let ctx = canvas.getContext("2d");
	var scale = 1;

	let timer = null;

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

	const drawRectTimer = () => {
		canvas.width = v.videoWidth;
		canvas.height = v.videoHeight;
		//console.log(canvas.width, canvas.height);
		//ctx.clearRect(0, 0, canvas.width, canvas.height);
		ctx.drawImage(v, 0, 0);
	};
	if (!take) {
		if (!timer) timer = setInterval(drawRectTimer, 60);
	} else {
		drawRectTimer();
		// if (null != track) {
		// 	track.stop(); //关闭摄像头
		// }

		// if (timer) clearInterval(timer);
		// timer=null
		// if ("srcObject" in v) v.srcObject = null;
		// else {
		// 	v.src = (window.URL && window.URL.createObjectURL(null)) || null;
		// }
	}
	return videoPlaying;
}

const takeshot = (v,canvas, photo, rect) => {
	// canvas.width = v.videoWidth;
	// canvas.height = v.videoHeight;
	let ctx=canvas.getContext("2d");
	//console.log(canvas.width, canvas.height);
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	ctx.drawImage(v, 0, 0);
	let photoctx = photo.getContext("2d");
	photo.width=canvas.width;
	photo.height=canvas.height
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
	let data = canvas.toDataURL("image/webp");
	this.$refs.photo.src = data;
};

export default run;