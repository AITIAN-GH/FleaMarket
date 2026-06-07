<template>
  <div style="line-height: 60px; display: flex">
    <div style="flex: 1;">
      <span style="cursor: pointer; font-size: 18px" @click="collapse">
        <el-icon>
          <component :is="collapseBtnClass" />
        </el-icon>
      </span>
      <el-breadcrumb class="header_breadcrumb">
        <el-breadcrumb-item v-if="breadcrumbList == ''"><span class="item">首页</span></el-breadcrumb-item>
        <el-breadcrumb-item v-for="curr in breadcrumbList" :key="curr.id" v-else><span class="item">{{ curr.text }}</span></el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-dropdown style="width: 84px; cursor: pointer; text-align: right">
      <el-avatar :size="48" :src="baseURL+avatarUrl" style="margin-top: 6px" />
      <template #dropdown>
        <el-dropdown-menu style="width: 100px; text-align: center">
          <el-dropdown-item>
            <div @click="router.push('/person')">个人信息</div>
          </el-dropdown-item>
          <el-dropdown-item>
            <div @click="router.push('/password')">修改密码</div>
          </el-dropdown-item>
          <el-dropdown-item>
            <div @click="logout">退出登录</div>
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script setup>
import { baseURL } from '../config'
import { ref, watch } from 'vue'
import { userInfo } from '../store'
import router from '../router'
import { user_logout } from "../api/user.js";

const userStore = userInfo()
const avatarUrl = ref(userStore.user.avatarUrl)
// 导航面包屑
const breadcrumbList = ref([])
// 折叠图标
const collapseBtnClass = ref('CaretLeft')

// 动态监听路由变化
watch(() =>router.currentRoute.value, (currentFullPath) => {
  let items = currentFullPath.matched.filter(r => r.name != 'HOME').map(record => {
    return {
      text: record.meta.title, 
    }
  })
  breadcrumbList.value = items
})
watch(() =>userStore.user.avatarUrl, (newVal) => {
  avatarUrl.value = newVal
},{
  immediate: true, // 只在初始化时执行一次，以便在avataraUrl变化时执行以下操
})

defineExpose({
  collapseBtnClass
})

const $emit = defineEmits(['asideCollapse'])

const collapse = () => {
  $emit('asideCollapse')
}

const logout = () => {

  // 清除后端登录信息
  user_logout({
    userUid: userStore.user.uid
  }).then(ret =>{
    if (ret.code == '200') {
        // 清空信息
      userStore.resetUser()
    }else{
      ElNotification.warning({
        title: '通知',
        message: '后台出错了 !!',
        duration: 1200,
        showClose: false,
      })
    }
  }).catch((err) => {
    console.log(err)
    ElNotification.error({
      title: '通知',
      message: '出错了 !!',
      duration: 1200,
      showClose: false,
    })
  })
}

</script>
<style lang="scss" scoped>
.header_breadcrumb{
  display: inline-block; 
  margin-left: 10px;
  .item{
    font-weight: 800;
  }
}
</style>