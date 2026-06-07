<template>
  <div class="person">
    <el-form label-width="80px" size="small">
      <el-upload
          class="avatar-uploader"
          ref="uploadAvatarRef" 
          action="/api/file/upload/avatar"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload">
        <div v-if="form.avatar">
          <img :src="'/api/'+form.avatar" class="avatar">
        </div>
        <div v-else>
          <el-avatar :size="38" icon="CirclePlus" />
        </div>
      </el-upload>

      <el-form-item label="昵称">
        <el-input v-model="form.username" style="height: 30px;" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" style="height: 30px;" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="地址">
        <el-input type="textarea" v-model="form.address" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="个性签名">
        <el-input type="textarea" v-model="form.sign" autocomplete="off"></el-input>
      </el-form-item>
      <el-button type="primary" size="large" style="margin-left: 37%;" @click="save">确 定</el-button>
      <el-button type="warning" size="large" style="margin-left: 40px;" @click="cancle">取 消</el-button>
    </el-form>
  </div>
</template>

<script setup>
import { ElNotification } from 'element-plus'
import router from '../router'
import { onMounted, ref } from 'vue'
import { userInfo } from '../store'
import { updateInfo, getUserInfo } from '../api/user.js'

const uploadAvatarRef = ref(null)
const imageUrl = ref('')
const userStore = userInfo()

const form = ref({
  id: '',
  username: '',
  email: '',
  address: '',
  avatar: '',
  sign: '',
})

// 图片上传处理
const beforeAvatarUpload = (rawFile) => {
  // 将选择的文件保存到 formData 中
//   const formData = new FormData()
//   formData.append('file', file)
//   uploadForm.file = formData
//   if (rawFile.type !== 'img/jpg') {
//     ElMessage.error('Avatar picture must be JPG format!')
//     return false
//   }
  return true
}

const handleAvatarSuccess = (response, uploadFile) => {
  imageUrl.value = URL.createObjectURL(uploadFile.raw)
  form.value.avatar = response.data
}


const getUser = () => {
  form.value.id = userStore.user.id
  form.value.username = userStore.user.username
  form.value.email = userStore.user.email
  form.value.address = userStore.user.address
  form.value.avatar = userStore.user.avatarUrl
  form.value.sign = userStore.user.sign
}

const save = () => {
  updateInfo(form.value).then(res => {
    if (res.code === '200') {
      getUserInfo(form.value.id).then(ret => {
        if (ret.code === '200') {
          updateUserInfo(ret.data)
        }else{
          ElNotification.error({
            title: '通 知',
            message: '加载失败 !!!',
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
        return
      })
    }else{
      ElNotification.error({
        title: '通 知',
        message: '更新失败 !!!',
        duration: 1200,
        showClose: false
      })
      return
    }
  }).catch((err) => {
    ElNotification.error({
      title: '通 知',
      message: '更新失败 !!!',
      duration: 1200,
      showClose: false
    })
  })
}

// 跟新信息后赋值
const updateUserInfo = async (data) => {
  form.value.username = data.name
  form.value.email = data.email
  form.value.address = data.address
  form.value.sign = data.sign
  userStore.user.name = data.name
  userStore.user.email = data.email
  userStore.user.address = data.address
  userStore.user.sign = data.sign
  userStore.user.avatarUrl = data.avatar
  ElNotification.success({
    title: '通 知',
    message: '更新成功 !!!',
    duration: 1200,
    showClose: false
  })
}

const cancle = () => {
  router.push('/')
}

onMounted(() => {
  getUser()
})
</script>

<style>
.person {
  width: 660px;
  margin-left: 28%;
}
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
  width: 120px;
  height: 120px;
  line-height: 138px;
  text-align: center;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
}
</style>
