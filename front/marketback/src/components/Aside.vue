<template>
  <div class="asider_container">
    <el-menu :default-openeds="opens" style="min-height: 100%; overflow-x: hidden"
        background-color="rgb(48, 65, 86)"
        text-color="#fff"
        active-text-color="#ffd04b"
        :collapse-transition="false"
        :collapse="isCollapse"
        router>
      <div class="aside_title">
        <p class="title_content" v-if="logoTextShow">AITIAN</p>
        <p class="title_content" v-else>AT</p>
      </div>
      <template v-for="item in menus" :key="item.id">
        <template v-if="item.path">
          <el-menu-item :index="'/'+item.path">
            <component :is="item.icon" style="width: 20px;height: 20px;margin-right: 5px"></component>
            <template #title>
              <span>{{ item.name }}</span>
            </template>
          </el-menu-item>
        </template>
        <template v-else>
          <el-sub-menu :index="'/'+item.id">
            <template #title>
              <el-icon>
                <component :is="item.icon" style="width: 20px;height: 20px;margin-right: 5px"></component>
              </el-icon> 
              <span>{{ item.name }}</span>
            </template>
            <template v-for="subItem in item.children" :key="subItem.id">
              <el-menu-item :index="'/'+subItem.path">
                <component :is="subItem.icon" style="width: 20px;height: 20px;margin-right: 5px"></component>
                <template #title>
                  <span>{{ subItem.name }}</span>
                </template>
              </el-menu-item>
            </template>
          </el-sub-menu>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script setup>
import { defineExpose, ref, onMounted } from 'vue'
import { userInfo } from '../store';

const userStore = userInfo()
const menus = ref(userStore.user.routerList)
const opens = ref([])

const isCollapse = ref(false)
const logoTextShow = ref(true)

defineExpose({
  isCollapse,
  logoTextShow
})

onMounted(() => {
  if (localStorage.getItem('menus')) {
    menus.value = JSON.parse(localStorage.getItem('menus'))
  }
})

</script>

<style lang="scss" scoped>
.asider_container{

  .aside_title {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
  
    .title_content {
      margin-left: 0 auto;
      color: white; 
      font-size: larger;
      font-weight: bolder;
      font-family: 'Gill Sans MT';
    }
  }
  .el-menu-item.is-active {
    background-color: rgb(38, 52, 69) !important;
  }
  
  .el-menu-item:hover {
    background-color: rgb(38, 52, 69) !important;
  }
  
  .el-submenu__title:hover {
    background-color: rgb(38, 52, 69) !important;
  }
  
  /*解决收缩菜单文字不消失问题*/
  .el-menu--collapse span {
    visibility: hidden;
  }
}
</style>
