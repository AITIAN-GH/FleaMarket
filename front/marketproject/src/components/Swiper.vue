<template>
    <el-carousel type="card" trigger="click" :height="props.height">
        <el-carousel-item v-for="(item, index) in props.list" :key="index">
            <img :style="{width:props.width}" :src="baseURL+item">
        </el-carousel-item>
    </el-carousel>
</template>

<script setup>
import { baseURL } from '@/config';
// 定义组件属性
const props = defineProps(['list','width','height'])
</script>

<style scoped lang="scss">
.el-carousel__item {
  opacity: 0.9;
  margin: 0 auto;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #eeeeee;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #eceff5;
}
</style>