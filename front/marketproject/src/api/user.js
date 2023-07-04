import { $get, $post } from '@/utils/request'
import md5 from 'md5'
import { setTokenOverTime } from '../api/refreshToken.js'

// 用户注册
export let $user_register = async (params) => {
    // 密码md5加密
    params.password = md5(md5(params.password).split('').reverse().join(''))
    params.userrepwd = md5(md5(params.userrepwd).split('').reverse().join(''))
    let ret = await $post('/register',params)
    return ret
}

// 检查用户是否注册过
export let $user_checkname = async (params) => {
    let ret = await $post('/user/checkname',params)
    return ret
}

// 用户登录
export let $user_login = async (params) => {
    // 密码md5加密
    params.password = md5(md5(params.password).split('').reverse().join(''))
    let ret = await $post('/login',params)

    localStorage.setItem("BeforeTokenOverTime", setTokenOverTime())
    return ret
}

// 用户登出
export let $user_logout = async (params) => {
    let ret = await $post('/logout', params)
    return ret
}

// 检查token值
export let $check_token = async (params) => {
    let ret = await $post('/checkToken', params)
    return ret
}

// 获取新消息
export let $user_news = async (params) => {
    let ret = await $get('/user/news', params)
    return ret
}
