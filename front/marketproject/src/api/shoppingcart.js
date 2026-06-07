import { $post, $put, $delete } from '@/utils/request'

// 购物车添加
export const $shoppingcart_add = async (params) => {
    let ret = await $post('/cart/add',params)
    return ret
}
// 购物车删除
export const $shoppingcart_del = async (params) => {
    
    let ret = await $delete('/cart/del',params)
    return ret
}
// 购物车更新
export const $shoppingcart_up =  async (params) => {
    let ret = await $put('/cart/up',params)
    return ret
}
// 获取单个购物车
export const $shoppingcart_one =  async (params) => { 
    let ret = await $post('/cart/one',params)
    return ret
}
// 购物车查询
export const $shoppingcart_list =  async (params) => { 
    let ret = await $post('/cart/list',params)
    return ret
}

// 添加订单
export const $shoppingcart_addorder = async (params) => {
    let ret = await $post('/cart/addOrder', params)
    return ret
}

// 添加订单
export const $shoppingcart_cancleorder = async (params) => {
    let ret = await $post('/cart/cancelOrder', params)
    return ret
}

// 获取订单
export const $shoppingcart_getorder = async (params) => { 
    let ret = await $post('/cart/getOrder', params)
    return ret
}

// 获取订单
export const $shoppingcart_getPayed = async (params) => {
    let ret = await $post('/cart/getPayed', params)
    return ret
}