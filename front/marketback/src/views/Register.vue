<template>
  <div class="wrapper">
    <div style="margin: 150px auto; background-color: #fff; width: 460px; height: 400px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>注 册</b></div>
      <el-form :rules="rules" ref="ruleFormRef" :model="ruleForm">
        <el-form-item prop="username">
          <el-input placeholder="请输入账号" prefix-icon="user" size="large" v-model="ruleForm.username"></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input placeholder="请输入邮箱" prefix-icon="position" size="large" v-model="ruleForm.email"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="请输入密码" prefix-icon="lock" size="large" show-password v-model="ruleForm.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="请确认密码" prefix-icon="lock" size="large" show-password v-model="ruleForm.confirmPassword"></el-input>
        </el-form-item>
        <el-form-item style="margin: 20px 0 0 120px; text-align: right">
          <el-button type="primary" size="large" autocomplete="off" @click="submitForm(ruleFormRef)">&nbsp;注&nbsp;&nbsp;册&nbsp;</el-button>
          <el-button type="warning" size="large" style="margin-left: 20px;" autocomplete="off" @click="router.push('/login')">去登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ElNotification } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'
import { user_register, user_checkname } from '../api/user.js'
import router from '@/router'

// 定义表单Ref对象
const ruleFormRef = ref(null)

const ruleForm = reactive({
  username: '',
  password: '',
  email: '',
  confirmPassword: ''
})

// 验证用户名
const validateUserName =  (vrule, value, callback) => {
  if (value === '') {
      callback(new Error('请输入用户名！！'))
  } else {
    // 正则验证
    if (/^\w{6,18}$/.test(value)) {
      // 检查用户名是否重复
      user_checkname(value).then(res => {
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
const validateUserPwd = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请输入密码！！'))
    } else {
        if (/^\w{3,18}$/.test(value)) {
            callback()
        } else {
            callback(new Error('密码必须是有字母、数字、_组成的6-18位字符'))
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

// 验证确认密码
const validateConfirmPassword = (rule, value, callback) => {
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

const rules = reactive({
  username: [{ validator: validateUserName, trigger: 'blur' }],
  password: [{ validator: validateUserPwd, trigger: 'blur' }],
  email: [{ validator: validateEmail, trigger: 'blur' }],
  confirmPassword: [{ validator: validateConfirmPassword, trigger: 'blur' }]
})

// 表单提交
const submitForm = (formEl) => {
  if (!formEl) return
    formEl.validate((valid) => {
      if (valid) {
        user_register({...ruleForm}).then((res) => {
          console.log(res)
        })
        // if (result) {
          ElNotification.success({
              title: '通知',
              message: '^_< 注册成功 !!',
          })
          // } else {
          //     ElNotification.success({
          //         title: '通知',
          //         message: '>_< 注册失败  !!!',
          //     })
          // }
      } else {
          ElNotification.warning({
              title: '通知',
              message: '>_< 注册失败! 格式不对  !!!',
          })
          return false
      }
  })
}
</script>

<style>
  .wrapper {
    height: 100vh;
    background-image: linear-gradient(to bottom right, #1b7f37 , #3F5EFB);
    overflow: hidden;
  }
</style>
