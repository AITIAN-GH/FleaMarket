import { $delete, $get, $put, $post } from '../utils/request.js'

export const getClassifyData = async (params) => {
    let res = await $get('/classify/page',params)
    return res
}

export const updateClassify = async (params) => {
    let res = await $post('/classify',params)
    return res
}

export const deleteClassify = async (param) => {
    let res = await $delete('/classify/'+param)
    return res
}

export const deleteBathClassify = async (params) => {
    let res = await $post('/classify/del/batch',params)
    return res
}
