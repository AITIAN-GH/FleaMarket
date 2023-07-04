import { defineStore } from 'pinia'
import { $shoppingcart_list } from '@/api/shoppingcart.js'
import { $user_news } from '../api/user.js'
import { ElNotification } from 'element-plus'

// 创建一个store管理高亮索引
export const userMenuActive = defineStore('menuActive', {
    // 定义状态
    state: () => {
        return {
            activeIndex: sessionStorage.getItem('menuActive') || '/',
            messageList: []
        }
    },
    actions: {
        // 设置高亮索引
        setActiveIndex(val) {
            // 缓存中存储高亮索引
            sessionStorage.setItem('menuActive', val)
            this.activeIndex = val
        },
        setMessage(val) {
            let temp = []
            temp.push(val)
            this.messageList = temp
        }
    }
})

// 创建一个store存储用户信息
export const userInfo = defineStore('user', {
    // 定义用户信息
    state: () => {
        return {
            // 是否显示用户界面
            showLander: false,
            user: {
                id: '',
                username: '',
                flag: 1,
            }
        }
    },
    actions: {
        // 设置用户信息
        setUser(val) {
            $user_news({userId: val.id}).then(res => {
                if (res.data.news) {
                    val.unReadCount = res.data.news.length
                } 
            }).catch(err => 
                console.log(err)
            ).finally(() => { 
                this.user = val
                localStorage.setItem('user', JSON.stringify(val))
            })
        },
        setShowLander(val) {
            this.showLander = val
        },
        resetUser() {
            this.user = {
                id: '',
                username: '',
                flag: 1,
            }
            localStorage.removeItem('user')
            localStorage.removeItem('before_token')
            localStorage.removeItem('messageList')
            localStorage.removeItem('BeforeTokenOverTime')
            sessionStorage.clear()
        }
    }
})

// 创建一个store存储购物车信息
export const shoppingCartInfo = defineStore('shoppingcart', {
    // 定义用户信息
    state: () => {
        return {
            cartcount: 0,
            shoppingcartList: []
        }
    },
    actions: {
        loadShoppingcartList() {
            let userStore = userInfo()
            // 未登录返回
            if (!userStore.user.id) return
            // 已登录
            $shoppingcart_list({id: userStore.user.id}).then(res => {
                let data = res.data
                let tmp = []
                if (res.code == '200') {
                    // 购物车商品数量
                    this.cartcount = data.count,
                    tmp = data.list.map(r => {
                        return {
                            ...r,
                            state: false
                        }
                    })
                    this.shoppingcartList = tmp
                }else{
                    ElNotification.warning({
                        title: '通知',
                        message: '查询出错了 !!',
                        duration: 1200,
                        showClose: false,
                    })
                }
            }).catch(err => 
                console.log(err)
            )  
        },
        //删除本地购物车数据
        delshoppingCart(cartid) {
            let index = this.shoppingcartList.find(r => r.cartId = cartid)
            this.shoppingcartList.splice(index, 1)
        },
        // 清空购物车
        resetCart() {
            this.cartcount = 0,
            this.shoppingcartList = []
        }
    }
})