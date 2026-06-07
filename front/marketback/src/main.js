import { createApp } from 'vue'
import App from './App.vue'
import router from '@/router' 
import { createPinia  } from 'pinia'
import { createPersistedState } from 'pinia-persistedstate-plugin'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import 'element-plus/dist/index.css'
import '@/assets/css/global.css'

const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

const pinia = createPinia()
pinia.use(createPersistedState())

// 挂载插件
app.use(router)
app.use(pinia)
app.mount('#app') // 挂载容器