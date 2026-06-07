<template>
  <div class="app" id="app">
    <!-- 头部 -->
    <div class="header">
      <div class="content">
        <ul>
          <li v-if="userStore.user.id">
            <el-dropdown trigger="click">
              <span style="margin-top: 3px; color: #f7f6cc">{{
                userStore.user.username
              }}</span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>
                    <el-link
                      :underline="false"
                      href="http://127.0.0.1:8081/"
                      target="blank"
                      >我的后台</el-link
                    >
                  </el-dropdown-item>
                  <el-dropdown-item @click="exit">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </li>
          <template v-else>
            <li @click="login">登录</li>
            <li>|</li>
            <li @click="register">注册</li>
          </template>
          <li v-if="userStore.user.id">
            <el-dropdown trigger="click">
              <span style="margin-top: 3px; color: #f7f6cc">我的订单</span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="openOrder">未支付</el-dropdown-item>
                  <el-dropdown-item @click="openPayedOrder"
                    >已支付</el-dropdown-item
                  >
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </li>
          <li @click="openOrder" v-else>我的订单</li>
          <li @click="openCollect">我的收藏</li>
          <li
            class="shopcart"
            @click="openShoppingCart"
            :class="{ shopcart2: cartcount > 0 }"
          >
            <el-icon><ShoppingCart /></el-icon>
            <span v-if="cartcount == 0">购物车</span>
            <span v-else>购物车({{ cartcount }})</span>
          </li>
          <li @click="getUserNews" style="display: flex; align-items: center">
            <el-icon style="color: white"><Bell /></el-icon>
            <div class="red-point" v-if="unReadCount > 0"></div>
            <span style="margin-left: 5px">消息</span>
          </li>
        </ul>
      </div>
    </div>
    <!-- 头部end-->
    <!-- 导航 -->
    <div class="nav">
      <div class="content">
        <div class="logo">
          <span @click="router.push('/')">
            <p
              style="
                font-size: larger;
                font-weight: bolder;
                font-family: 'Gill Sans', 'Gill Sans MT', Calibri,
                  'Trebuchet MS', sans-serif;
              "
            >
              AITIAN
            </p>
          </span>
        </div>
        <div class="menu">
          <el-menu
            :default-active="activeIndex"
            class="el-menu-demo"
            mode="horizontal"
            router
          >
            <el-menu-item index="/">首页</el-menu-item>
            <el-menu-item index="/goods">全部商品</el-menu-item>
            <el-menu-item index="/about">关于</el-menu-item>
          </el-menu>
        </div>
      </div>
    </div>
    <!-- 导航end -->
    <!-- 主体 -->
    <main class="main">
      <div class="routerviews">
        <router-view></router-view>
      </div>
    </main>
    <!-- 主体end -->
    <!-- 底部 -->
    <div class="footer">
      <div class="top">
        <a class="icon1" href="javascript:;">7天无理由退换货</a>
        <a class="icon2" href="javascript:;">满99元全场免邮</a>
        <a class="icon3" href="javascript:;">l00%品质保证</a>
      </div>
      <div class="line"></div>
      <div class="bottom">
        <p>
          <a href="https://github.com/AITIAN-GH/FleaMarket">
            <img src="@/assets/GitHub.png" alt="" srcset="" />
          </a>
        </p>
        <p>
          <a class="icon1" href="javascript:;">首页</a>
          <span>|</span>
          <a class="icon2" href="javascript:;">全部商品</a>
          <span>|</span>
          <a class="icon3" href="javascript:;">关于我们</a>
        </p>
        <p>Powered By AITIAN &copy; 2023</p>
        <p>联系我：780662364@qq.com</p>
      </div>
    </div>
    <!-- 底部end -->
    <Lander ref="LoginRef"></Lander>
    <Register ref="registerRef"></Register>
    <Notice ref="noticeRef"></Notice>
  </div>
</template>

<script setup>
import Notice from "./components/Notice.vue";
import Lander from "@/components/Lander.vue";
import Register from "@/components/Register.vue";
import { ElNotification } from "element-plus";
import { ShoppingCart, Bell } from "@element-plus/icons-vue";
import { $check_token, $user_logout, $user_news } from "./api/user.js";
import { onMounted, ref, computed, watch } from "vue"; // 引入组合式api
import { useRouter, useRoute } from "vue-router";
import { userMenuActive, userInfo, shoppingCartInfo } from "@/store"; // 导入全局状态存储库
import websocketclient from "./utils/webscoket.js";

// 使用pinia管理菜单索引
const menuActive = userMenuActive();
// 使用pinia管理用户信息
const userStore = userInfo();
// 使用pinia管理购物车信息
const cartStore = shoppingCartInfo();
// 创建路由
const router = useRouter();
// 获取当前路由
const route = useRoute();
// 定义一个ref用于绑定组件
const LoginRef = ref(null);
const registerRef = ref(null);
const noticeRef = ref(null);
const cartcount = ref(userStore.user.unReadCount);
const messages = ref([]);
const unReadCount = ref(0);

// 菜单选项激活时监视路由变化
watch(
  () => route.path,
  (nval) => {
    menuActive.setActiveIndex(nval);
  },
  {
    immediate: true, //立刻执行
  }
);

watch(
  () => cartStore.cartcount,
  (val) => {
    cartcount.value = val;
  },
  {
    immediate: true, //立刻执行
  }
);

watch(
  () => menuActive.messageList,
  (newVal, oldVal) => {
    messages.value = newVal;
    if (messages.value) {
      unReadCount.value = messages.value.length;
    }
  },
  { deep: true }
);

// 注册登录页面方法
const login = () => {
  // 打开登录页面
  LoginRef.value.showLander = true;
};

// 注册页面方法
const register = () => {
  // 打开注册页面
  registerRef.value.showRegister = true;
};

// 定义菜单高亮索引
const activeIndex = computed(() => {
  return menuActive.activeIndex;
});

// 打开订单
const openOrder = () => {
  if (!checkLogin()) {
    router.push("/order");
  }
};

// 打开已支付订单
const openPayedOrder = () => {
  if (!checkLogin()) {
    router.push("/payed");
  }
};

// 打开收藏
const openCollect = () => {
  if (!checkLogin()) {
    router.push("/collect");
  }
};

// 打开购物车
const openShoppingCart = () => {
  if (!checkLogin()) {
    router.push("/shoppingcart");
  }
};

// 登出方法
const exit = () => {
  $user_logout({ userId: userStore.user.id })
    .then((ret) => {
      if (ret.code === "200") {
        userStore.resetUser();
        cartStore.resetCart();
        cartcount.value = 0;
        window.location.reload();
      } else {
        ElNotification.warning({
          title: "通知",
          message: "出错了 !!",
          duration: 1200,
          showClose: false,
        });
      }
    })
    .catch((err) => {
      ElNotification.error({
        title: "通知",
        message: "出错了 !!",
        duration: 1200,
        showClose: false,
      });
    });
};

const checkLogin = () => {
  if (!userStore.user.id) {
    // 修改登陆界面状态
    userStore.setShowLander(true);
    return true;
  }
};

const getUserNews = () => {
  if (!userStore.user.id) {
    // 修改登陆界面状态
    userStore.setShowLander(true);
    return true;
  }
  noticeRef.value.visible = true;
};

onMounted(() => {
  if (!menuActive.messageList == "") {
    messages.value = menuActive.messageList;
  }
  let localdata = JSON.parse(localStorage.getItem("user"));
  // 如果浏览器缓存中有user信息
  if (localdata != null && localdata != "") {
    $check_token({
      id: localdata.id,
      username: localdata.username,
      flag: localdata.flag,
    }).then((res) => {
      if (res.data) {
        let localuser = JSON.parse(localStorage.getItem("user"));
        websocketclient.wsInit(true, localuser.username);
        userStore.setUser(localuser);
        menuActive.messageList = JSON.parse(localStorage.getItem("messageList"));
      } else {
        userStore.resetUser()
      }
    }).catch(err => {
      ElNotification.warning({
        title: "通知",
        message: "出错了，请联系管理员 !!",
        duration: 1200,
        showClose: false,
      })
      console.log(err)
    })
  } else {
    userStore.resetUser()
  }
})
</script>

<style lang="scss">
* {
  margin: 0;
  padding: 0;
  list-style: none;
  text-decoration: none;
  box-sizing: border-box;
  font-size: 14px;
}

.app {
  width: 100vw;

  .header {
    height: 40px;
    background-color: #3d3d3d;

    .content {
      width: 1226px;
      margin: 0 auto;
      color: #b0b0b0;
      display: flex;
      justify-content: flex-end;

      ul {
        display: flex;
        align-items: center;
        height: 40px;
        background-color: #3d3d3d;

        li {
          cursor: pointer;
          margin: 0 6px;

          &:hover {
            color: #fff;
          }

          &.shopcart {
            width: 108px;
            height: 40px;
            margin-left: 15px;
            line-height: 40px;
            background-color: #3d3d3d;
            display: flex;
            justify-content: center;
            align-items: center;

            span {
              margin-left: 3px;
            }

            &:hover {
              color: #ff6700;
              background-color: #fff;
            }
          }

          &.shopcart2 {
            background-color: #ff6700;
            color: white;
            &:hover {
              background-color: #ff6700;
              color: white;
            }
          }

          .red-point {
            position: relative;
            border: 3px solid red;
            border-radius: 3px;
            margin-bottom: 14px;
          }
        }
      }
    }
  }

  .nav {
    margin-top: 10px;

    .content {
      width: 1226px;
      margin: 0 auto;
      border-bottom: 1px solid #b0b0b0;
      align-items: center;
      display: flex;

      .logo {
        width: 170px;
        text-align: center;
      }

      .menu {
        width: 400px;
        margin-left: 12px;

        .el-menu-horizontal {
          border-bottom: none;
        }

        .el-menu-item {
          margin-left: 40px;
        }
      }
    }
  }

  .main {
    margin: 10px 0;

    .routerviews {
      width: 1226px;
      margin: 0 auto;
      background-color: #f5f5f5;
    }
  }

  .footer {
    background-color: #2f2f2f;
    height: 334px;

    .line {
      height: 1px;
      background-color: #3d3d3d;
    }

    .top {
      width: 1226px;
      height: 145px;
      margin: 0 auto;
      display: flex;
      justify-content: center;
      align-items: center;

      a {
        color: white;
        font-size: 20px;
        height: 40px;
        width: 210px;
        background: url(./assets/footbg.png) no-repeat;
        padding-left: 56px;
        line-height: 40px;

        &.icon1 {
          background-position: -2px -4px;
        }

        &.icon2 {
          background-position: -2px -55px;
          margin: 0 150px;
        }

        &.icon3 {
          background-position: -2px -102px;
        }
      }
    }

    .bottom {
      width: 1226px;
      margin: 0 auto;
      color: #888888;

      p {
        img {
          width: 45px;
        }
        text-align: center;
        margin: 20px 0;

        a {
          color: #888888;
        }

        span {
          margin: 0 20px;
        }
      }
    }
  }
}
</style>