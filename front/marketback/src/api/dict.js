import { $get, $put, $delete, $post } from '../utils/request'

export const getAllDicts = async (params) => {
    let ret  = $get('/dict/page', params)
    return ret
}

export const updateDict = async (params) => {
    let ret  = $put('/dict/update', params)
    return ret
}

export const delDictById = async (param) => {
    let ret = await $delete('/dict/'+param)
    return ret
}

export const delDictByIds = (params) => {
    const ret = $post('/dict/del/batch',params)
    return ret
}

export const loadIcons = async (params) => { 
    let ret  = $get('/dict', params)
    return ret
}

export const getSystemLog = async (params) => { 
    let ret  = $get('/file/systemlog', params)
    return ret
}
