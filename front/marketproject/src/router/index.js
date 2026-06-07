import { createRouter, createWebHistory } from 'vue-router'
import { start, close } from '@/plugins/nprogress'

// 路由数组
const routes = [
    { path: '/', name: 'home', meta: { title: '首页' }, component: () => import('../views/Home.vue') },
    { path: '/home', redirect: '/' },
    { path: '/index', redirect: '/' },
    {
        path: '/goods',
        name: 'goods',
        meta: { title: '全部' },
        component: () => import('@/views/Goods.vue'),
        children: []
    },
    { path: '/detail', name: 'detail', meta: { title: '商品详情' }, component: () => import('../views/Detail.vue') },
    { path: '/order', name: 'order', meta: { title: '未支付订单' }, component: () => import('../views/Order.vue') },
    { path: '/payed', name: 'payed', meta: { title: '已支付订单' }, component: () => import('../views/PayedOrder.vue') },
    { path: '/shoppingcart', name: 'shoppingcart', meta: { title: '购物车' }, component: () => import('../views/ShoppingCart.vue') },
    { path: '/collect', name: 'collect', meta: { title: '收藏' }, component: () => import('../views/Collect.vue') },
    { path: '/about', name: 'about', meta: { title: '关于' }, component: () => import('../views/About.vue') },
    { path: '/404', name: '404', meta: { title: '404' }, component: () => import('../views/404.vue') },
    // { path: '/:pathMatch(.*)', redirect: '/404' } // 任意其他路径匹配
]

// 创建路由容器
const router = createRouter({
    routes,  //路由规则
    history: createWebHistory(),  //历史模式
});

// 路由前置守卫
router.beforeEach((to, from, next) => {
    // 开启进度条
    start()
    let allRoutes = router.getRoutes()
    // 检查路由是否存在
    if (!allRoutes.length > 12 || to.matched.length === 0) {
        if (localStorage.getItem('allClassify')) {
            let storeMenus = JSON.parse(localStorage.getItem('allClassify'))
            if (storeMenus && storeMenus.length > 0) {
                const manageRoute = {
                    path: '/goods',
                    name: 'goods',
                    meta: { title: '全部' },
                    component: () => import('@/views/Goods.vue'),
                    children: storeMenus
                }
                // 动态添加到现在的路由对象中去
                router.addRoute(manageRoute)
            }
        }
        next(to.path)
    } else {
        next()
    }
})

// 路由后置守卫
router.afterEach((to, from) => {
    close() // 关闭进度条
    document.title = to.meta.title
})

// 导出路由对象
export default router