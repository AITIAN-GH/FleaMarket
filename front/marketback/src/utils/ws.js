export const ws = () => {
    let socket = {}
    if (!localStorage.getItem('user')) {
        return
    }
    let user = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {}
    let username = user.username
    if (typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket")
    } else {
        console.log("您的浏览器支持WebSocket")
        // 设置socket连接地址并传入username
        let socketUrl = "ws://localhost:8082/imserver/" + username;
        if (socket != null) {
            // 关闭socket连接
            socket.onclose = () => {
                console.log('socket 清空')
            }
            socket = null
        }
        // 开启一个websocket服务
        socket = new WebSocket(socketUrl);
        if (socket != null) {
            console.log("链接成功");
        } else {
            console.log("出错了");
        }
    }
    return socket
}
export const wsopen = (socket) => {
    console.log("链接打开")
}

export const wsclose = (socket) => {
    socket.onclose = () => {
        console.log('socket关闭')
    }
}
export const wserror = (socket) => {
    socket.onerror = () => {
        console.log("websocket发生了错误");
    }
}




