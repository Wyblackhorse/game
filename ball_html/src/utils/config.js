import Vue from 'vue'

const domain = document.domain

const config = {
  baseUrl: 'http://' + domain + ':10010/',
  wsUrl: 'ws://' + domain + ':10010/ws/'
}

Vue.prototype.$config = config

export default config
