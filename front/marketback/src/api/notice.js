import { $get, $post ,$put } from '@/utils/request'

export const getAllNotice = async (params) => {
    let ret = await $get('/notice/list',params)
    return ret
}

export const updateNotice = async (params) => {
    let ret = await $put('/notice', params)
    return ret
}

export const delNotice = async (params) => {
    let ret = $post('/notice/del',params)
    return ret
}
