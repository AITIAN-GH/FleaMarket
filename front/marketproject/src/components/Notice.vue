<template>
    <div class="page">
        <el-drawer v-model="visible" :show-close="false" size="500px">
            <template  #header="{ titleClass  }">
                <div :class="titleClass" class="title">未读信息</div>
                <el-button style="margin-right: 14%;" type="primary" @click="AllReceived">
                    <el-icon class="el-icon--left"><SuccessFilled /></el-icon>一键已读
                </el-button>
            </template>
            <div class="infinite-list-wrapper" style="overflow: auto">
                <ul v-infinite-scroll="load" class="list" :infinite-scroll-disabled="disabled">
                    <li v-for="Message in Messages" class="flc" :key="Message.id">
                        <div class="list-item">
                            <div v-if="!Message.myContent">
                                <span style="font-weight: bolder;">{{ Message.fromName }}</span> <span style="margin-left: 8px;">评论了我的商品</span>
                            </div>
                            <div v-else>
                                <span style="font-weight: bolder;">{{ Message.fromName }}</span> <span style="margin-left: 8px;">回复了我的评论</span>
                            </div>
                            <div>
                                回复 <span style="color: var(--el-color-primary);">{{ Message.myName }}</span>： {{ Message.content }}
                            </div>
                            
                            <div style="font-size: 14px;color: #b39999;" v-if="Message.myContent">
                                |{{ Message.myName }}：{{ Message.myContent }}
                            </div>
                            <div>
                                {{ new Date(Message.time).toLocaleString() }} 
                                <el-link @click="ReplyMessage(Message)"><el-icon class="el-icon--left"><Edit /></el-icon>回复</el-link>&nbsp;
                                <el-link @click="Received(Message)"><el-icon class="el-icon--left"><Edit /></el-icon>已读</el-link>
                            </div>
                        </div>
                        <div>
                            <div @click="gotoDetail(Message.productId)">
                                <img :src="'/api/'+Message.proImg" style="width: 78px;height: 78px;" />
                            </div>
                        </div>
                    </li>
                    <el-icon v-if="loading"><Loading /></el-icon>
                    <p class="Message_notify" v-if="noMore">No more</p>
                </ul>
            </div>
        </el-drawer>

        <el-dialog v-model="showMessageInput" title="回复评论" center width="36%" :before-close="handleClose">
            <el-form method="post" ref="ruleFormRef" status-icon :model="ruleForm" :rules="rules">
                <el-form-item prop="content">
                    <el-input placeholder="在此处书写回复" v-model="ruleForm.content" />
                </el-form-item>
                <el-form-item>
                    <el-button style="margin-left: 38%;width: 23%;" type="primary" @click="submitForm(ruleFormRef)">评论</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script setup>
import { $user_news } from '../api/user.js'
import { $comment_add, $com_received } from '../api/product.js'
import { ElNotification } from 'element-plus'
import { SuccessFilled, Edit, Loading } from '@element-plus/icons-vue'
import { ref, watch, onMounted } from 'vue'
import { userInfo, userMenuActive } from '../store'
import router from '../router'

const userStore = userInfo()
const menuActive = userMenuActive()
const visible = ref(false) 
const loading = ref(false)
const Messages = ref([])
const count = ref(0)
const MessageCount = ref(0)
const pageIndex = ref(1)
const pageSize = ref(5)
const noMore = computed(() => count.value >= MessageCount.value)
const disabled = computed(() => loading.value || noMore.value)
const showMessageInput = ref(false) // 显示消息框
const ruleFormRef = ref(null) // 定义表单Ref对象

defineExpose({
    visible,
    MessageCount
})

watch(()=>visible.value, (val)=> {
    if (val) {
        loadNoticeList()
    }
},{immediate:true})

const load = () => {
    loading.value = true
    setTimeout(() => {
        pageIndex.value += 1
        $user_news({
            userId: userStore.user.id,
            pageIndex: pageIndex.value,
            pageSize: pageSize.value
        }).then(res => {
            // 合并两个数组的元素
            if (Messages.value) {
                Messages.value = [...Messages.value,...res.data.news]
            }else{
                Messages.value = res.data.news
            }
            count.value += pageSize.value
            localStorage.setItem('messageList', JSON.stringify(Messages.value))
        }).catch(err => {
            console.log(err)
        })
        loading.value = false
    }, 1000)
}

// 定义表单数据
const ruleForm = reactive({
    content: '',
    userId: 0,
    productId: 0,
    pid: 0,
})

// 验证消息内容
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
                console.log(ret)
                // loadNoticeList()
            }).catch(err => console.log(err)).finally(e => {
                showMessageInput.value = false
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
    showMessageInput.value = false
}

// 加载通知消息
const loadNoticeList = () => {
    if(localStorage.user != null){
        let localUser = JSON.parse(localStorage.user)
        $user_news({
            userId: localUser.id,
            pageIndex: 1,
            pageSize: pageSize.value
        }).then(res => {
            if (res.data != null) {
                Messages.value = res.data.news
                MessageCount.value = res.data.total
                count.value = pageSize.value
                menuActive.messageList = Messages.value
            }
        })
    }
}

// 打开回复消息对话框
const ReplyMessage = (message) => {
    if (!userStore.user.id) {
        userStore.setShowLander(true)
        return
    }
    ruleForm.productId = message.productId
    ruleForm.pid = message.commentId
    ruleForm.userId = userStore.user.id
    showMessageInput.value = true
}

// 确认收到信息
const Received = (message) => {
    if (!userStore.user.id) {
        userStore.setShowLander(true)
        return
    }
    
    $com_received({
        userId: userStore.user.id,
        userNewId: message.id,
    }).then(res => {
        if (res.code == '200') {
            menuActive.messageList = Messages.value
        }
    }).catch(err => {
        console.log(res)
    })
    Messages.value = Messages.value.filter(m => m.id != message.id)
}

// 一键已读
const AllReceived = (message) => {
    if (!userStore.user.id) {
        userStore.setShowLander(true)
        return
    }
    $com_received({
        userId: userStore.user.id,
        userNewIds: Messages.value.map(m => m.id)
    }).then(res => {
        if (res.code == '200') {
           
            menuActive.messageList = []
        }
    }).catch(err => {
        console.log(res)
    })
    Messages.value = []
}

const gotoDetail = (productId) => {
    // 通过router跳转到Detail页面
    visible.value = false
    router.push('/detail?id=' + productId)
}

watch(() => menuActive.messageList, (newVal, oldVal) => {
    Messages.value = []
    if (newVal) {
        newVal.forEach(message => {
            Messages.value.push(message)
        })
    }
}, { deep: true })

onMounted(() => {
    loadNoticeList()
})
</script>

<style lang="scss" scoped>
.title{
    margin-left: 10%;
    margin-right: 2px;
    font-size: 26px;
    font-weight: bolder;
    color: rgba(55, 84, 90, 0.842);
}
.flc{
    display: flex;
    flex-direction: row;
    justify-content: space-between; 	
    background: var(--el-color-primary-light-9);
}
.infinite-list-wrapper {
    height: 496px;
    text-align: center;
}
.infinite-list-wrapper .list {
    padding: 0;
    margin: 5px;
    list-style: none;
}
  
.infinite-list-wrapper .list-item {
    display: flex;
    align-items: center;
    flex-direction: column;
    align-items: flex-start;
    justify-content: center;
    height: 100px;
}

.infinite-list-wrapper .list-item + .list-item {
    margin-top: 10px;
}
.Message_notify{
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
