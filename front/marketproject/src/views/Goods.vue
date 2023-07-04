<template>
    <div class="page">
        <div class="breadcrumb">
            <div class="content">
                <el-breadcrumb :separator-icon="ArrowRight">
                    <el-breadcrumb-item :to="{ path: '/goods' }">商品</el-breadcrumb-item>
                    <el-breadcrumb-item v-for="(item, index) in paths" :key="index">{{ item.meta.title }}</el-breadcrumb-item>
                </el-breadcrumb>
                <div class="nav">
                    <div class="title">分类</div>
                    <el-tabs type="card" class="tabs" tab-position="bottom" v-model="activeName" @tab-change="tabChange()">
                        <el-tab-pane v-for="item in categorys" :key="item.bid" :name="item.bid + item.path"
                        :label="item.classify"></el-tab-pane>
                    </el-tabs>
                    <div class="search">
                        <el-input v-model="searchText" placeholder="请输入搜索内容">
                            <template #append>
                            <el-button :icon="Search" @click="nameSearch" />
                            </template>
                        </el-input>
                    </div>
                </div>
            </div>
        </div>
        <!-- 分类商品展示 -->
        <div class="products">
            <Item v-for="item in products" :key="item.id" :item="item" :close="false" delete="a"></Item>
        </div>
        <!-- 分类商品展示end -->
        <!-- 分页 -->
        <div class="pagination" v-if="total > 1">
            <el-pagination background layout="prev, pager, next" v-model:current-page="currentPageIndex"
                @current-change="loadProduct_list" :total="total" :page-size="8" />
        </div>
        <!-- 分页end -->
    </div>
</template>

<script setup>
import Item from '@/components/Item.vue'
import { ArrowRight,Search } from '@element-plus/icons-vue'
import { $category_all } from '../api/category.js'
import { $product_search,$nameSearch } from '../api/product'
import { onMounted, ref, computed, watch } from 'vue'; // 导入组合式api
import { useRoute } from 'vue-router'
import router from '../router'


const searchText = ref('')
const categorys = ref([])
const products = ref([])
// 当前分类 v-model="activeName"使其高亮
const activeName = ref(0)
const total = ref(0)
const route = useRoute()
let itempath = ref('')
let paths = ref([])
let v1 = ''

const currentPageIndex = ref(1)
const pageSize = ref(8)


// 加载所有分类
const loadCategorys = async () => {
    // 根据后台数据生成路由数组
    let newRoutes = []
    $category_all().then(res => {
        categorys.value = res.data
        let tmp = res.data
        tmp.forEach(elem => {
            newRoutes.push({
                path: elem.path,
                name: elem.path.split('/')[1],
                meta: { title: elem.classify },
                component: {render: (e) => e("router-view")}
            })
        })
        // 替换原来的路由数组
        router.removeRoute('goods')
        router.addRoute('goods', {
            path: '/goods',
            name: 'goods',
            meta: { title: '全部' },
            component: () => import('@/views/Goods.vue'),
            children: newRoutes
        })
        localStorage.setItem('allClassify', JSON.stringify(newRoutes))
        loadProduct_list()
    }).catch(err => {
        console.log(err);
    })
}

// 加载商品方法
const loadProduct_list = async () => {
    let strs  = activeName.value ? activeName.value.toString().split('/') : []

    if (strs != '') {
        v1 = strs[0]
        itempath = '/' + strs[1]
    } else {
        v1 = 1
    }
    router.push(itempath).then(() => {
        paths.value = computed(() => {
            return route.matched
        }).value
    })
    let { data } = await $product_search({
        classify: v1,
        pageIndex: currentPageIndex.value,
        pageSize: pageSize.value
    })
    products.value = data.productList
    total.value = data.total 
}

// 分类切换
const tabChange = () => {
    currentPageIndex.value = 1
    loadProduct_list()
} 

// 通过名称搜索商品
const nameSearch = () => {
  if (!searchText.value=='') {
    $nameSearch(searchText.value.trim()).then(ret => {
        products.value = ret.data.productList
        total.value = 0
    })
  }
}

onMounted(() => {
    // 确保页面已经完全加载完成
    nextTick(() => {
        loadCategorys()
    });
})
</script>

<style scoped lang="scss">

.breadcrumb {
    background-color: white;

    .content {
        width: 1226px;
        margin: 0 auto;
        .nav {
            margin-top: 10px;
            display: flex;
            align-items: flex-end;

            .title {
                font-weight: bold;
                font-size: 16px;
                margin-right: 20px;
                padding-bottom: 10px;
            }

            .tabs {
                flex: 1;
            }
            .search {
                margin-left: auto;
                width: 300px;
            }
        }
    }
}

.products {
    width: 1240px;
    height: 648px;
    overflow: hidden;
    margin: 0 auto;
    margin-top: 16px;
    transform: translateX(-14px);
}

.pagination {
    width: 1226px;
    margin: 0 auto;
    margin-top: 6px;
    padding-bottom: 20px;
    display: flex;
    justify-content: center;
}
</style>
