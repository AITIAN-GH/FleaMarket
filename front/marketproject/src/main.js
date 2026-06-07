import { createApp } from 'vue'
import App from '@/App.vue'
import router from '@/router' // 导入路由
import { createPinia  } from 'pinia'
import 'element-plus/dist/index.css'

let app = createApp(App) // 创建Vue实例
const pinia = createPinia()
// 挂载插件
app.use(router)
app.use(pinia)
app.mount('#app') // 挂载容器
