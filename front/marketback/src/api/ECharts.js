import { $get } from '../utils/request.js'

export const getchartData = async (params) => {
    let res = await $get('/EChartsData',params)
    return res
}

export const getChartsPersonData = async (params) => {
    let res = await $get('/EChartsPersonData',params)
    return res
}