<template>
  <div class="homepage-hero-module">
    <div class="video-container">
      <div :style="fixStyle" class="filter">
        <div style="width: 400px; margin: 100px auto">
          <div style="font-size: 30px; text-align: center; padding: 30px 0; color: #333">欢迎登录</div>
          <el-form :rules="rules" ref="ruleFormRef" status-icon :model="ruleForm">
            <el-form-item prop="username">
              <el-input size="large" prefix-icon="user" v-model="ruleForm.username"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input size="large" prefix-icon="lock" show-password v-model="ruleForm.password"></el-input>
            </el-form-item>
            <el-form-item prop="validCode">
              <div style="display: flex">
                <el-input prefix-icon="key" v-model="ruleForm.validCode" style="width: 48%; margin-right: 4%" placeholder=" 输 入 验 证 码"></el-input>
                <ValidCode @input="createValidCode" ref="VdRef"/>
              </div>
            </el-form-item>
            <el-form-item>
              <el-button style="width: 100%" type="primary" autocomplete="off" @click="submitForm(ruleFormRef)">
                <span>登录</span><span style="margin-left: 4px;" v-if="showLoading"><el-icon><Loading /></el-icon></span>
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button :link="true" @click="gotoRegister">前往注册 >></el-button>
              <el-button :link="true" @click="gotoResetUser" style="margin-left: 56%;">忘记密码??</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <video :style="fixStyle" autoplay loop muted class="fillWidth" :oncanplay="videpCanplay">
        <source src="@/assets/sea.mp4" type="video/mp4"/>
        浏览器不支持 video 标签，建议升级浏览器。
      </video>
    </div>
  </div>
</template>

<script setup>
import ValidCode from "@/components/ValidCode.vue"
import { user_login } from '../api/user.js'
import { reactive, ref } from 'vue'
import { ElNotification } from 'element-plus'
import router from '@/router'
import { userInfo } from '../store'

const userStore = userInfo()
const VdRef = ref(null)
const showLoading = ref(false)
// 背景视频样式
const fixStyle = ref('')
let vdCode = ''
const Varefresh = ref(0)

// 定义表单Ref对象
const ruleFormRef = ref(null)

const ruleForm = reactive({
  username: '',
  password: '',
  validCode: ''
})

const gotoRegister = () => {
  router.push('/register')
}

const gotoResetUser = () => {
  router.push('/resetUser')
}

// 获取验证码
const createValidCode = (data) => {
  vdCode = data
}

const validateName = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入用户名'))
  } else {
    callback()
  }
}

const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else{
    callback()
  }
}

const validateVdCode = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入验证码'))
  } else {
    callback()
  }
}

const rules = reactive({
  username: [{ validator: validateName, trigger: 'blur' }],
  password: [{ validator: validatePass, trigger: 'blur' }],
  validCode: [{ validator: validateVdCode, trigger: 'blur' }]
})

// 表单提交
const submitForm = (formEl) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (!(vdCode === ruleForm.validCode || ruleForm.validCode === vdCode.toLowerCase())) {
      VdRef.value.Varefresh = 1
      ElNotification.warning({
        title: '通 知',
        message: '验证码错误 !!!',
        duration: 1200,
        showClose: false
      })
      return
    }
    if (valid) {
      VdRef.value.Varefresh = 1
      showLoading.value = true
      user_login({...ruleForm}).then((res) => {
        let ret = res.data
        if (res.code == '200') {
          ElNotification.success({
            title: '通 知',
            message: '登录成功 !!!',
            duration: 1200,
            showClose: false
          })   
          if (ret.token) {
            localStorage.setItem('back_token', ret.token)
          }
          // 将permissionList中每个元素的auth属性提取出来，包括其children中的auth属性，然后过滤掉其中的空值
          const userAuths = ret.permissionList.flatMap(item => [item.auth, ...(item.children || []).map(it => it.auth)]).filter(Boolean)

          const { id, uid, name: username, address, avatar: avatarUrl, email, sign } = ret.user
          const manager = { id, uid, username, address, avatarUrl, email, sign, auths: userAuths, routerList: ret.permissionList }
          
          // 使用{...item}将对象属性展开，然后将children属性值替换为一个新的数组，通过filter方法过滤出page属性为true的子元素
          const asides = ret.permissionList.map(item => ({ ...item, children: item.children.filter(child => child.page) }))
          
          userStore.setUser(manager)
          localStorage.setItem('menus', JSON.stringify(asides))
          showLoading.value = false
          router.push('/')
        }else{
          showLoading.value = false
          ElNotification.error({
            title: '通 知',
            message: '用户名或密码错误 !!!',
            duration: 1200,
            showClose: false
          })
        }
      })
      formEl.resetFields()
    } else {
      VdRef.value.Varefresh = 1
      ElNotification.warning({
        title: '通 知',
        message: '请正确输入 !!!',
        duration: 1200,
        showClose: false
      })
      return false
    }
  })
}

const videpCanplay = () => {
  // 背景视频开始播放时加载下面代码
  const windowWidth = document.body.clientWidth
  const windowHeight = document.body.clientHeight
  const windowAspectRatio = windowHeight / windowWidth
  let videoWidth
  let videoHeight
  if (windowAspectRatio < 0.5625) {
    videoWidth = windowWidth
    videoHeight = videoWidth * 0.5625
    fixStyle.value = {
      height: windowWidth * 0.5625 + 'px',
      width: windowWidth + 'px',
      'margin-bottom': (windowHeight - videoHeight) / 2 + 'px',
      'margin-left': 'initial'
    }
  } else {
    videoHeight = windowHeight
    videoWidth = videoHeight / 0.5625
    fixStyle.value = {
      height: windowHeight + 'px',
      width: windowHeight / 0.5625 + 'px',
      'margin-left': (windowWidth - videoWidth) / 2 + 'px',
      'margin-bottom': 'initial'
    }
  }
}

// 窗口大小变化时， 调整背景视频大小
window.onresize = () => {
  const windowWidth = document.body.clientWidth
  const windowHeight = document.body.clientHeight
  const windowAspectRatio = windowHeight / windowWidth
  let videoWidth
  let videoHeight
  if (windowAspectRatio < 0.5625) {
    videoWidth = windowWidth
    videoHeight = videoWidth * 0.5625
    fixStyle.value = {
      height: windowWidth * 0.5625 + 'px',
      width: windowWidth + 'px',
      'margin-bottom': (windowHeight - videoHeight) / 2 + 'px',
      'margin-left': 'initial'
    }
  } else {
    videoHeight = windowHeight
    videoWidth = videoHeight / 0.5625
    fixStyle.value = {
      height: windowHeight + 'px',
      width: windowHeight / 0.5625 + 'px',
      'margin-left': (windowWidth - videoWidth) / 2 + 'px',
      'margin-bottom': 'initial'
    }
  }
}

</script>

<style scoped>
.homepage-hero-module,.video-container {
  position: relative;
  height: 100vh;
  overflow: hidden;
}

.video-container .poster img{
  z-index: 0;
  position: absolute;
}

.video-container .filter {
  z-index: 1;
  position: absolute;
  /*background: rgba(0, 0, 0, 0.4);*/
  width: 100%;
}

.fillWidth {
  width: 100%;
}
</style>