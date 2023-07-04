<template>
    <div class="page">
        <el-drawer v-model="visible" :show-close="false" size="500px">
            <template #header="{ close }">
                <h2 style="font-size: 16px; color: #556B2F">{{ productName }}</h2>
                <el-button type="primary" @click="WriteComment">
                    <el-icon class="el-icon--left"><Edit /></el-icon>评论
                </el-button>
                <el-button type="danger" @click="close">
                    <el-icon class="el-icon--left"><CircleCloseFilled /></el-icon>关闭
                </el-button>
            </template>
            <div class="infinite-list-wrapper" style="overflow: auto">
                <ul v-infinite-scroll="load" class="list" :infinite-scroll-disabled="disabled">
                    <li v-for="comment in comments" class="list-item" :key="comment.id">
                        <div>
                            {{ comment.fromName }}： {{ comment.content }}
                        </div>
                        <div>
                            {{ comment.time }} &nbsp; 
                            <span v-if="comment.fromName != userStore.user.username">
                                <el-link @click="ReplyComment(comment.id)" >
                                    <el-icon class="el-icon--left"><Edit /></el-icon>回复
                                </el-link>
                            </span>
                        </div>
                        <div v-if="comment.toName != ''">
                            回复：{{ comment.toName }}
                        </div>
                    </li>
                    <el-icon v-if="loading"><Loading /></el-icon>
                    <p class="comment_notify" v-if="noMore">No more</p>
                </ul>
            </div>
        </el-drawer>

        <el-dialog v-model="showCommentInput" title="评论" center width="40%" :before-close="handleClose">
            <el-form method="post" ref="ruleFormRef" status-icon :model="ruleForm" :rules="rules">
                <el-form-item prop="content">
                    <el-input placeholder="在此处评论" v-model="ruleForm.content" />
                </el-form-item>
                <el-form-item>
                    <el-button style="margin-left: 38%;width: 23%;" type="primary" @click="submitForm(ruleFormRef)">评论</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script setup>
import { $comment_one, $comment_add } from '../api/product.js'
import { ElNotification } from 'element-plus'
import { CircleCloseFilled, Edit, Loading } from '@element-plus/icons-vue'
import { ref, watch, computed } from 'vue'
import { userInfo } from '../store/index.js'

const userStore = userInfo()
const visible = ref(false) 
const loading = ref(false)
const comments = ref([])
const count = ref(6)
const productName = ref('')
const commentCount = ref(3)
const productId = ref(0) 
const pageIndex = ref(1)
const pageSize = ref(6)
const noMore = computed(() => count.value >= commentCount.value)
const disabled = computed(() => loading.value || noMore.value)

const $emit = defineEmits(['cct'])

defineExpose({
    visible,
    productId,
    commentCount,
    productName
})

const load = () => {
    loading.value = true
    setTimeout(() => {
        pageIndex.value += 1
        $comment_one({
            productId: productId.value,
            pageIndex: pageIndex.value,
            pageSize: pageSize.value
        })
        .then(res => {

            // 合并两个数组的元素
            comments.value = [...comments.value,...res.data.commentList]
            count.value += pageSize.value
        })
        .catch(err => {
            console.log(err)
        })
        loading.value = false
    }, 1000)
}

// 显示评论框
const showCommentInput = ref(false)

// 定义表单Ref对象
const ruleFormRef = ref(null)

// 定义表单数据
const ruleForm = reactive({
    content: '',
    productId: 0,
    userId: 0,
    pid: 0,
})

// 验证评论内容
const validateContent = (vrule, value, callback) => {
    if (value === '') {
        callback(new Error('请输入评论内容！！'))
    } else {
        callback()
    }
}

// 验证表单数据
const rules = reactive({
    content: [{ validator: validateContent, trigger: 'blur' }]
})

// 表单提交
const submitForm = (formEl) => {
    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            $comment_add({...ruleForm}).then(ret => {
                loadCommentList()
            }).catch(err => console.log(err)).finally(e => {
                showCommentInput.value = false
                ruleFormRef.value.resetFields()
            })
        } else {
            ElNotification.warning({
                title: '通知',
                message: '评论失败 !!!',
                duration: 1200,
                showClose: false,
            })
        }
    })
}

// 关闭对话框
const handleClose = (done) => {
    // 重置表单
    ruleFormRef.value.resetFields()
    // 关闭窗口
    done()
    // 修改登陆界面状态
    showCommentInput.value = false
}

// 加载商品评论
const loadCommentList = () => {
    $comment_one({
        productId: productId.value,
        pageIndex: 1,
        pageSize: pageSize.value
    })
    .then(res => {
        comments.value = res.data.commentList
        commentCount.value = res.data.total
    })
    .catch(err => {
        console.log(err)
    })
}

// 打开新评论对话框
const WriteComment = () => {
    if (userStore.user.id) {
        ruleForm.productId = parseInt(productId.value) 
        ruleForm.userId = userStore.user.id
        showCommentInput.value = true
        return
    }
    userStore.setShowLander(true)
}

// 打开回复评论对话框
const ReplyComment = (pid) => {
    if (userStore.user.id) {
        ruleForm.pid = pid
        ruleForm.productId = parseInt(productId.value) 
        ruleForm.userId = userStore.user.id
        showCommentInput.value = true
        return
    }
    userStore.setShowLander(true)
}

watch(()=>commentCount.value, (val)=>{
    $emit('cct', val)
},{
    immediate:true
})

watch(()=>productId.value, (val)=>{
    if (val != 0) {
        loadCommentList()
    }
},{
    immediate:true
})
</script>

<style lang="scss" scoped>
.infinite-list-wrapper {
    height: 483px;
    text-align: center;
}
.infinite-list-wrapper .list {
    padding: 0;
    margin: 0;
    list-style: none;
}
  
.infinite-list-wrapper .list-item {
    display: flex;
    align-items: center;
    flex-direction: column;
    align-items: flex-start;
    justify-content: center;
    height: 66px;
    background: var(--el-color-primary-light-9);
    margin: 15px;
    color: var(--el-color-primary);
}
.infinite-list-wrapper .list-item + .list-item {
    margin-top: 10px;
}
.comment_notify{
    display: flex;
    align-items: center;
    justify-content: center;
    height: 66px;
    background: var(--el-color-primary-light-9);
    margin: 10px;
    color: var(--el-color-primary);
}

.page {
    background-color: white;
}
</style>
