import { $get, $post ,$put } from '@/utils/request'

export const getAllGoods = async (params) => {
    let ret = await $get('/product/all/',params)
    return ret
}

export const updateUserProduct = async (params) => {
    let ret = await $put('/product/update', params)
    return ret
}

export const delProductByIds = async (params) => {
    let ret = $post('/product/del',params)
    return ret
}

export const getClassifyList = async (params) => {
    let ret = $get('/classify',params)
    return ret
}

export const getOneComment = async (params) => {
    let ret = $get('/product/comment/one',params)
    return ret
}

export const addComment = async (params) => {
    let ret = $post('/product/comment',params)
    return ret
}

export const delComment = async (params) => {
    let ret = $post('/product/comment/del',params)
    return ret
}