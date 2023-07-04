import axios from "axios"
import { isRefreshToken } from '../api/refreshToken.js'
import { ElNotification } from 'element-plus'

//初始化一个axios实例
let instance = axios.create({
    //请求前缀
    baseURL: '/api',
    //请求超时时间
    timeout: 20000,
    headers: {
        'Content-Type': 'application/json;charset=utf-8'
    }
},(error) => {
    return Promise.reject(error);
})
// 添加一个请求拦截器
instance.interceptors.request.use(config => {
    //从localStorage中获取token
    let token = localStorage.getItem('before_token')
    if (token) {
        config.headers.token = token
    }
    let flag = isRefreshToken(instance, config)
    return flag ? flag : config
})

// 响应拦截器
instance.interceptors.response.use(response => {
    // 可在此处进行统一处理
        return response
    }, error => {
    // 响应错误处理
        const response = error.response;
        if (response && response.status === 500) {
            ElNotification({
            title: '服务器异常',
            message: '500 Internal Server Error',
            type: 'error'
        })
    }
    return Promise.reject(error)
})

//定义get请求方法
export const $get = async (url, params) => {
    let { data } = await instance.get(url, { params })
    return data;
}

//定义post请求方法
export const $post = async (url, params) => {
    let { data } = await instance.post(url, params)
    return data;
}

//定义put请求方法
export const $put = async (url, params) => {
    let { data } = await instance.put(url, { params })
    return data;
}

//定义delete请求方法
export const $delete = async (url, params) => {
    let { data } = await instance.delete(url, { params })
    return data;
}

//导出axios实例
export default instance