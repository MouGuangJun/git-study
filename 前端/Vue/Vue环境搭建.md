# Vue环境搭建

## 普通Vue使用

### 下载

[安装 — Vue.js (vuejs.org)](https://v2.cn.vuejs.org/v2/guide/installation.html)

![image-20220912175514675](../../../md-photo/image-20220912175514675.png)



### 使用方式

直接引用对应的vue.js就可以使用：

```html
<head>
    <meta charset="UTF-8">
    <title>基本使用</title>
    <script type="text/javascript" src="../js//vue.js"></script>
</head>
```



## 搭建脚手架

Vue脚手架是Vue官方提供的标准化开发工具（开发平台）

官网：

[Vue.js (vuejs.org)](https://v2.cn.vuejs.org/)

![image-20220912205356961](../../../md-photo/image-20220912205356961.png)



win + r打开命令行：

```bash
# npm配置淘宝镜像：
$ npm config set registry=https://registry.npm.taobao.org
# 安装@vue/cli （如果出现卡顿，按一下enter）
$ npm install -g @vue/cli
# 重启cmd，查看vue是否安装成功
$ vue

# 到桌面下创建脚手架
cd C:\Users\19007\Desktop
# 创建脚手架
vue create vue_test
```

选择vue2创建脚手架：

![image-20220912210705699](../../../md-photo/image-20220912210705699.png)



成功创建项目：

![image-20220912210857890](../../../md-photo/image-20220912210857890.png)

启动服务：

```bash
$ cd vue_test 
# 启动服务
$ npm run serve
```



成功启动服务：

![image-20220912211207503](../../../md-photo/image-20220912211207503.png)



helloworld：

访问http://localhost:8080/

![image-20220912211329409](../../../md-photo/image-20220912211329409.png)



停止应用：ctrl + c：连续按两次以上。

