import { $get, $delete, $post } from '@/utils/request'

// 搜索商品
export const $product_search = async (params) => {
    let ret = await $get('/product/search',params)
    return ret
}

// 通过id搜索单个商品
export const $product_one = async (params) => {
    let res = await $get('/product/one',params)
    return res
}

// 通过id搜索单个商品的评论
export const $comment_one = async (param) => {
    let res = await $get('/product/comment/one',param)
    return res
}

// 通过添加评论
export const $comment_add = async (param) => {
    let res = await $post('/product/comment',param)
    return res
}

// 通过添加评论
export const $com_received = async (param) => {
    let res = await $post('/product/comment/receive',param)
    return res
}

// 通过id搜索查询收藏的商品
export const $nameSearch = async (productName) => {
    let res = await $get('/product/name/'+productName)
    return res
}

// 搜索查询收藏的商品
export const $collect_search = async (params) => {
    let res = await $get('/product/collection',params)
    return res
}

// 删除收藏的商品
export const $delcollect = async (params) => {
    let res = await $delete('/product/collection/del',params)
    return res
}
// 判断是否已经收藏
export const $checkcollect = async (params) => {
    let res = await $post('/product/collection/one',params)
    return res
}
// 添加收藏商品
export const $addcollect = async (params) => {
    let res = await $post('/product/collection/add',params)
    return res
}