<template>
  <div :style="fixStyle">
    <img src="../assets/404.png" style="width: 100%; height: 100%">
  </div>
</template>

<script setup>
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