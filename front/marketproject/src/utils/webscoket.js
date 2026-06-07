import  { userMenuActive } from '../store/index.js'
import { wsURL } from '../config'

export let wsTimer = null  // ws定时器
export let webSocket = null

const websocketclient = {
  
  // 初始化ws
  wsInit : (wsIsRun, username) => {
    if (typeof (WebSocket) == "undefined") {
      console.log("您的浏览器不支持WebSocket")
      return
    }
    if (!wsIsRun) return
    // 销毁ws
    websocketclient.wsDestroy()
    let wsurl = wsURL+username
    // 初始化ws
    webSocket = new WebSocket(wsurl)
    // ws连接建立时触发
    webSocket.addEventListener('open', websocketclient.wsOpenHanler)
    // ws服务端给客户端推送消息
    webSocket.addEventListener('message', websocketclient.wsMessageHanler)
    // ws通信发生错误时触发
    webSocket.addEventListener('error', websocketclient.wsErrorHanler)
    // ws关闭时触发
    webSocket.addEventListener('close', websocketclient.wsCloseHanler)

    // 检查ws连接状态,readyState值为0表示尚未连接，1表示建立连接，2正在关闭连接，3已经关闭或无法打开
    window.clearInterval(wsTimer)
    wsTimer = setInterval(() => {
      if (webSocket.readyState === 1) {
        window.clearInterval(wsTimer)
      } else {
        console.log('ws建立连接失败')
        wsInit()
      }
    }, 3000)
  },
  
  // 建立连接成功提示
  wsOpenHanler : (event) => {
  },

  // 发送消息
  sendDataToServer : (message) => {
    if (webSocket.readyState === 1) {
      webSocket.send(message)
    } else {
      throw Error('服务未连接')
    }
  },

  // 接受服务器推送数据 
  wsMessageHanler : (e) => {
    const newsStore = userMenuActive()
    let message = JSON.parse(e.data)
    if (newsStore.messageList == null) {
      newsStore.setMessage(message)
    }else{
      newsStore.messageList.push(message)
    }
    localStorage.setItem('messageList', JSON.stringify(newsStore.messageList))
  },

  // ws通信发生错误
  wsErrorHanler : (event) => {
    console.log(event, '通信发生错误')
    wsInit()
  },

  // ws关闭
  wsCloseHanler : (event) => {
    console.log(event, 'ws关闭')
    wsInit()
  },
  // 销毁ws
  wsDestroy : () => {
    if (webSocket != null) {
      webSocket.removeEventListener('open', wsOpenHanler)
      webSocket.removeEventListener('message', wsMessageHanler)
      webSocket.removeEventListener('error', wsErrorHanler)
      webSocket.removeEventListener('close', wsCloseHanler)
      webSocket.close()
      webSocket = null
      window.clearInterval(wsTimer)
    }
  },
}

export default websocketclient