# 一个前后端分离的跳蚤市场管理系统系统
使用技术：SpringBoot + Vuejs + Redis + ElementPlus + Axios  + Pinia + MybatisPlus + MySQL
## 环境准备
> JDK1.8 === Nodejs18.12.1 === Maven3.9.2 === Redis-Win === MySQL
## 启动流程
 - 1.先将项目`git clone`到本地
>
 - 2.前台准备，在front的两个子文件夹下分别执行`npm i`安装系统所需依赖
> 
 - 3.后台准备，使用`Maven`包管理工具安装系统所需依赖，推荐使用Idea开发工具来管理项目代码
> 
 - 4.在`application.yml`配置好相关配置如数据库连接用户名、密码等
> 
 - 5.启动redis服务器`之后`，启动后台Springboot项目开启服务
> 
 - 6.使用front文件夹下的start.bat启动前台UI页面
> 
 - 7.完全启动后会自动跳转到`http://localhost:80`,或手动访问
