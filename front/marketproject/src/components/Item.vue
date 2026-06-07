<template>
    <div class="item" @click="gotoDetail(item.id)">
        <div class="bookimg">
            <img :src="baseURL+item.classImg">
        </div>
        <div class="close">
            <el-popconfirm
                title="确定删除?" 
                confirm-button-text="确定" 
                cancel-button-text="取消" 
                @confirm="confirmDel(item.id)" 
                v-if="close">
                <template #reference>              
                    <Close  @click.stop/>
                </template>
            </el-popconfirm>
        </div>
        <div class="name">书名:{{ item.productName }}</div>
        <div class="bookdetail">
            <span class="author">作者:{{ item.productAuthor }}</span>&ensp;&ensp;
        </div>
        <div class="price">
            <!-- 若原价大于现价显示原价 -->
            <span v-if="item.pro_price > item.del_price">
                <span class="selling_price">{{ item.del_price }}￥起</span>
                <span class="por_price">￥{{ item.pro_price }}</span>
            </span>
            <span v-else-if="item.pro_price <= item.del_price">
                <span class="selling_price">{{ item.del_price }}￥起</span>
            </span>
        </div>
    </div>
</template>

<script setup>
import { baseURL } from '@/config'
import { Close } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()
// 定义组件属性
const { item, close } = defineProps({
    item: {
        type: Object
    },
    close: {
        type: Boolean,
        default: false
    }
})
// 跳转至详情页
const gotoDetail = (itemId) => {
    // 去除高亮索引
    router.push('/detail?id=' + itemId)
}
// 定义事件
const emit = defineEmits(['delete'])
// 定义确定删除
const confirmDel = (id) => {
    emit('delete', id)
}
</script>

<style scoped lang="scss">
.item {
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
    position: relative;
    &:hover {
        transform: translate3d(0, -2px, 0);
        box-shadow: 0 15px 30px rgb(0 0 0 / 10%);

        .close {
            z-index: 1;
        }
    }

    .bookimg {
        align-self: center;
        display: flex;
        justify-content: center;
        width: 200px;
        overflow: hidden;
        img {
            height: 176px;
        }
    }

    .name {
        margin-top: 14px;
        font-size: 14px;
        color: #333;
    }

    .bookdetail {
        font-size: 12px;
        color: #b0b0b0;
        margin: 10px 0;
    }

    .price {
        margin: 2px 0;

        .selling_price {
            font-size: 14px;
            color: #ff6700;
        }

        .por_price {
            font-size: 14px;
            color: #b0b0b0;
            text-decoration: line-through;
            margin-left: 10px;
        }
    }

    .close {
        position: absolute;
        right: 20px;
        top: 10px;
        width: 20px;
        height: 20px;
        z-index: -2;
 
        &:hover {
            color: #ff6700;
        }
    }

}
</style>