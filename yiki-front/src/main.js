import Vue from 'vue'
import App from './App.vue'
import router from './router'

Vue.config.productionTip = false

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
Vue.use(ElementUI)

import { createPinia, PiniaVuePlugin } from 'pinia'
Vue.use(PiniaVuePlugin)
import pinia from "@/store/base"

import '@/assets/iconfont/iconfont.css'


import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'
Vue.use(Antd)

new Vue({
  router,
  pinia,
  render: h => h(App),
}).$mount('#app')
