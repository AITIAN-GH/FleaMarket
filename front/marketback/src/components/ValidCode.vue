<template>
  <div class="ValidCode disabled-select" :style="`width:${Vawidth}; height:${Vaheight}`" @click="refreshCode">
    <span v-for="(item, index) in codeList" :key="index" :style="getStyle(item)">{{ item.code }}</span>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'

const codeList = ref([])
const Varefresh = ref(0)

defineExpose({Varefresh})

const { Vawidth, Vaheight, Valength } = defineProps({
  Vawidth: {
    type: String,
    default: '100px'
  },
  Vaheight: {
    type: String,
    default: '40px'
  },
  Valength: {
    type: Number,
    default: 4
  }
})

watch(()=> Varefresh.value,(newVal, oldVal)=> {
  while(newVal > 0){
    createdCode()
    newVal = newVal - 1
  }
},{
  immediate: true
})

const refreshCode = () => {
  createdCode()
}

const $emit = defineEmits(['input'])

const createdCode = () => {
  codeList.value = []
  const len = Valength
  const chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz0123456789'
  const charsLen = chars.length
  // 生成
  for (let i = 0; i < len; i++) {
    const rgb = [Math.round(Math.random() * 220), Math.round(Math.random() * 240), Math.round(Math.random() * 200)]
    codeList.value.push({
      code: chars.charAt(Math.floor(Math.random() * charsLen)),
      color: `rgb(${rgb})`,
      fontSize: `${10 + (+[Math.floor(Math.random() * 10)] + 6)}px`,
      padding: `${[Math.floor(Math.random() * 10)]}px`,
      transform: `rotate(${Math.floor(Math.random() * 90) - Math.floor(Math.random() * 90)}deg)`
    })
  }
  // 将当前数据派发出去
  $emit('input', codeList.value.map(item => item.code).join(''))
}
const getStyle = (data) => {
  return `color: ${data.color}; font-size: ${data.fontSize}; padding: ${data.padding}; transform: ${data.transform}`
}

onMounted(() => {
  createdCode()
})
</script>

<style scoped>
.ValidCode {
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

.ValidCode span {
  display: inline-block;
}
</style>
