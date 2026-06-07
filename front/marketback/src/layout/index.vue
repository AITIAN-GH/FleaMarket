<template>
  <el-container style="min-height: 100vh">
    <el-aside :width="sideWidth + 'px'" style="box-shadow: 2px 0 6px rgba(2, 2, 2, 0.35);background-color: rgb(48, 65, 86);">
      <Aside ref="SiderRef" style="padding-bottom: 20px"/>
    </el-aside>
    
    <el-container>
      <el-header style="border-bottom: 1px solid #ccc;">
        <Header ref="HeaderRef" @asideCollapse="collapse()"></Header>
      </el-header>
      <el-main>
        <div class="main">
          <router-view></router-view>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>
  
<script setup>
import Aside from "@/components/Aside.vue"
import Header from "@/components/Header.vue"
import { ref, onMounted } from "vue"
import { user_checkToken } from '../api/user.js'
import { userInfo } from "../store"

const userStore = userInfo()
const SiderRef = ref(null)
const HeaderRef = ref(null)
const sideWidth = ref(200)
let isCollapse = false

const collapse = () => {  // 点击收缩按钮触发
  isCollapse = !isCollapse
  if (isCollapse) {  // 收缩
    HeaderRef.value.collapseBtnClass = 'CaretRight'
    SiderRef.value.isCollapse = true
    SiderRef.value.logoTextShow = false
    sideWidth.value = 64
    isCollapse = true
  } else {   // 展开
    HeaderRef.value.collapseBtnClass = 'CaretLeft'
    SiderRef.value.isCollapse = false
    SiderRef.value.logoTextShow = true
    sideWidth.value = 200
    isCollapse = false
  }
}


onMounted(async () => {
  if (localStorage.getItem('pinia-user')) {
    let localdata = JSON.parse(localStorage.getItem('pinia-user'))
    user_checkToken({
      id: localdata.user.uid,
      username: localdata.user.username
    }).then(res => {  // 检查是否有token 并且有效果 发出请求获取用户信息
      if (!res.data) {
        userStore.resetUser()
      }
    }).catch(err => {
      console.log(err)
    })
  }
})
</script>

<style scoped lang="scss">
.main {
  height: 100%;
  width: 100%;
  margin: 0 auto;
}
</style>

  
  