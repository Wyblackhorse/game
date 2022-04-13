import App from './App'

// #ifndef VUE3

import Vue from 'vue'
import uView from 'uview-ui';
// 国际化 json 文件，文件内容详见下面的示例
import messages from './locale/index'
import VueI18n from 'vue-i18n'

Vue.config.productionTip = false
App.mpType = 'app'
Vue.use(uView)

let i18nConfig = {
	locale: uni.getStorageSync('lang') ? uni.getStorageSync('lang') : 'en', //当前语言默认英文;
	messages,
}

Vue.use(VueI18n)
const i18n = new VueI18n(i18nConfig)

const app = new Vue({
	i18n,
    ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif
