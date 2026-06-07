import { defineStore } from 'pinia'
import router,{ setRoutes, resetRouter } from "@/router"

// 创建一个store管理高亮索引
export const userPageActive = defineStore('currActive', {
    // 定义状态
    state: () => {
        return {
            currentProductId: sessionStorage.getItem('currentProductId') || 0,
            currentProductName: sessionStorage.getItem('currentProductName') || '',
        }
    },
    actions: {
        setPageId(val){
            sessionStorage.setItem('currentProductId', val)
            this.currentProductId = val
        },
        setProductName(val){
            sessionStorage.setItem('currentProductName', val)
            this.currentProductName = val
        }
    }
})

// 创建一个store存储用户信息
export const userInfo = defineStore('user', {
    // 定义用户信息
    state: () => {
        return {
            user: {
                id: 0,
                uid: '',
                username: '',
                email: '',
                address: '',
                avatarUrl: '',
                sign: '',
                auths: [],
                routerList: []
            }
        }
    },
    actions: {
        // 设置用户信息
        setUser(val) {
            this.user = val
            setRoutes(val.routerList)
        },
        resetUser() {
            // 清空用户信息
            this.user = {
                id: 0,
                uid: '',
                username: '',
                email: '',
                address: '',
                avatarUrl: '',
                sign: '',
                auths: [],
                routerList: []
            }
            // 清空本地存储空间信息
            sessionStorage.clear()
            localStorage.removeItem('menus')
            localStorage.removeItem('pinia-user')
            localStorage.removeItem('BackTokenOverTime')
            resetRouter()
            document.cookie.split(";").forEach((c) => {
                document.cookie = c.replace(/^ +/, "").replace(/=.*/, "=;expires=" + new Date().toUTCString() + ";path=/")
            })
            router.push('/login')
        }
    }
}
,{
    // pinia 持久化 可以自定义配置存储的地方，这是默认储存
    persist: true
})