import Vue from 'vue'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // a modern alternative to CSS resets

import Element from 'element-ui'
import './styles/element-variables.scss'

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'
import bus from './utils/bus'

import i18n from './lang' // internationalization
import './icons' // icon
import './permission' // permission control
import './utils/error-log' // error log

import * as filters from './filters' // global filters

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online ! ! !
 */
if (process.env.NODE_ENV === 'production') {
  const { mockXHR } = require('../mock')
  mockXHR()
}

Vue.use(Element, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
  i18n: (key, value) => i18n.t(key, value)
})

// register global utility filters
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false

function formatNumber(n) {
  n = n.toString()
  return n[1] ? n : '0' + n
}
function formatTime(number, format) {
  if (number === 0) {
    return '--'
  }
  const time = new Date(number)
  const newArr = []
  const formatArr = ['Y', 'M', 'D', 'h', 'm', 's', 'ms']
  newArr.push(time.getFullYear())
  newArr.push(formatNumber(time.getMonth() + 1))
  newArr.push(formatNumber(time.getDate()))

  newArr.push(formatNumber(time.getHours()))
  newArr.push(formatNumber(time.getMinutes()))
  newArr.push(formatNumber(time.getSeconds()))
  newArr.push(formatNumber(time.getMilliseconds()))

  for (const i in newArr) {
    format = format.replace(formatArr[i], newArr[i])
  }
  return format
}
Vue.filter('formatDate', function(value) {
  return formatTime(value, 'Y-M-D h:m:s')
})
Vue.prototype.hasAuth = function(auth) {
  return store.getters.roles.includes(auth)
}
new Vue({
  el: '#app',
  router,
  store,
  bus,
  i18n,
  render: h => h(App)
})
