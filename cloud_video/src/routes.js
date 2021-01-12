import Login from './views/login.vue'
import Register from './views/register.vue'
import Forget from './views/forgetPassword.vue'
import NotFound from './views/404.vue'
import Home from './views/Home.vue'
import VideoManage from './views/nav1/videoManage.vue'
import VideoInfo from './views/nav1/videoInfo1.vue'
import LiveInfo from "./views/nav1/liveInfo.vue";
import RealtimeResults from './views/nav2/realtimeResults.vue'
import AttendanceRate from './views/nav2/attendanceRate.vue'
import Groupsettings from "./views/nav3/groupsettings";
import Settings from './views/nav3/settings.vue'
import Views from './views/nav4/views.vue'
import Lives from './views/nav4/lives.vue'

// 前端路由配置
let routes = [{
        path: '/login',
        component: Login,
        name: '',
        hidden: true
    },
    {
        path: '/register',
        component: Register,
        name: '',
        hidden: true
    },
    {
        path: '/forget',
        component: Forget,
        name: '',
        hidden: true
    },
    {
        path: '/404',
        component: NotFound,
        name: '',
        hidden: true
    },
    {
        path: '/',
        component: Home,
        name: '管理',
        iconCls: 'el-icon-message',
        children: [
            { path: '/videoManage', component: VideoManage, name: '视频管理' },
            { path: '/videoInfo', component: VideoInfo, name: '录制视频' },
            { path: '/liveInfo',component:LiveInfo,name:'开启直播'},
        ]
    },
    {
        path: '/',
        component: Home,
        name: '观看',
        iconCls: 'fa fa-angellist',
        children:[
            { path: '/Views' , component: Views,name: '视频操作' },
            { path: '/Lives' , component: Lives,name: '观看直播' },
        ]
    },
    {
        path: '/',
        component: Home,
        name: '统计',
        iconCls: 'fa fa-id-card-o',
        children: [
            { path: '/realtimeResults', component: RealtimeResults, name: '实时考勤结果' },
            { path: '/attendanceRate', component: AttendanceRate, name: '出勤率' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '设置',
        iconCls: 'fa fa-address-card',
        children: [
            { path: '/settings', component: Settings, name: '账号配置' },
            { path: '/groupsettings', component: Groupsettings, name: '群组设置'}
        ]
    },
    {
        path: '*',
        hidden: true,
        redirect: { path: '/404' }
    }
];

export default routes;