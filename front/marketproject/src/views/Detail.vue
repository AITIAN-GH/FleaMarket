<template>
    <div class="page">
        <div class="detail_header">
            <div class="content">
                <div class="name">{{ product.productName }}</div>
                <ul>
                    <li>概述</li>
                    <li>参数</li>
                    <li @click="openComment">评价</li>
                </ul>
            </div>
        </div>
        <div class="detail_content">
            <div class="swiper">
                <Swiper :list="product_imgs" width="520px" height="520px"></Swiper>
            </div>
            <div class="content">
                <h1 class="name">商品名: {{ product.productName }}</h1>
                <div class="author">提供者：{{ product.productAuthor }}</div>
                <div class="comments">
                    <el-link size="small" @click="openComment">
                        评 论: {{ commentCount }}
                    </el-link>
                </div>
                <div class="comments">
                    <span v-if="!product.stock > 0">已售罄</span>
                    <span v-else>库存：{{ product.stock }}</span>
                </div>
                <div class="price">
                    <span>现价: {{ product.delPrice }}￥</span>
                    <span class="del">原价: {{ product.proPrice }}￥</span>
                </div>
                <div class="pro-list">
                    <span class="pro-name">{{ product.productName }}</span>
                    <span class="pro-price">
                        <span>{{ product.proPrice }}￥</span>
                    </span>
                    <p class="price-sum">总计: {{ product.delPrice }}￥</p>
                </div>
                <div class="button">
                    <el-button  class="shop-cart" v-if="!product.stock > 0">来晚了哦</el-button>
                    <el-button class="shop-cart" @click="addcart" v-else>加入购物车</el-button>
                    <el-button class="like" @click="addCollection">喜欢</el-button>
                </div>
                <div class="pro-policy">
                    <li><el-icon>
                            <CircleCheckFilled />
                        </el-icon> AI自营</li>
                    <li><el-icon>
                            <CircleCheckFilled />
                        </el-icon> AI发货</li>
                    <li><el-icon>
                            <CircleCheckFilled />
                        </el-icon> 7天无理由退货</li>
                    <li><el-icon>
                            <CircleCheckFilled />
                        </el-icon> 7天价格保护</li>
                </div>
            </div>
        </div>
        <Comment ref="commentRef" @cct="changeCommentCount"></Comment>
    </div>
</template>

<script setup>
import Comment from '../components/Comment.vue'
import Swiper from '@/components/Swiper.vue'
import { ElNotification } from 'element-plus'
import { CircleCheckFilled } from '@element-plus/icons-vue'
import { $product_one,$checkcollect,$addcollect, $comment_one } from '@/api/product.js'
import { $shoppingcart_one, $shoppingcart_add } from '../api/shoppingcart.js'
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { userInfo, shoppingCartInfo } from '../store/index.js'

const userStore = userInfo()
const cartStore = shoppingCartInfo()
const route = useRoute()
const product = ref({})
const commentCount = ref(0)
const productId = ref(parseInt(route.query.id))

const product_imgs = ref([]) // 商品图片数组
const commentRef = ref(null)

// 加载商品
const loadProduct = () => {
    // 获取当前商品id
    $product_one({id: productId.value}).then(res =>{
        if (res.code == '200') {
            product.value = res.data
            product_imgs.value = []
            for (let index = 0; index < 3; index++) {
                product_imgs.value.push(res.data.classImg)
            }
        }
    }).catch(err => {
        console.log(err)
    })
    
}

// 加入购物车
const addcart = async () => {
    // 获取用户登录状态
    if (!userStore.user.id) {
        // 修改登陆界面状态
        userStore.setShowLander(true)
        return
    }
    let { data } = await $shoppingcart_one({
        userId: userStore.user.id,
        productId: productId.value
    })
    if (data.success) {
        ElNotification.success({
            title: '通知',
            message: '^_^ 已加入过了  !!',
            duration: 1200,
            showClose: false
        })
    }else{
        let { data } = await $shoppingcart_add({
            userId: userStore.user.id,
            productId: productId.value
        })
        if (data.success) {
            cartStore.loadShoppingcartList()
            ElNotification.success({
                title: '通知',
                message: '^_< 已成功加入  !!',
                duration: 1200,
                showClose: false,
            })
        }else {
            ElNotification.success({
                title: '通知',
                message: '加入失败 ! 请重试 !!',
                duration: 1200,
                showClose: false,
            })
        }
    }
}

// 加入收藏夹
const addCollection = async () => {
    // 获取用户登录状态
    if (!userStore.user.id) {
        // 修改登陆界面状态
        userStore.setShowLander(true)
        return
    }
    let { data } = await $checkcollect({
        userId: userStore.user.id,
        productId: productId.value
    })
    if (data.success) {
        ElNotification.success({
            title: '通知',
            message: '^_^ 已加入过了  !!',
            duration: 1200,
            showClose: false,
        })
    }else{
        let { data } = await $addcollect({
            userId: userStore.user.id,
            productId: parseInt(route.query.id) 
        })
        if (data.success) {
            cartStore.loadShoppingcartList()
            ElNotification.success({
                title: '通知',
                message: '^_< 已成功加入  !!',
                duration: 1200,
                showClose: false,
            })
        }else {
            ElNotification.success({
                title: '通知',
                message: '加入失败 ! 请重试 !!',
                duration: 1200,
                showClose: false,
            })
        }
    }
}

// 加载商品评论
const loadCommentCount = () => {
    $comment_one({
        productId: productId.value,
        pageIndex: 1,
        pageSize: 1
    }).then(res => {
        commentCount.value = res.data.total

    }).catch(err => {
        console.log(err)
    })
}

// 打开评论页
const openComment = () => {
    commentRef.value.visible = true
    commentRef.value.productId = productId.value
    commentRef.value.productName = product.value.productName
}

const changeCommentCount = (val) => {
    commentCount.value = val
}

watch(()=>route.query.id, (val)=>{
    if (val != 0) {
        productId.value = val
        loadCommentCount()
    }
},{immediate:true})

onMounted(() => {
    commentCount.value = 0
    // 确保页面已经完全加载完成
    nextTick(() => {
        loadProduct()
    });
})
</script>

<style lang="scss" scoped>
.page {
    background-color: white;
}
.detail_header {
    height: 32px;
    box-shadow: 0 5px 5px rgb(0 0 0 / 7%);
    .content {
        width: 1226px;
        margin: 0 auto;
        display: flex;
        justify-content: space-between;

        .name {
            font-size: 18px;
            font-weight: 400;
            color: #212121;
        }

        ul {
            display: flex;

            li {
                font-size: 14px;
                color: #616161;
                margin-left: 20px;
                cursor: pointer;

                &:hover {
                    color: #ff6700;
                }
            }
        }
    }
}

.detail_content {
    width: 1226px;
    margin: 0 auto;
    display: flex;
    padding-top: 4px;

    .swiper {
        width: 80%;
        height: 80%;
    }
    @media (max-width: 880px) {
        .swiper {
            width: 60%;
            height: 50%;
        }
    }

    .content {
        margin-left: 25px;
        width: 640px;

        .name {
            height: 30px;
            line-height: 30px;
            font-size: 24px;
            font-weight: 400;
            color: #212121;
        }

        .author {
            color: #b0b0b0;
            margin-top: 10px;
        }

        .comments {
            color: #ff6700;
            padding-top: 10px;
        }

        .price {
            display: block;
            font-size: 18px;
            color: #ff6700;
            border-bottom: 1px solid #e0e0e0;
            padding: 25px 0 25px;

            .del {
                font-size: 14px;
                margin-left: 10px;
                color: #b0b0b0;
                text-decoration: line-through;
            }
        }

        .pro-list {
            background: #f9f9fa;
            padding: 30px 60px;
            margin: 50px 0 50px;

            .pro-name {
                line-height: 30px;
                color: #616161;
            }

            .pro-price {
                float: right;
            }

            .price-sum {
                color: #ff6700;
                font-size: 24px;
                padding-top: 20px;
            }
        }

        .button {
            height: 55px;
            margin: 10px 0 20px 0;
            display: flex;

            .el-button {
                width: 340px;
                height: 55px;
                font-size: 16px;
                color: #fff;
                border: none;
                text-align: center;
            }

            .shop-cart {
                width: 340px;
                background-color: #ff6700;
            }

            .like {
                width: 260px;
                margin-left: 40px;
                background-color: #b0b0b0;
            }
        }

        .pro-policy {
            display: flex;

            li {
                margin-right: 20px;
                color: #b0b0b0;
            }
        }
    }
}
</style>
