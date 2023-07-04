import { $get } from '@/utils/request'

// 搜索商品
export const $category_all = async () => {
    let ret = await $get('/classify/all')
    return ret
}