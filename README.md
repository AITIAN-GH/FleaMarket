# 一个前后端分离的跳蚤市场管理系统

使用技术：SpringBoot + Vue3 + Vite + Redis + ElementPlus + Axios + Pinia + MybatisPlus + MySQL

## 项目结构
- `front/marketproject`：用户前台，默认端口 `80`
- `front/marketback`：后台管理端，默认端口 `8081`
- `back`：Spring Boot 后端，默认端口 `8090`
- `market.sql`：数据库初始化脚本
- `material`：项目演示用头像和商品图片素材

## 环境准备

> JDK 1.8 === Node.js 18.x === Maven 3.9.x === Redis === MySQL 8.x

> 导入 `market.sql` 初始化数据库，并在 `back/market-back/src/main/resources/application-druid.yml` 中配置数据库连接

> 在 `back/market-back/src/main/resources/application.yml` 中配置 Redis、邮箱、支付宝回调和静态资源路径
> 将 `material/avatars` 和 `material/image` 复制到本机静态资源目录，或直接修改 `application.yml` 中的资源路径

## 启动流程

> 在 `front/marketproject` 和 `front/marketback` 目录下分别执行 `npm install`
> 在 `back` 目录下执行 `mvn clean install`

> 启动 Redis 后，运行 `back/market-back` 中的 `MarketBackApplication.java`
>
> 执行 `front/start.bat` 启动两个前端，或分别执行 `npm run dev`

## 访问地址
- 前台：`http://127.0.0.1`
- 后台：`http://127.0.0.1:8081`
- 后端：`http://127.0.0.1:8090`

## 配置说明
- `application-druid.yml`：MySQL 数据源配置
- `application.yml`：Redis、邮件、支付和静态资源配置
- 默认静态资源目录：
  - `avatar-home: C:/Users/AITIAN/Desktop/avatars`
  - `product-image-home: C:/Users/AITIAN/Desktop/image`
