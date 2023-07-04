import { $delete, $get, $post } from '../utils/request'

export const getAllPermissions = async (params) => {
    let ret  = $get('/permission', params)
    return ret
}

export const getPagePermissions = async (params) => {
    let ret  = $get('/permission/page', params)
    return ret
}

export const saveRolePermissions = async (params) => {
    let ret  = $post('/permission/saveRoleMenu', params)
    return ret
}

export const savePermissioin = async (params) => {
    let ret  = $post('/permission', params)
    return ret
}

export const delPermissioins = async (params) => {
    let ret  = $post('/permission/del/batch', params)
    return ret
}

export const delOnePermissioin = async (param) => {
    let ret  = $delete('/permission/'+ param)
    return ret
}



