window.console || (window.console = {}),
window.console.log || (window.console.log = function() {}),
function() {
    function e(t) {
        var i, r, s, n = 1;
        this.initPublish = {
            previewWindowWidth: 650,
            previewWindowHeight: 482,
            videoWidth: 640,
            videoHeight: 480,
            fps: 15,
            bitrate: 600,
            wmode: "transparent",
            quality: "high",
            allowScriptAccess: "always"
        },
        "string" == typeof arguments[n] && (i = arguments[n], n++),
        "object" == typeof arguments[n] && (e.__extend(this.initPublish, arguments[n]), n++),
        "object" == typeof arguments[n] && (e.__extend(this.initPublish, arguments[n]), n++),
        "function" == typeof arguments[n] && (r = arguments[n], n++),
        "function" == typeof arguments[n] && (s = arguments[n]),
        e.__id || (e.__id = 100),
        e.__publishers || (e.__publishers = []),
        e.__publishers.push(this),
        this.id = e.__id++,
        this.initSuccessCallbackFun = r,
        this.initErrorCallbackFun = s,
        this.errorMessageCallbackFun = null,
        this.component = {
            cameraList: [],
            microPhoneList: []
        },
        this.container = t,
        this.previewWindowWidth = this.initPublish.previewWindowWidth,
        this.previewWindowHeight = this.initPublish.previewWindowHeight,
        this.callbackObj = null,
        this.url = i,
        this.vcodec = {},
        this.acodec = {},
        this.code = 0,
        this.errors = {
            100 : "摄像头异常",
            101 : "麦克风异常",
            102 : "摄像头禁用",
            103 : "服务器关闭连接",
            104 : "服务器连接失败",
            105 : "推流地址错误",
            106 : "麦克风禁用",
            110 : "推流地址为空",
            199 : "未知错误"
        },
        this.sdkVersion = "V1.0.7",
        this.start()
    }
    e.prototype.start = function() {
        var e = {},
        t = {},
        i = {},
        r = this;
        return e.id = this.id,
        e.width = this.previewWindowWidth,
        e.height = this.previewWindowHeight,
        e.js_bridge_method = "nePublisher.js_bridge_message",
        t.wmode = this.initPublish.wmode,
        t.allowFullScreen = "true",
        t.allowScriptAccess = "always",
        t.quality = this.initPublish.quality,
        t.align = "middle",
        //swfobject.embedSWF("publisher-ebd4e1deba.swf", this.container, this.previewWindowWidth, this.previewWindowHeight, "11.1.0", "//nos.netease.com/vod163/AdobeFlashPlayerInstall.swf", e, t, i,
        //function(e) {
		r.callbackObj = document.getElementById('my-publisher'),
        //}),
        this
    },
    e.prototype.doPublish = function() {
        this.callbackObj.__publish(this.url, this.vcodec, this.acodec);
    },
    e.prototype.startPublish = function() {
        var t = 0;
        return "string" == typeof arguments[t] && (this.url = arguments[t], t++),
        "object" == typeof arguments[t] && (e.__extend(this.initPublish, arguments[t]), t++),
        "function" == typeof arguments[t] && (this.errorMessageCallbackFun = arguments[t]),
        this.setCamera(this.initPublish.cameraIndex || 0),
        this.setMicroPhone(this.initPublish.microPhoneIndex || 0),
        this.initPublish.size = this.initPublish.videoWidth + "x" + this.initPublish.videoHeight,
        this.vcodec.size = this.initPublish.size,
        this.vcodec.fps = this.initPublish.fps,
        this.vcodec.bitrate = this.initPublish.bitrate,
        this.vcodec.video = this.initPublish.video,
        this.acodec.audio = this.initPublish.audio,
        this.stopPublish(!0),
        this.url = this.url.replace(/\s/g, ""),
        "" === this.url ? e.js_bridge_message("error", '{"id":' + this.id + ',"code":110}') : void this.doPublish()
    },
    e.prototype.stopPublish = function(e) {
        this.callbackObj.__stop_publish()
    },
    e.prototype.stopPreview = function() {
        this.callbackObj.__stop_preview()
    },
    e.prototype.release = function() {
        this.callbackObj.__stop_publish(),
        this.callbackObj.__stop_preview();
        var t = 0,
        i = null;
        for (t = 0; t < e.__publishers.length; t++) if (i = e.__publishers[t], i.id == this.id) {
            e.__publishers.splice(t, 1);
            break
        }
        var r = document.getElementById(this.container);
        r && r.parentNode.removeChild(r)
    },
    e.prototype.startPreview = function(e, t, i) {
        t && i ? this.callbackObj.__preview(e, t, i) : this.callbackObj.__preview(e)
    },
    e.prototype.getCameraSnapshot = function(e, t) {
        if (!this.cameraSnapshotCallback) return this.cameraSnapshotCallback = e,
        this.callbackObj.__getCameraShapshot(t)
    },
    e.prototype.getDefinedErrors = function() {
        return this.errors
    },
    e.prototype.getCameraList = function() {
        return this.component.cameraList
    },
    e.prototype.setCamera = function(e) {
        if (this.initPublish.cameraIndex = e, e >= this.component.cameraList.length) throw new Error("Œ¥ªÒ»°µΩ∏√…„œÒÕ∑");
        cameraName = this.component.cameraList[e],
        this.vcodec.device_index = e,
        this.vcodec.device_name = cameraName
    },
    e.prototype.getMicroPhoneList = function() {
        return this.component.microPhoneList
    },
    e.prototype.setMicroPhone = function(e) {
        if (this.initPublish.microPhoneIndex = e, e >= this.component.microPhoneList.length) throw new Error("Œ¥ªÒ»°µΩ∏√¬ÛøÀ∑Á");
        microPhoneName = this.component.microPhoneList[e],
        this.acodec.device_index = e,
        this.acodec.device_name = microPhoneName
    },
    e.prototype.alterDefinedErrors = function(t) {
        return e.__extend(this.errors, t)
    },
    e.prototype.resetPreviewWindow = function(e) {
        this.previewWindowWidth = e.previewWindowWidth,
        this.previewWindowHeight = e.previewWindowHeight
    },
    e.prototype.getSDKVersion = function() {
        return this.sdkVersion
    },
    e.prototype.on_publisher_ready = function(e, t, i) {
        this.component.cameraList = e,
        this.component.microPhoneList = t,
        i.apply(this)
    },
    e.prototype.on_publisher_error = function(e, t) {
        this.stopPublish(),
        this.errorMessageCallbackFun(e, t)
    },
    e.prototype.on_publishInit_error = function(e, t, i) {
        i.apply(this, [e, t])
    },
    e.js_bridge_message = function(t, i, r) {
        if(typeof(i) != 'object') i = JSON.parse(i);
        var s = e.__findPublisher(i.id),
        n = i.code;
        switch (t) {
        case "flashReady":
            s.on_publisher_ready(i.cameras, i.microphones, s.initSuccessCallbackFun);
            break;
        case "sdkVersion":
            s.sdkVersion = i.sdkVersion;
            break;
        case "initError":
            s.on_publishInit_error(n, s.errors["" + n], s.initErrorCallbackFun);
            break;
        case "error":
            s.code = n, s.on_publisher_error(n, s.errors["" + n]);
            break;
        case "debug":
            break;
        case "publishStarted":
            break;
        case "connectSuccess":
            break;
        case "cameraSnapshot":
            !r && console && console.warn && console.warn("«Î‘⁄ø™∆Ù‘§¿¿∫Û π”√ΩÿÕºΩ”ø⁄"),
            s.cameraSnapshotCallback && s.cameraSnapshotCallback(r),
            s.cameraSnapshotCallback = null
        }
    },
    e.__findPublisher = function(t) {
        var i, r;
        for (i = 0; i < e.__publishers.length; i++) if (r = e.__publishers[i], r.id == t) return r;
        throw new Error("publisher not found. id=" + t)
    },
    e.__extend = function(t, i) {
        function r(e) {
            return "Array" === Object.prototype.toString.call(e).slice(8, -1)
        }
        function s(e) {
            return "Object" === Object.prototype.toString.call(e).slice(8, -1)
        }
        var n, o, a;
        for (n in i) o = t[n],
        a = i[n],
        o !== a && (a && (copyIsArray = r(a) || s(a)) ? (o = copyIsArray ? o && r(o) ? o: [] : o ? o: {},
        t[n] = e.__extend(o, a)) : void 0 !== a && (t[n] = a));
        return t
    },
    window.nePublisher = e
} ();