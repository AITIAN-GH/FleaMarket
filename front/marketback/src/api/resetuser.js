import { $get, $post } from '../utils/request.js'
import md5 from 'md5'

export const getCaptcha = async (params) => {
    let res = await $get('/captcha/create',params)
    return res
}

export const resetPassword = async (params) => {
    params.password = md5(md5(params.password).split('').reverse().join(''))
    let res = await $post('/resetUser',params)
    return res
}