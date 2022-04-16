import Vue from 'vue'
import utils from './utils'
import store from '../store'
import bus from './bus'

let wsUrl = null
let wsSock = null
let connUUID = null

function start(token) {
  wsUrl = store.getters.baseWsUrl + token
  initWebSocket()
}

function stop() {
  connUUID = null

  if (wsSock != null) {
    wsSock.close()
    wsSock = null
  }
}

function initWebSocket() {
  stop()

  connUUID = utils.uuid()

  wsSock = new WebSocket(wsUrl)
  wsSock.uuid = connUUID
  wsSock.onopen = onOpen
  wsSock.onclose = onClose
  wsSock.onmessage = onMessage
  wsSock.onerror = onError
}

function onOpen(e) {
  if (e.target.uuid !== connUUID) return
}

function onError(e) {
  if (e.target.uuid !== connUUID) return
}

function onMessage(e) {
  const data = JSON.parse(e.data)
  const content = data.content
  if (data.type === 0) {
    // 查询历史消息
    if (content.pageNo === 0) {
      store.commit('initMessage', content.results.reverse())
      // store.commit('showTitle', content.results[0].nickname)
    } else {
      store.commit('moreMessage', content.results)
    }
    bus.$emit('MESSAGE_CHAT')
  } else if (data.type === 1) {
    // 如果对话框未打开则显示通知,点击后打开对话框
    if (!store.state.chatDialogVisible) {
      bus.$emit('ON_CHAT_MESSAGE', data)
      return
    }
    // 收到新消息
    store.commit('newMessage', {
      id: data.buddyId,
      nickname: data.nickname,
      content: data.content,
      type: data.msgType
    })
    bus.$emit('MESSAGE_CHAT')
  } else if (data.type === 2) {
    // 收到新消息
    store.commit('newMessage', {
      id: data.buddyId,
      nickname: data.nickname,
      content: data.content,
      type: data.msgType
    })
    bus.$emit('MESSAGE_CHAT')
  }
}

function onClose(e) {
  if (e.target.uuid !== connUUID) return
  setTimeout(initWebSocket, 1000 * 10)
}

function send(Data) {
  wsSock.send(Data)
}

const websocket = {
  start: start,
  stop: stop,
  send: send
}

Vue.prototype.$websocket = websocket

export default websocket
