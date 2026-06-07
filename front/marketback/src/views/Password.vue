<template>
  <el-card style="width: 500px; margin-left: 28%;margin-top: 30px;">
    <span><h3 style="margin-left: 47%;margin-bottom: 10px;">修改密码</h3></span>
    <el-form label-width="120px" :rules="rules" ref="ruleFormRef" status-icon :model="form">
      <el-form-item label="原密码" prop="password">
        <el-input v-model="form.password" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="form.newPassword" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-button type="primary" size="large" style="margin-left: 37%;" @click="save(ruleFormRef)">确 定</el-button>
      <el-button type="warning" size="large" style="margin-left: 40px;" @click="cancle">取 消</el-button>
    </el-form>
  </el-card>
</template>

<script setup>
import { ElNotification } from "element-plus"
import { ref, reactive } from "vue"
import router from '../router'
import { updatePass, user_logout } from '../api/user.js'
import { userInfo } from "../store"

const userStore = userInfo()
const ruleFormRef = ref(null)

const form = reactive({
  id: 0,
  password: '',
  newPassword: '',
  confirmPassword: ''
})

const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else{
    callback()
  }
}

const validateNewPass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码!!'))
  }else if (value === form.password){
    callback(new Error('不能与原密码一致 !!'))
  } else{
    callback()
  }
}

const validateConPass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码 !!'))
  }else if (value != form.newPassword){
    callback(new Error('两次不一致密码'))
  }else if (value === form.password){
    callback(new Error('不能与原密码一致 !!'))
  }else {
    callback()
  }
}

const rules = reactive({
  password: [{ validator: validatePass, trigger: 'blur' }],
  newPassword: [{ validator: validateNewPass, trigger: 'blur' }],
  confirmPassword: [{ validator: validateConPass, trigger: 'blur' }]
})

const save = (formEl) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (valid) {
      form.id = userStore.user.id
      updatePass(form).then(res => {
        if (res.code === '200') {
          user_logout({
            userUid: userStore.user.uid, 	// UID of the user that just logged in.
          }).then(res => {
            if (res.code == '200') {
              ElNotification.success({
                title: '通 知',
                message: '修改成功 !!!',
                duration: 1200,
                showClose: false
              })
              userStore.resetUser()
            }else{
              ElNotification.success({
                title: '通 知',
                message: '出错了 !!!',
                duration: 1200,
                showClose: false
              })
            }
          }).catch(err => {
            ElNotification.error({
              title: '通 知',
              message: '出错了 !!!',
              duration: 1200,
              showClose: false
            })
          })
        }else{
          ElNotification.error({
            title: '通 知',
            message: '修改失败 !!!',
            duration: 1200,
            showClose: false
          })
        }
      }).catch(err => {
        ElNotification.error({
          title: '通 知',
          message: '出错了 !!!',
          duration: 1200,
          showClose: false
        })
      })
      resetForm()
    }else{
      ElNotification.warning({
        title: '通 知',
        message: '输入有误 !!!',
        duration: 1200,
        showClose: false
      })
    }
  })
}

const cancle = () => {
  router.push('/')
}

// 重置表单
const resetForm = () => {
  ruleFormRef.value.resetFields()
}
</script>

<style>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}
.avatar {
  width: 138px;
  height: 138px;
  display: block;
}
</style>
