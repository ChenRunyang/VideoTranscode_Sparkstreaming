import babelpolyfill from 'babel-polyfill'
import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-default/index.css'
//import './assets/theme/theme-green/index.css'
import VueRouter from 'vue-router'
import store from './vuex/store'
import Vuex from 'vuex'
//import NProgress from 'nprogress'
//import 'nprogress/nprogress.css'
import VideoPlayer from 'vue-video-player'
import routes from './routes'
import Mock from './mock'
Mock.bootstrap();
import 'font-awesome/css/font-awesome.min.css'
import Vchart from 'v-charts'
import { vueBaberrage } from 'vue-baberrage'
import Stream from 'rtmp-streamer'


require('video.js/dist/video-js.css')
require('vue-video-player/src/custom-theme.css')

Vue.use(ElementUI)
Vue.use(VueRouter)
Vue.use(Vuex)
Vue.use(Vchart)
Vue.use(VideoPlayer)
Vue.use(vueBaberrage)
Vue.use(Stream)

//NProgress.configure({ showSpinner: false });

const router = new VueRouter({
	routes
})

router.beforeEach((to, from, next) => {
	//NProgress.start();
	if (to.path == '/login') {
		sessionStorage.removeItem('user');
	}
	let user = JSON.parse(sessionStorage.getItem('user'));
	if (!user && to.path != '/login' && to.path != '/register' && to.path != '/forget') {
		next({
			path: '/login'
		})
	} else {
		next()
	}
})

//router.afterEach(transition => {
//NProgress.done();
//});

new Vue({
	//el: '#app',
	//template: '<App/>',
	router,
	store,
	//components: { App }
	render: h => h(App)
}).$mount('#app')