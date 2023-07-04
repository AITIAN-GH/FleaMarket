import { $get } from '@/utils/request'

// 获取轮播图
export const $carousel_list = async () => {
    let ret = await $get('/carousel')
    return ret
}

// 获取通告信息
export const $notice_list = async () => {
    let ret = await $get('/notice/all')
    return ret
}