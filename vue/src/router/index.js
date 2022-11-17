import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/Manage.vue'
import request from "@/utils/request";
import * as ElementUi from "element-ui";

Vue.use(VueRouter);

//  设置token
//  request 拦截器
//  可以对请求发送前 对请求做一些处理
//  比如统一加 token，对请求参数统一加密
request.interceptors.request.use(config => {
  config.headers['Content-Type'] = 'application/json;charset=utf-8';
  //从本地获取 数据
  let user = localStorage.getItem("userIn") ? JSON.parse(localStorage.getItem("userIn")) : null;
  if (user) {
    config.headers['token'] = user.token; //设置请求头
  }else {
    router.push("/login")
  }
  return config
},error => {
  return Promise.reject(error)
});

//  response 拦截器
//  可以在接口响应后统一 处理结果
// request.interceptors.response.use(
//     response => {
//       let res = response.data;
//       console.log(res)
//       // //  如果返回的是文件
//       //   if (response.config.responseType === 'blob'){
//       //     return res;
//       //   }
//       //  兼容 服务器返回的字符串数据
//       //   if (typeof res === 'string'){
//       //     res = res ? JSON.parse(res) : res
//       //   }
//       //   //  当权限验证不通过的时候，给出提示
//       //   if (res.code ==='401'){
//       //     ElementUi.Message({
//       //       message: res.msg,
//       //       type: 'error'
//       //     });
//       //
//       //   }
//     },error => {
//       console.log('err' + error);
//       return Promise.reject(error)
//     });


const routes = [
  {
    path: '/',
    component: () => import('../views/Manage.vue'),
    redirect: '/login',
    children: [ //子路由
      {
        path: 'home',
        name: '首页',
        component: () => import('../views/Home.vue'),
      },
      {
        path: 'UserView',
        name: '用户管理',
        component: () => import('../views/UserView.vue'),
      },
      {
        path: 'class',
        name: '班级管理',
        component: () => import('../views/Class.vue'),
      },
      {
        path: 'grade',
        name: '成绩管理',
        component: () => import('../views/Grade.vue'),
      },
      {
        path: 'dorm',
        name: '宿舍管理',
        component: () => import('../views/Dorm.vue'),
      },
      {
        path: 'file',
        name: '文件管理',
        component: () => import('../views/File.vue'),
      },
      {
        path: 'echart',
        name: '图表',
        component: () => import('../views/Echarts.vue'),
      }

    ]
  },
  //  登录
  {
    path: '/login',
    name: '登录',
    component: () => import('../views/Login.vue')
  },
  //  注册
  {
    path: '/register',
    name: '注册',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});



export default router
