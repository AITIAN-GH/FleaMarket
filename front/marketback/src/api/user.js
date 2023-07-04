import { $get, $post ,$put, $delete } from '@/utils/request'
import { setTokenOverTime } from '../api/refreshToken.js'
import md5 from 'md5'

// 用户注册
export const user_register = async (params) => {
    // 密码md5加密
    params.password = md5(md5(params.password).split('').reverse().join(''))
    params.confirmPassword = md5(md5(params.confirmPassword).split('').reverse().join(''))
    let ret = await $post('/register',params)
    return ret
}

// 检查用户是否注册过
export const $user_checkname = async (params) => {
    let ret = await $post('/user/checkname',params)
    return ret
}

// 用户登录
export const user_login = async (params) => {
    // 密码md5加密
    params.password = md5(md5(params.password).split('').reverse().join(''))
    let ret = await $post('/login',params)
    localStorage.setItem("BackTokenOverTime", setTokenOverTime())
    return ret
}

// 用户token检测
export const user_checkToken = async (params) => {
    let ret = await $post('/checkToken',params)
    return ret
}

// 检查用户是否注册过
export let user_checkname = async (params) => {
    let ret = await $post('/user/checkname',params)
    return ret
}

// 用户登出
export const user_logout = async (params) => {
    let ret = await $post('/logout', params)
    return ret
}


// 查询所有用户
export const getAllUsers = async (params) => {
    let ret = await $get('/user/page', params)
    return ret
}

// 查询用户信息
export const getUserInfo = async (param) => {
    let ret = await $post('/user',param)
    return ret
}

// 更新用户信息
export const updateUser = async (params) => {
    let ret = await $put('/user/update', params)
    return ret
}

// 更新用户信息
export const updatePass = async (params) => {
    params.password = md5(md5(params.password).split('').reverse().join(''))
    params.newPassword = md5(md5(params.newPassword).split('').reverse().join(''))
    params.confirmPassword = md5(md5(params.confirmPassword).split('').reverse().join(''))
    let ret = await $post('/user/changePass', params)
    return ret
}

// 更新用户信息
export const updateInfo = async (params) => {
    let ret = await $post('/user/updateInfo', params)
    return ret
}

// 更新用户信息
export const delUserById = async (param) => {
    let ret = await $delete('/user/del/'+param)
    return ret
}

export const delUserByIds = (params) => {
    let ret = $post('/user/del/batch',params)
    return ret
}