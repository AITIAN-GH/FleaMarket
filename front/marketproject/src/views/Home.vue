<template>
    <div class="page">
        <!-- 轮播图 -->
        <div class="swiper">
            <div class="swiper_img">
                <swiper :list="carousel_list" width="290px" height="400px"></swiper>
            </div>
            <div class="swiper_content">
                <el-collapse>
                    <el-collapse-item title="欢迎!!">
                        <div>
                            欢迎使用本系统<br/>
                            <span style="margin-left: 72%;font-weight: bolder;">----  AITIAN</span>
                        </div>
                    </el-collapse-item>
                    <el-collapse-item :title="'公告'+item.id+'：'+item.title" v-for="item in notice_list" :key="item.id">
                        <div>
                            {{ item.content }}<br/>
                            <span style="margin-left: 34%;font-weight: bolder;">活动截止日期：{{ new Date(item.endTime).toLocaleString() }}</span>
                        </div>
                    </el-collapse-item>
                </el-collapse>
            </div>
        </div>
        <!-- 轮播图end -->
        <!-- 分类 -->
        <Types :types="book_list">
            <img class="img" src="../assets/cebianlan1.jpg">
            <img class="img" src="../assets/cebianlan2.jpg">
            <img class="img" src="../assets/cebianlan3.jpg">
        </Types>
        <!-- 分类end -->
    </div>
</template>

<script setup>
import { $carousel_list, $notice_list } from '../api/carousel' // 导入api方法
import { $product_search } from '../api/product' // 导入api方法
import { onMounted, ref } from 'vue' // 导入组合式api
import Swiper from '../components/Swiper.vue' // 导入轮播图组件
import Types from '../components/Types.vue'

const carousel_list = ref([])
//分类数据
const book_list = ref([]) 
const notice_list = ref([])

// 轮播图数据
const loadCarousel_list = async () => {
    let { data } = await $carousel_list()
    carousel_list.value =  data.map(r => r.imgPath )
}

// 直接使用Item组件加载商品信息
const loadProduct_list = async () => {
    let ret1 = await $product_search({
        pageIndex: 1,
        pageSize: 5
    })
    let ret2 = await $product_search({
        classify: 2,
        pageIndex: 1,
        pageSize: 4
    })

    book_list.value = {
        title: "欢迎来到本站 <(￣︶￣)↗[GO!]",
        datas:[
            {
                title:'精选一',
                data:ret1.data.productList
            },
            {
                title:'精选二',
                data: ret2.data.productList
            }
        ]
    }
}

// 加载公告列表
const loadNoticeList = () => {
    $notice_list().then(res => {
        // 只显示前前6条消息
        notice_list.value = res.data.records.slice(0, 6)
    }).catch(err => {
        console.log(err)
    })

} 

onMounted(() => {
    // 加载商品信息
    loadProduct_list(),
    // 加载轮播数据
    loadCarousel_list(),
    // 加载通告信息
    loadNoticeList()
})
</script>

<style scoped lang="scss">
.swiper{
    height: 440px;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    .swiper_img{
        width: 930px;
    }
    .swiper_content{
        margin-left: 10px;
        width: 400px;
        overflow: hidden;
    }
}
.img{
    width: 100%;
}
</style>
