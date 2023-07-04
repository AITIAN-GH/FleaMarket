<template>
    <el-dialog v-model="showLander" title="登录" center width="24%" :before-close="handleClose">
        <el-form method="post" enctype="application/x-www-form-urlencoded" ref="ruleFormRef" status-icon :model="ruleForm"
            :rules="rules">
            <el-form-item prop="username">
                <el-input :prefix-icon="Platform" placeholder="请输入账号" v-model="ruleForm.username" />
            </el-form-item>
            <el-form-item prop="password">
                <el-input :prefix-icon="Apple" placeholder="请输入用密码" v-model="ruleForm.password" type="password"
                    autocomplete="off" />
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="submitForm(ruleFormRef)">
                    <span>登录</span><span style="margin-left: 4px;" v-if="showLoading"><el-icon><Loading /></el-icon></span>
                </el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</template>

<script setup>
// 导入请求api
import { $user_login } from '@/api/user.js'
// 导入全局状态管理方法userInfo 管理User对象
import { userInfo, userMenuActive } from '@/store/index.js'
// 导入通知消息框
import { reactive, ref, watchEffect } from 'vue'
import { ElNotification } from 'element-plus'
import { Apple, Platform, Loading } from '@element-plus/icons-vue'
import websocketclient from '../utils/webscoket.js'

const userStore = userInfo()
const showLoading = ref(false)
// 是否显示登录页面
const showLander = ref(userStore.showLander)

// 更新登录页状态
watchEffect(() => {
    showLander.value = userStore.showLander
})

// 关闭对话框
const handleClose = (done) => {
    // 重置表单
    ruleFormRef.value.resetFields()
    // 关闭窗口
    done()
    // 修改登陆界面状态
    userStore.setShowLander(false)
}

// 定义表单Ref对象
const ruleFormRef = ref(null)

// 定义表单数据
const ruleForm = reactive({
    username: '',
    password: ''
})

// 验证用户名
const validateUserName = (vrule, value, callback) => {
    if (value === '') {
        callback(new Error('请输入用户名！！'))
    } else {
        // 正则验证
        if (/^\w{6,18}$/.test(value)) {
            callback()
        } else {
            callback(new Error('用户名必须是有字母、数字、_组成的6-18位字符'))
        }
    }
}

// 验证密码
const validateUserPwd = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请输入密码！！'))
    } else {
        if (/^\w{6,18}$/.test(value)) {
            callback()
        } else {
            callback(new Error('密码必须是有字母、数字、_组成的6-18位字符'))
        }
    }
}

// 验证表单数据
const rules = reactive({
    username: [{ validator: validateUserName, trigger: 'blur' }],
    password: [{ validator: validateUserPwd, trigger: 'blur' }],
})

// 初始化ws
const initMySocket = (wsIsRun, username) => {
  websocketclient.wsInit(wsIsRun, username)
}

// 表单提交
const submitForm = (formEl) => {
    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            showLoading.value = true
            $user_login({...ruleForm})
            .then(ret => { 
                let data = ret.data
                if (ret.code =='200') {
                    ElNotification.success({
                        title: '通知',
                        message: '^_< 登录成功  !!',
                        duration: 1200,
                        showClose: false,
                    })
                    let realUser = {
                        id: data.id,				
                        username: data.username,
                        flag: 1,
                        unReadCount: 0
                    }
                    // 存入pinia
                    userStore.setUser(realUser)
                    // 保存用户状态存入浏览器缓存
                    localStorage.setItem('before_token', data.token)
                    //清空表单数据
                    formEl.resetFields()
                    // 开启websocket
                    initMySocket(true, data.username)
                    showLoading.value = false
                    //关闭窗口
                    showLander.value = false
                } else {
                    showLoading.value = false
                    ElNotification.warning({
                        title: '通知',
                        message: '>_< 登录失败！用户名或密码错误  !!!',
                        duration: 1200,
                        showClose: false,
                    })
                }
            }).catch(err => {
                showLoading.value = false
                console.log(err)
            })
        } else {
            ElNotification.warning({
                title: '通知',
                message: '>_< 登录失败 格式不对  !!!',
                duration: 1200,
                showClose: false,
            })
        }
    })
}

// 暴露组件成员
defineExpose({
    showLander
})
</script>

<style scoped lang="scss">
.el-button--primary {
    width: 100%;
}
</style>