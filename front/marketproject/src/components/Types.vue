<template>
    <div class="types">
        <div class="top">
            <div class="title">{{ props.types.title }}</div>
            <ul class="types-nav" v-if="typesNav.length > 1">
                <li :class="{ active:activeIndex===index }" @mouseenter="activeIndex=index" 
                    v-for="(item, index) in typesNav" :key="index">
                    {{ item }}
                </li>
            </ul>
        </div>
        <div class="box">
            <div class="img">
                <!-- 对应侧边栏图片 -->
                <slot></slot>
            </div>
            <div class="content">
                <Item v-for="item in currentList" :key="item.id" 
                    :item="item" @delete="aaa"></Item>
                <div class="more">
                    <a href="/goods">
                        浏览更多<DArrowRight :width="30"/>
                    </a>
                </div>
            </div>
            
        </div>
    </div>
</template>

<script setup>
import { DArrowRight } from '@element-plus/icons-vue';
import Item from './Item.vue'
import { computed,ref } from 'vue'

const props = defineProps(['types'])
const activeIndex = ref(0) //  定义高亮索引

//  分类菜单
const typesNav = computed(() => {
    if (props.types.datas) {
        return props.types.datas.map(r=>r.title)
    }else {
        return []
    }
})

// 获取当前分类数据
const currentList = computed(() => {
    if (props.types.datas) {
        return props.types.datas[activeIndex.value].data
    }else {
        return []
    }
})

// 删除
const aaa = (e) => {
    // alert(e)
}
</script>

<style lang="scss" scoped>
.types {
    width: 1226px;
    height: 700px;
    margin: 0 auto;
    align-content: center;
    .top {
        height: 58px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        title{
            font-size: 22px;
            color: #333;
            font-weight: 200;
        }
        .types-nav{
            display:  flex;
            align-items: center;
            font-size: 16px;
            li{
                margin-left: 30px;
                cursor: pointer;
                &.active{
                    color:#ff6708;
                    border-bottom: 2px solid #ff6780;
                }
            }

        }
    }
    .box{
        height: 614px;
        display: flex;
        .img{
            width: 246px;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }
        .content{
            flex: 1;
        }
        .more{
            cursor: pointer;
            width: 234px;
            height: 300px;
            background: #fff;
            padding: 20px 10px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            transition: all .2s linear;
            float: left;
            margin: 12px 18px 5px 52px;

            &:hover {
                transform: translate3d(0, -2px, 0);
                box-shadow: 0 15px 30px rgb(0 0 0 / 10%);
            }
            a{
                font-size: 24px;
                color: #333;
                display: flex;
                align-items: center;
                &:hover{
                    color: #ff6700;
                }
            }
        }
    }
}
</style>