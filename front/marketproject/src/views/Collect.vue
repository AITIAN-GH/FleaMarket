<template>
  <div class="empty_back">
      <div v-if="products.length == 0">
        <img :src="emptyImg" onerror="javascript:this.src='@/assets/empty_collect.jpg';this.οnerrοr=null;">
        <span style="font-weight: bolder;color: #bc9bfd; font-size: xx-large; position: absolute;top:286px;left: 650px;">
          没 有 发 现 收 藏
        </span>
      </div>
      <div v-else>
        <!-- 分类商品展示 -->
        <div class="products">
            <Item v-for="item in products" :key="item.id" :item="item" :close="true" @delete="delCollection"></Item>
        </div>

        <!-- 分类商品展示end -->
        <!-- 分页 -->
        <div class="pagination" v-if="total > 1">
            <el-pagination background layout="prev, pager, next" v-model:current-page="currentPageIndex"
                @current-change="loadCollectList" :total="total" :page-size="6" />
        </div>
        <!-- 分页end -->
      </div>
  </div>
</template>

<script setup>
import Item from '@/components/Item.vue'
import { $collect_search, $delcollect } from '../api/product'
import { onMounted, ref } from 'vue'
import { userInfo } from '../store'

const userStore = userInfo()
const products = ref([])
const total = ref(0)
const currentPageIndex = ref(1)
const emptyImg = ref('https://picsum.photos/1226/440')

// 加载商品方法
const loadCollectList = async () => {
  let { data } = await $collect_search({
      userId: userStore.user.id,
      pageIndex: currentPageIndex.value,
      pageSize: 8
  })
  products.value = data.productList
  total.value = data.total 
}

const delCollection = (pid) => {
  $delcollect({
    pid: pid,
    uid: userStore.user.id
  }).then(res =>{
    if (res.code == '200') {
      loadCollectList()
    }
  })
  .catch(err => {
    console.log(err)
  })
}

onMounted(() => {
  // 确保页面已经完全加载完成
  nextTick(() => {
    loadCollectList()
  });
})
</script>

<style scoped lang="scss">
.empty_back{
  margin-top: 0px;
  text-align: center;
  width: 1226px;
  height: 440px;
  overflow: hidden;

  img[src=""],img:not([src]){
    background: url('@/assets/empty_collect.jpg') no-repeat center;
    background-size:100% 100%;
  }
}
.products {
  width: 1240px;
  overflow: hidden;
  margin: 0 auto;
  margin-top: 14px;
  transform: translateX(-14px);
}
.pagination {
  width: 1226px;
  margin: 0 auto;
  margin-top: 10px;
  padding-bottom: 20px;
  display: flex;
  justify-content: center;
}
</style>

