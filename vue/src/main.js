import Vue from 'vue'
import ElementUI from 'element-ui';
import App from './App.vue'
import router from './router'
import axios from 'axios'
import 'element-ui/lib/theme-chalk/index.css';
import './assets/gloable.css'
import request from "@/utils/request";

Vue.config.productionTip = false;

Vue.prototype.request = request;

Vue.prototype.$axios = axios;

Vue.use(ElementUI,{size: "mini"});

new Vue({
  router,
  render: h => h(App)
}).$mount('#app');
