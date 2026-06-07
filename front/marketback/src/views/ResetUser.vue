<template>
  <div class="wrapper">
    <div style="margin: 150px auto; background-color: #fff; width: 460px; height: 400px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>重置用户密码</b></div>
      <el-form :rules="rules" ref="ruleFormRef" :model="ruleForm">
        <el-form-item prop="email">
          <el-input placeholder="请输入邮箱" prefix-icon="position" size="large" v-model="ruleForm.email"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="请输入密码" prefix-icon="lock" size="large" show-password v-model="ruleForm.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="请确认密码" prefix-icon="lock" size="large" show-password v-model="ruleForm.confirmPassword"></el-input>
        </el-form-item>
        <el-form-item prop="captcha">
          <el-input placeholder="请确输入证码" prefix-icon="HelpFilled" size="large" v-model="ruleForm.captcha"></el-input>
        </el-form-item>
        <el-form-item style="margin: 20px 0 0 70px; text-align: right">
          <el-button type="primary" size="large" autocomplete="off" @click="submitForm(ruleFormRef)">提&nbsp;交</el-button>
          <el-button type="success" size="large" style="margin-left: 20px;" autocomplete="off" @click="router.push('/login')">登&nbsp;录</el-button>
          <el-button type="warning" size="large" style="margin-left: 20px;" autocomplete="off" @click="getUserCaptcha">获取验证码</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ElNotification } from 'element-plus'
import { reactive, ref } from 'vue'
import { getCaptcha, resetPassword } from '../api/resetuser.js'
import router from '@/router'

// 定义表单Ref对象
const ruleFormRef = ref(null)

const ruleForm = reactive({
  email: '',
  password: '',
  confirmPassword: '',
  captcha: ''
})

// 验证邮箱
const validateEmail = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请输入邮箱！！'))
    } else {
        // ^:表示字符串的开始。
        // [a-zA-Z0-9_-]+:表示至少一个字母(大小写)、数字、下划线或破折号。
        // @:表示电子邮件地址中的"at"符号。
        // (?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?:表示可选的子字符串，由一个或多个字母数字字符(包括破折号)和一个连字符组成，长度最多为62个字符。
        // (?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*:表示可选的子字符串，由一个点和一个字母数字字符(包括破折号)组成，长度最多为63个字符。
        // $:表示字符串的结束。
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
        if (/^\w{3,18}$/.test(value)) {
            callback()
        } else {
            callback(new Error('密码必须是有字母、数字、_组成的6-18位字符'))
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

// 验证 验证码
const validateCaptcha = (rule, value, callback) => {
  if(ruleForm.email === '') {
    callback(new Error('请先输入邮箱！！'))
  } else if (value === '') {
    callback(new Error('请输入收到的验证码！！'))
  } else {
    callback()
  }
}

const rules = reactive({
  email: [{ validator: validateEmail, trigger: 'blur' }],
  password: [{ validator: validateUserPwd, trigger: 'blur' }],
  confirmPassword: [{ validator: validateConfirmPassword, trigger: 'blur' }],
  captcha: [{ validator: validateCaptcha, trigger: 'blur' }]
})

// 表单提交
const submitForm = (formEl) => {
  if (!formEl) return
    formEl.validate((valid) => {
      if (valid) {
        resetPassword({...ruleForm}).then((res) => {
          if (res.code == '200') {
            ElNotification.success({
              title: '通知',
              message: '^_< 重置成功 !!',
              duration: 1200,
              showClose: false
            })
          } else {
            ElNotification.success({
              title: '通知',
              message: '>_< 修改失败  !!!',
              duration: 1200,
              showClose: false
            })
          }
        })
      } else {
          ElNotification.warning({
            title: '通知',
            message: '>_< 输入格式不对  !!!',
            duration: 1200,
            showClose: false
          })
          return false
      }
  })
}

// 获取验证码
const getUserCaptcha = () => {
  if (!/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/.test(ruleForm.email)) {
    ElNotification.warning({
      title: '通知',
      message: '>_< 请正确填写邮箱!!!',
      duration: 1000,
      showClose: false
    })
    return false
  }
  getCaptcha({toEmail: ruleForm.email}).then(res => {
    if (res.code == '200') {
      ElNotification.success({
        title: '通知',
        message: '^_< 验证码已发送!!!',
        duration: 1000,
        showClose: false
      })

    }else{
      ElNotification.error({
        title: '通知',
        message: '>_< 获取出错,再核对一下吧!!',
        duration: 1500,
        showClose: false
      })
    }
  }).catch(err => {
    ElNotification.error({
      title: '通知',
      message: '>_< 获取出错,再核对一下吧!!',
      duration: 1500,
      showClose: false
    })
    console.log(err)
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
