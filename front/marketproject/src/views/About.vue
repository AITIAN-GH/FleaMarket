<template>
    <div class="page">
      <div class="content" style="text-align: center; font-size: 32px;"> 
        <div style="background-color: padding: 20px;">
          <p class="gradient-text">感  谢  使  用  本  系  统</p>
          <p class="gradient-text">Powered By <span class="gradient-text">AITIAN</span></p>
          <p class="gradient-text">联系我：780662364@qq.com</p>
        </div>
      </div>
    </div>
</template>
  
<style scoped lang="scss">
.page{
  width: 1226px;
  height: 340px;

  .content {
    text-align: center;
    display: flex;
    justify-content: center;

    .gradient-text {
      font-size: 32px;
      font-weight: bold;
      background: linear-gradient(to right, #b7b7c8, #151f91);
      background-clip: text;
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      text-align: center;
      margin-top: 50px; 
    }
  }
}
</style>