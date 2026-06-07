import { createRouter, createWebHistory } from 'vue-router'
import { userInfo } from '../store'
const modules = import.meta.glob('../views/*.vue')
import { user_checkToken } from '../api/user.js'

// 路由数组
const routes = [
    {
        path: '/login',
        name: 'Login',
        meta: { title: '登录' },
        component: () => import('@/views/Login.vue')
    },
    {
        path: '/register',
        name: 'Register',
        meta: { title: '注册' },
        component: () => import('@/views/Register.vue')
    },
    {
        path: '/resetUser',
        name: 'ResetUser',
        meta: { title: '重置密码' },
        component: () => import('@/views/ResetUser.vue')
    },
    {
        path: '/404',
        name: '404',
        meta: { title: '404' },
        component: () => import('@/views/404.vue')
    },
]

// 创建路由容器
const router = createRouter({
    routes,
    history: createWebHistory()
})

// 菜单列表转换成路由配置对象
export const setRoutes = (menuList) => {
    
    // 从pinia中获取当前用户的路由信息
    const userStore = userInfo()
    const storeMenus = userStore.user.routerList
    let userAuths = []
    if (storeMenus && storeMenus.length > 0) {
        // 获取当前的路由对象名称数组
        const currentRouteNames = router.getRoutes().map(v => v.name)
        if (!currentRouteNames.includes('HOME')) {
            // 拼装动态路由
            const manageRoute = {
                path: '/', 
                name: 'HOME',
                meta: {title: 'HOME'}, 
                component: () => import('../layout/index.vue'), 
                redirect: "/home", 
                children: [
                    { path: '/password', name: '密码', component: modules['../views/Password.vue']},
                    { path: '/person', name: '个人', component: modules['../views/Person.vue']}       
                ]
            }
            storeMenus.forEach(item => {
                if(item.auth != ''){
                    userAuths.push(item.auth)
                }
                if (item.path != '') {  // 当且仅当path不为空的时候才去设置路由
                    let itemMenu = { 
                        path: item.path, 
                        name: item.name,
                        meta: { title: item.name}, 
                        component: modules['../views/'+item.page+'.vue'],
                        children: []
                    }
                    manageRoute.children.push(itemMenu)
                    if (item.children) {
                        item.children.forEach(item => {
                            if(item.auth != ''){
                                userAuths.push(item.auth)
                            }
                        })
                    }
                } else if (item.children) {
                    item.children.forEach(item => {
                        if(item.auth != ''){
                            userAuths.push(item.auth)
                        }
                        if (item.path != '') {
                            let itemMenu = { 
                                path: item.path, 
                                name: item.name,
                                meta: { title: item.name},  
                                component: modules['../views/'+item.page+'.vue'],
                            }
                            manageRoute.children.push(itemMenu)
                        }
                    })
                }
            })
            userStore.user.auths = userAuths
            // 动态添加到现在的路由对象中去
            router.addRoute(manageRoute)
        }
    }
}

export const resetRouter = () => {
    router.matcher = createRouter({
        routes,
        history: createWebHistory()
    }).matcher
}

// 路由前置守卫
router.beforeEach((to, from, next) => {
    
    const userStore = userInfo()
    
    if (localStorage.getItem('pinia-user') != null && userStore.user.username !='') {
        let allRoutes = router.getRoutes()
        if (allRoutes.length > 4) {    // 大于4 是因为项目除了默认包含登录路由 还默认内置了其他路由
            if (to.matched.length === 0 && to.path != '/404') {
                // 判断此跳转路由的来源路由是否存在，存在的情况跳转到来源路由，否则跳转到404页面
                if (localStorage.getItem('pinia-user') != null) {
                    let piniauser = JSON.parse(localStorage.getItem('pinia-user'))
                    user_checkToken({
                        id: piniauser.user.uid,
                        username: piniauser.user.username
                    }).then(res => {  // 检查是否有token 并且有效果 发出请求获取用户信息
                        if (!res.data) {
                            userStore.resetUser()
                            next({ path: '/login' })
                            return
                        }
                        next({ path: '/404', replace: true })
                    })
                }
            } else {
                next()
            }
        } else {
            setRoutes()
            next({
                path: to.path,
                replace: true,
            })
        }
    } else {
        if (to.path === "/login") {
            next()
        } else if (to.path === "/register") {
            next()
        } else if (to.path === "/resetUser") {
            next()
        } else {
            next({ path: '/login' })
        }
    }   
})

// 路由后置守卫
router.afterEach((to, from) => {
    let state,timer, visibilityChange;
    if (to.path == "/login") {
        sessionStorage.clear()
        localStorage.clear()
    } 
    
    // 判断用户是否观看当前页面，浏览器兼容
    if (typeof document.hidden !== 'undefined') {
        visibilityChange = 'visibilitychange';
        state = 'visibilityState';
    } else if (typeof document.mozHidden !== 'undefined') {
        visibilityChange = 'mozvisibilitychange';
        state = 'mozVisibilityState';
    } else if (typeof document.msHidden !== 'undefined') {
        visibilityChange = 'msvisibilitychange';
        state = 'msVisibilityState';
    } else if (typeof document.webkitHidden !== 'undefined') {
        visibilityChange = 'webkitvisibilitychange';
        state = 'webkitVisibilityState';
    }
    // 具体方法
    document.addEventListener('visibilitychange', () => {
        // 用户息屏、或者切到后台运行 （离开页面） 
        if (document.visibilityState === 'hidden') {
            clearTimeout(timer)
            const arr = ['ヾ(￣▽￣)Bye~Bye~','=͟͟͞͞(꒪⌓꒪*)你咋走了','(,,•́ . •̀,,)记得回来','(づ ●─● )づ你别走呀','(ง •̀_•́)ง我在这里']
            document.title = arr[Math.ceil(Math.random() * 4)]
        }
        // 用户打开或回到页面 
        if (document.visibilityState === 'visible') {
            clearTimeout(timer)
            const arr = ['⁄(⁄ ⁄•⁄ω⁄•⁄ ⁄)⁄你回来辣','(๑>؂<๑)你回来辣','(づ ●─● )づ你回来辣','(ง •̀_•́)ง你回来辣']
            if (to.meta.title) {
                document.title = to.meta.title
            }else{
                document.title = arr[Math.ceil(Math.random() * 3)]
            }
        }
    })
    document.title = to.meta.title
})

// 导出路由对象
export default router