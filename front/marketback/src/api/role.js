import { $get, $put, $delete, $post } from '../utils/request.js'

export const getRoleList = (params) => {
    const ret = $get('/role/page', params)
    return ret
}

export const getPermissionsByRoleId = (params) => {
    const ret = $get('/role/'+params)
    return ret
}

export const getRoleNameList = (params) => {
    const ret = $get('/role/allNames', params)
    return ret
}


export const updateRole = (params) => {
    const ret = $put('/role/update', params)
    return ret
}

export const delRoleById = (param) => {
    const ret = $delete('/role/'+param)
    return ret
}

export const delRoleByIds = (params) => {
    const ret = $post('/role/del/batch',params)
    return ret
}