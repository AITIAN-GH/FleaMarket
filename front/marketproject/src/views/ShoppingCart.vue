<template>
    <div cLass="page">
        <div class="cart_head">
            <div class="cont">
                <img src="@/assets/shoppingcart.png" />
                <p>我的购物车</p>
                <span>温馨提示：产品是否购买成功，以最终下单为准哦，请尽快结算</span>
            </div>
        </div>
        <div class="cart-content">
            <div class="real-content" v-if="cartCount !== 0 && cartList.length!==0">
                <ul>
                    <li class="cart-header">
                        <div class="pro-check">
                            <el-checkbox v-model="ckAll" label="全选" size="large" />
                        </div>
                        <div class="pro-img"></div>
                        <div class="pro-name">商品名称</div>
                        <div class="pro-price">单价</div>
                        <div class="pro-num">数量</div>
                        <div class="pro-total">小计</div>
                        <div class="pro-action">操作</div>
                    </li>
                    {{cartCount}}
                    <li class="book-list" v-for="(item, index) in cartList" :key="index">
                        <div class="pro-check">
                            <el-checkbox v-model="item.state" label="" size="large"  v-if="item.stock > 0" />
                        </div>
                        <div class="pro-img">
                            <img :src="baseURL + item.classimg" />
                        </div>
                        <div class="pro-name">{{ item.productName }}</div>
                        <div class="pro-price">{{ item.delPrice }}元</div>
                        <div class="pro-num" >
                            <el-input-number v-model="item.count" @change="changeNum(item.cartId, item.count)" :min="1"
                                :max="item.stock" size="small" v-if="item.stock > 0"/>
                            <span style="color: #ff6700;" v-else>已售罄</span>
                        </div>
                        <div class="pro-total pro-total-in">{{ item.delPrice * item.count }}</div>
                        <div class="pro-action">
                            <el-popconfirm width="185px" confirm-button-text="删除" cancel-button-text="取消" title="确定删除吗?"
                                @confirm="delshoppingcart(item.cartId)">
                                <template #reference>
                                    <el-icon size="18">
                                        <CircleCloseFilled />
                                    </el-icon>
                                </template>
                            </el-popconfirm>
                        </div>
                    </li>
                </ul>
            </div>

            <div class="emptycart" v-else>
                <img :src="emptyImg" onerror="javascript:this.src='@/assets/emptycart.png';this.οnerrοr=null;">
                <span
                    style="font-weight: bolder;color: #bc9bfd; font-size: xx-large; position: absolute;top:350px;left: 680px;">
                    这 里 啥 都 没 有
                </span>
            </div>
            <div style="height: 20px; background-color: rgb(245,245,245);"></div>
            <div cLass="cart-bar">
                <div cLass="cart-bar-left">
                    <span>
                        <router-Link to="/goods">继续购物</router-link>
                    </span>
                    <span cLass="sep">|</span>
                    <span cLass="cart-total">
                        共 <span class="cart-total-num">{{ totalNum }}</span>件商品，已选择<span class="cart-total-num">{{
                            totalSelected }}</span>件
                    </span>
                </div>
                <div cLass="cart-bar-right">
                    <span>
                        <span cLass="total-price-title">合计:</span>
                        <span class="total-price">{{ totalPrice }}元</span>
                    </span>
                    <el-popconfirm width="185px" confirm-button-text="买单" cancel-button-text="再看看" title="买单?"
                        @confirm="buy">
                        <template #reference>
                            <button class="btn-primary">去结算</button>
                        </template>
                    </el-popconfirm>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { computed, ref, watch } from "vue"
import { baseURL } from '@/config'
import { useRouter, } from 'vue-router'
import { ElNotification } from 'element-plus'
import { shoppingCartInfo, userInfo } from '@/store/index.js'
import { CircleCloseFilled } from "@element-plus/icons-vue"
import { $shoppingcart_del, $shoppingcart_addorder, $shoppingcart_up } from '../api/shoppingcart.js'

// 获取购物车全局状态
const cartStore = shoppingCartInfo()
const userStore = userInfo()
const cartList = ref(cartStore.shoppingcartList)
const cartCount = ref(cartStore.cartcount)

watch(()=> cartStore.shoppingcartList,(val)=>{
    cartList.value = val
},{immediate:true})

watch(()=> cartStore.cartCount,(val)=>{
    cartCount.value = val
},{immediate:true})

const router = useRouter()
const emptyImg = ref('https://picsum.photos/1226/420')

// 修改数量
let changeNum = async (id, count) => {
    let { data } = await $shoppingcart_up({
        cartId: id,
        count: count
    })
    if (!data) {
        ElNotification.warning({
            title: '通知',
            message: '更改失败 !!',
            duration: 1200,
            showClose: false,
        })
        return
    }
}

// 总数量
let totalNum = computed(() => {
    return cartStore.shoppingcartList.length
})

// 选中数量
let totalSelected = computed(() => {
    return cartStore.shoppingcartList.filter(r => r.state).length
})

// 总价
let totalPrice = computed(() => {
    let prices = cartStore.shoppingcartList.filter(r => r.state).map(r => r.delPrice)
    let counts = cartStore.shoppingcartList.filter(r => r.state).map(r => r.count)
    let ret = 0
    if (prices != null && prices != '') {
        for (let index = 0; index < prices.length; index++) {
            ret += prices[index] * counts[index]
        }
    }
    return ret
})

// 全选
let ckAll = computed({
    get() {
        return cartStore.shoppingcartList.every(r => r.state)
    },
    set(val) {
        cartStore.shoppingcartList.forEach(r => r.state = val);
    }
})

// 删除
const delshoppingcart = async (id) => {
    cartStore.delshoppingCart({ cartid: id })
    let { data } = await $shoppingcart_del({ id })
    if (data) {
        cartStore.loadShoppingcartList()
        ElNotification.success({
            title: '通知',
            message: '^_^ 成功删除  !!',
            duration: 1200,
            showClose: false,
        })
    } else {
        ElNotification.success({
            title: '通知',
            message: '删除失败 !!',
            duration: 1200,
            showClose: false,
        })
    }
}

// 结算
const buy = () => {
    let cartIds = cartList.value.filter(r => r.state).map(r => r.cartId)
    if (cartIds == '' || cartIds.length == 0) {
        ElNotification.warning({
            title: '通知',
            message: '未选择购买商品 !!',
            duration: 1200,
            showClose: false,
        })
        return
    }
    $shoppingcart_addorder({
        userId: userStore.user.id,
        cartIds: cartIds
    }).then(res => {
        if (res.data) {
            router.push('/order')
        } else {
            ElNotification.warning({
                title: '通知',
                message: '出错了！请重试 !!',
                duration: 1200,
                showClose: false,
            })
            return
        }
    }).catch(err => {
        console.log(err);
    })
    cartStore.loadShoppingcartList()
}

const loadUserCartList = () => {
    cartStore.loadShoppingcartList()
}


onMounted(() => {
    nextTick(() => {
        loadUserCartList()
    });
})
</script>

<style scoped lang="scss">
.cart_head {
    height: 64px;
    border-bottom: 1px solid #ff6700;
    background-color: #fff;
    margin-bottom: 20px;

    .cont {
        width: 1226px;
        margin: 0 auto;
        display: flex;

        p {
            margin-left: 20px;
            font-size: 28px;
            line-height: 58px;
            display: flex;
            align-items: center;
            font-weight: 400;
            color: #424242;
        }

        span {
            color: #757575;
            font-size: 12px;
            height: 20px;
            line-height: 20px;
            margin-top: 30px;
            margin-left: 15px;
        }
    }
}

.cart-content {
    width: 1226px;
    height: 500px;
    margin: 0 auto;
    background-color: #fff;
    overflow: hidden;

    .real-content{
        height: 400px;
        overflow: auto; /* 允许滚动 */
    }

    .emptycart {
        height: 400px;
        text-align: center;

        img[src=""],img:not([src]) {
            background: url('@/assets/emptycart.png') no-repeat center;
            background-size: 100% 100%;
        }
    }

    .cart-header {
        height: 85px;
        padding-right: 26px;
        color: #424242;
        display: flex;
        align-items: center;

        .pro-img {
            width: 58px;
            height: 120px;
        }

        .pro-action {
            width: 80px;
            text-align: center;
        }

        .pro-check {
            width: 140px;
            text-align: center;
        }

        .pro-name {
            width: 380px;
        }

        .pro-num {
            width: 150px;
            text-align: center;
        }

        .pro-total {
            width: 200px;
            padding-right: 81px;
            text-align: right;
        }

        .pro-price {
            width: 140px;
            padding-right: 18px;
            text-align: center;
        }

    }

    .book-list {
        height: 116px;
        padding: 15px 26px 15px 0;
        border-top: 1px solid #e0e0e0;
        display: flex;
        align-items: center;

        .pro-img {
            width: 85px;

            img {
                width: 80px;
                height: 80px;
            }
        }

        .pro-total-in {
            color: #ff6700;
        }

        .pro-action {
            width: 80px;
            text-align: center;

            &:hover {
                color: #ff6700;
            }
        }

        .pro-check {
            width: 110px;
            text-align: center;
        }

        .pro-name {
            width: 380px;
        }

        .pro-num {
            width: 150px;
            text-align: center;
        }

        .pro-total {
            width: 200px;
            padding-right: 81px;
            text-align: right;
        }

        .pro-price {
            width: 140px;
            padding-right: 18px;
            text-align: center;
        }
    }


    .cart-bar {
        width: 1226px;
        height: 50px;
        line-height: 50px;
        background-color: #fff;
        display: flex;
        justify-content: space-between;


        .cart-bar-left {
            padding-left: 20px;

            a {
                line-height: 50px;
                margin-left: 32px;
                color: #757575;

                &:hover {
                    color: #ff6700;
                }
            }

            .sep {
                color: #eee;
                margin: 0 20px;
            }

            .cart-total {
                color: #757575;

                .cart-total-num {
                    color: #ff6706
                }
            }
        }

        .cart-bar-right {
            .total-price-title {
                color: #ff6700;
                font-size: 14px;
            }

            .total-price {
                color: #ff6700;
                font-size: 30px;
            }

            .btn-primary {
                border: none;
                float: right;
                width: 200px;
                height: 50px;
                text-align: center;
                font-size: 18px;
                margin-left: 50px;
                background: #ff6700;
                color: #fff;
            }
        }
    }
}</style>
