<template>
    <el-dialog v-model="showRegister" title="注册" center width="24%" :before-close="handleClose">
        <el-form method="post" enctype="application/x-www-form-urlencoded" ref="ruleFormRef" status-icon :model="ruleForm"
            :rules="rules">
            <el-form-item prop="username">
                <el-input :prefix-icon="Platform" placeholder="请输入账号" v-model="ruleForm.username" />
            </el-form-item>
            <el-form-item prop="email">
                <el-input :prefix-icon="Promotion" placeholder="请输入邮箱" size="large" v-model="ruleForm.email"></el-input>
            </el-form-item>
            <el-form-item prop="password">
                <el-input :prefix-icon="Apple" placeholder="请输入用密码" v-model="ruleForm.password" type="password"
                    autocomplete="off" />
            </el-form-item>
            <el-form-item prop="userrepwd">
                <el-input :prefix-icon="Pear" placeholder="再次确认密码" v-model="ruleForm.userrepwd" type="password"
                    autocomplete="off" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="submitForm(ruleFormRef)">注册</el-button>
                <el-button @click="resetForm(ruleFormRef)">Reset</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</template>

<script setup>
// 导入请求api
import { ElNotification } from 'element-plus'
import { $user_register, $user_checkname } from '@/api/user.js'
import { Apple, Pear, Platform, Promotion } from '@element-plus/icons-vue';
import { reactive, ref } from 'vue'

// 是否显示注册页面
let showRegister = ref(false)
// 关闭对话框
const handleClose = (done) => {
    // 重置表单
    ruleFormRef.value.resetFields()
    // 关闭窗口
    done()
}

// 定义表单Ref对象
const ruleFormRef = ref(null)

// 定义表单数据
const ruleForm = reactive({
    username: '',
    password: '',
    email: '',
    userrepwd: ''
})

// 验证用户名
const validateUserName = (vrule, value, callback) => {
    if (value === '') {
        callback(new Error('请输入用户名！！'))
    } else {
        // 正则验证
        if (/^\w{6,18}$/.test(value)) {
            // 检查用户名是否重复
            $user_checkname(value).then(res => {
                if (res.data) {
                    callback()
                } else {
                    callback(new Error('该用户名已经存在！请重新输入！'))
                }
            }).catch(err => {
                ElNotification.warning({
                    title: '通知',
                    message: '出错了 !!!',
                    duration: 1200,
                    showClose: false,
                })
                return
            })
        } else {
            callback(new Error('用户名必须是有字母、数字、_组成的6-18位字符'))
        }
    }
}

// 验证密码
const validateEmail = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请输入邮箱！！'))
    } else {
        if (!/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/.test(value)) {
            callback(new Error('请输入正确邮箱地址！！'))
        } else {
            callback()
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

// 验证确认密码
const validateUserRePwd = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请再次输入密码'))
    } else {
        if (value === ruleForm.password) {
            callback()
        } else {
            callback(new Error('两次密码输入不一致'))
        }
    }

}

// 验证表单数据
const rules = reactive({
    username: [{ validator: validateUserName, trigger: 'blur' }],
    email: [{ validator: validateEmail, trigger: 'blur' }],
    password: [{ validator: validateUserPwd, trigger: 'blur' }],
    userrepwd: [{ validator: validateUserRePwd, trigger: 'blur' }],
})

// 表单提交
const submitForm = (formEl) => {
    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            $user_register({ ...ruleForm }).then(res => {
                if (res.code == '200') {
                    ElNotification.success({
                        title: '通知',
                        message: '^_< 注册成功 !!',
                        duration: 1200,
                        showClose: false,
                    })
                }else{
                    ElNotification.warning({
                        title: '通知',
                        message: '>_<注册失败 !!!',
                        duration: 1200,
                        showClose: false,
                    })
                }
            }).catch(err => {
                ElNotification.warning({
                    title: '通知',
                    message: '出错了 !!!',
                    duration: 1200,
                    showClose: false,
                })
                console.log(err)
            })
        } else {
            ElNotification.warning({
                title: '通知',
                message: '>_< 注册失败! 格式不对  !!!',
                duration: 1200,
                showClose: false,
            })
        }
        resetForm()
        showRegister.value = false
    })
}
// 重置表单
const resetForm = () => {
    ruleFormRef.value.resetFields()
}

// 暴露组件成员
defineExpose({
    showRegister
})
</script>

<style scoped lang="scss">
.el-button--primary {
    width: 68%;
}
</style>