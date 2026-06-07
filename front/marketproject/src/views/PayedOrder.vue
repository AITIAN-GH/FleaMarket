<template>
    <div cLass="payed_order_page">
        <div v-if="orderList.length > 0">
            <div class="payed_oredr_title">
                <div class="cont">
                    <p>我的订单</p>
                </div>
            </div>
            <div class="payed_oredr_content" v-for="order in orderList" :key="order.orderid">
                <ul>
                    <li class="order">
                        <span class="orderid">订单编号: {{ order.orderid }}</span>
                        <span class="ordertime">订单时间: {{ order.time }}</span>
                    </li>
                    <li class="payed_oredr_header">
                        <div class="pro-img"></div>
                        <div class="pro-name">商品名称</div>
                        <div class="pro-price">单价</div>
                        <div class="pro-num">数量</div>
                        <div class="pro-total">小计</div>
                        <div class="pro-action"></div>
                    </li>
    
                    <li class="book-list" v-for="book in order.books" :key="book.id">
                        <div class="pro-img">
                            <img :src="baseURL+book.classimg" />
                        </div>
                        <div class="pro-name">{{ book.productName }}</div>
                        <div class="pro-price">{{ book.delPrice }}</div>
                        <div class="pro-num">
                            {{ book.bycount }}
                        </div>
                        <div class="pro-total pro-total-in">{{ book.bycount * book.delPrice }}元</div>
                    </li>
                </ul>
                <div style="height: 20px; background-color: rgb(245,245,245);"></div>
                <div cLass="payed_oredr_bar">
                    <div cLass="payed_oredr_bar-left">
                        <span cLass="payed_oredr_total">
                            共 <span class="payed_oredr_total-num">{{ order.count }}</span>件商品
                        </span>
                    </div>
                    <div cLass="payed_oredr_bar-right">
                        <span>
                            <span cLass="total-price-title">合计:</span>
                            <span class="total-price">{{ order.totalPrice }}元</span>
                        </span>
                        <button class="btn-primary">已支付</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="emtpy_payed" v-else>
            <img :src="emptyImg" @error.once="e => { e.target.src = defaultImage }" />
        </div>
    </div>
</template>

<script setup>
import defaultImage from '@/assets/empty_collect.jpg'
import { baseURL } from '@/config'
import { userInfo } from '@/store/index.js'
import { ElNotification } from 'element-plus'
import { onMounted, reactive, ref } from "vue"
import { $shoppingcart_getPayed } from '@/api/shoppingcart.js'

const userStore = userInfo()
// 订单数组
let orderList = reactive([])
const emptyImg = ref('https://picsum.photos/1218/434')

const loadUserPayedOrder = async () => {
    if (!localStorage.getItem('user')) {
        return;
    }
    let localUser = JSON.parse(localStorage.getItem('user'))
    let { data } = await $shoppingcart_getPayed({
        userId: localUser.id ? localUser.id : userStore.user.id,
    })
    data.forEach(r => {
        let index = orderList.findIndex((o) => o.orderid === r.orderCode )
        if (index > -1) {
            orderList[index].books.push(r)
            orderList[index].totalPrice += r.delPrice*r.bycount
            orderList[index].count = orderList[index].count + 1
        } else {
            orderList.push({
                orderid: r.orderCode,
                time: formattime(r.orderCode),
                books: [r],
                count: 1,
                totalPrice: r.delPrice * r.bycount
            })
        }
    })
}

let formattime = (time) => {
    let date = new Date(parseInt(time))
    let y = date.getFullYear()
    let m = date.getMonth()+1
    let d = date.getDate()
    let h = date.getHours()
    let mi = date.getMinutes()
    let s = date.getSeconds()
    return [y,m,d].map(r=>r>=10?r:'0'+r).join('-')+''+[h,mi,s].map(r=>r>=10?r:'0'+r).join(':')
}

onMounted(() => {
    nextTick(() => {
        loadUserPayedOrder()
    });
})

</script>

<style scoped lang="scss">
.payed_order_page{
    height: 448px;
    width: 1258px;
    overflow: auto;
    display: flex;
    justify-content: center;
    align-items: center;
    .payed_oredr_title {
        height: 64px;
        border-bottom: 1px solid #ff6700;
        background-color: #fff;
        margin-bottom: 20px;
    
        .cont {
            width: 1210px;
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
        }
    }
    .payed_oredr_content {
        height: 500px;
        padding-bottom: 20px;
        width: 1200px;
        margin: 0 auto;
        background-color: #fff;
    
        .order{
            height: 80px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 18px;
            padding: 0 20px;
            border-bottom: 1px solid #ff6700;
            .orderid{
                color: #ff6700;
            }
            .ordertime{
                color: #48acd0;
            }
    
        }
    
        .payed_oredr_header {
            height: 85px;
            padding-right: 26px;
            color: #424242;
            display: flex;
            align-items: center;
    
            .pro-img {
                width: 106px;
                height: 120px;
                margin-left: 20px;
            }
    
            .pro-action {
                width: 80px;
                text-align: center;
            }
            .pro-name {
                width: 200px;
                margin-left: 160px;
            }
    
            .pro-num {
                width: 150px;
                margin-left: 20px;
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
            padding: 15px 26px 15px 34px;
            border-top: 1px solid #e0e0e0;
            display: flex;
            align-items: center;
    
            .pro-img {
                width: 88px;
                margin-left: 74px;
                margin-right: 50px;
    
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
    
            .pro-name {
                width: 198px;
                margin-left: 44px;
            }
    
            .pro-num {
                width: 150px;
                margin-left: 20px;
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
    
        .payed_oredr_bar {
            width: 1198px;
            height: 60px;
            line-height: 50px;
            background-color: #fff;
            display: flex;
            justify-content: space-between;
            padding: 0 20px;
            margin: 0 20px;
            border-top: 1px solid #ff6700;
    
            .payed_oredr_bar-left {
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
    
                .payed_oredr_total {
                    color: #757575;
    
                    .payed_oredr_total-num {
                        color: #ff6706
                    }
                }
            }
    
            .payed_oredr_bar-right {
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
                    margin-left: 30px;
                    background: #8d9bda;
                    color: #fff;
                }
            }
        }
    }
    .emtpy_payed{
        img[src=""],img:not([src]){
            background: url('@/assets/empty_collect.jpg') no-repeat center;
            background-size: 100% 100%;
        }
    }
}
</style>
