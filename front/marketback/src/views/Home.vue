<template>
  <div>
    <div style="margin-bottom: 30px;">
      <div class="EChart">
        <div style="width: 300px;margin-left: 20px;margin-right: 10%;">
          <el-card  style="display: flex; justify-content: center;" shadow="always"><span style="display: flex; flex-direction: row;align-items: center;"><el-icon size="large" style="margin-right: 10px;"><StarFilled /></el-icon> {{ username }} </span></el-card>
          <el-card style="width: 440px;margin-top: 25px;" shadow="always">{{ poetry }}</el-card>
        </div>
        <EChartPerson></EChartPerson>
        <EChart></EChart>
      </div>
    </div>
    <el-card style="  background-image: linear-gradient(to bottom right, #EECFA1 , #6A5ACD);">
      AITIAN'S 后台管理系统
      <el-divider />
      <el-row :gutter="30">
        <el-col :span="12">
          <div style="padding: 20px 0; font-size: 20px">小白做毕设</div>
          <div>
            这是一款代码简单，结构清晰，功能强大，支持手机端和PC端的系统设计框架
          </div>
        </el-col>
        <el-col :span="12">
          <div style="padding: 20px 0; font-size: 20px">技术栈</div>
          <el-row >
            <el-col :span="12" style="line-height: 30px">
              <div><b>后端</b></div>
              <div>SpringBoot</div>
              <div>MybatisPlus</div>
              <div>Hutool</div>
              <div>Lombok</div>
              <div>Durid</div>
              <div>Poi</div>
            </el-col>
            <el-col :span="12" style="line-height: 30px">
              <b>前端</b>
              <div>Element-Plus</div>
              <div>Vue3+Vite</div>
              <div>Vue-Router</div>
              <div>Pinia</div>
              <div>ECharts</div>
              <div>Axios</div>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { userInfo } from '../store'
import EChart from '../components/EC.vue'
import EChartPerson from '../components/ECP.vue'
import axios from 'axios'

const userStore = userInfo()
const username = ref(userStore.user.username)
const poetry = ref('')

const getPoetry =  () => {
  let config = {
    method: 'get',
    url: 'https://v2.api-m.com/api/yiyan?type=poetry',
    timeout: 2000 // 将超时时间更改为 2 秒钟
  }
  axios(config).then(response => {
    if (response.data.data) {
      poetry.value = JSON.stringify(response.data.data)
    } else {
      poetry.value = '生命不息，奋斗不止 -- AITIAN'
    }
  }).catch(error => {
    console.log(error)
  })
}

onMounted(async () => {
  getPoetry()
})
</script>

<style scoped lang="scss">
.EChart{
  display: flex;
  flex-direction: row;
  height: 198px;
}
</style>