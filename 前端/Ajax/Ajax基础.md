# Ajax基础

## 简介

AJAX 全称为 Asynchronous JavaScript And XML，就是异步的 JS 和XML。通过 AJAX 可以在浏览器中向服务器发送异步请求，最大的优势：<font color='red'>无刷新获取数据</font>。AJAX 不是新的编程语言，而是一种将现有的标准组合在一起使用的新方式。



## 特点

### 优点

- 可以无需刷新页面而与服务器端进行通信。
- 允许你根据用户事件来更新部分页面内容。

### 缺点

- 没有浏览历史，不能回退。
- 存在跨域问题（同源）。
- SEO 不友好（数据不是直接在页面上，爬虫工具无法爬取到数据）。



## 安装Express

```bash
# 初始化包管理工具
$ npm init --yes

# 安装express
$ npm i express
```



编写Express服务应用（<font color='red'>**Code\2.express基本使用\2.express基本使用.js**</font>）：

```javascript
// 1.引入express
const express = require('express');

// 2.创建应用对象
const app = express();

// 3.创建路由规则
// request是对请求报文的封装
// response是对响应报文的封装
app.get('/', (request, response) => {
    // 设置响应
    response.send("Hello, Express");
});

// 监听端口启动服务
app.listen(8000, () => {
    console.log("服务已经启动，8000 端口监听中......");
});
```



启动服务：

```bash
$ node Code\2.express基本使用\2.express基本使用.js
```



前端访问服务http://localhost:8000/：

![image-20220928162948650](../../../md-photo/image-20220928162948650.png)



## Ajax初步使用

### GET请求

服务server.js（<font color='red'>**其中response.setHeader("Access-Control-Allow-Origin", "*");是解决浏览器跨域的问题，必须这样写才能调用成功。**</font>）：

```javascript
// 1.引入express
const express = require('express');

// 2.创建应用对象
const app = express();

// 3.创建路由规则
app.get('/server', (request, response) => {
    // 设置响应头，设置允许跨域
    response.setHeader("Access-Control-Allow-Origin", "*");
    // 设置响应体
    response.send("Hello, Ajax");
});

// 监听端口启动服务
app.listen(8000, () => {
    console.log("服务已经启动，8000 端口监听中......");
});
```



发送Ajax请求的Html：

<font color='red'>**注意readyState码值代表的意思**</font>：

- 0 === 未初始化
- 1 === open调用完毕
- 2 === send部分调用完毕
- 3 === 服务端返回部分结果
- 4 === 服务端返回全部结果。

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>1-GET</title>

    <style>
        #result {
            width: 200px;
            height: 100px;
            border: solid 1px #90b;
        }
    </style>
</head>

<body>
    <button onclick="send()">点击发送请求</button>
    <div id="result"></div>

    <script type="text/javascript">
        function send() {
            // 1.创建对象
            const xhr = new XMLHttpRequest();

            // 2.设置请求的方法和url
            xhr.open("GET", "http://127.0.0.1:8000/server?a=100&b=200");

            // 3.发送
            xhr.send();

            // 4.事件绑定，处理服务的返回结果
            // on  when：当......的时候。
            // readystate：是xhr中表示状态的属性 0 -> 未初始化 1 -> open调用完毕
            // 2 -> send部分调用完毕 3 -> 服务端返回部分结果 4 -> 服务端返回全部结果。
            // change：当状态值改变的时候触发。
            xhr.onreadystatechange = function () {
                // 服务端返回了所有结果
                if (xhr.readyState === 4) {
                    // 判断该状态响应码 2xx成功
                    if (xhr.status >= 200 && xhr.status < 300) {
                        // 处理服务端响应结果
                        console.log(xhr.status);// 状态码
                        console.log(xhr.statusText);// 状态字符串
                        console.log(xhr.getAllResponseHeaders());// 所有响应头
                        console.log(xhr.response);// 响应体

                        // 将响应内容放到div中
                        document.getElementById("result").innerHTML = xhr.response;
                    }
                }
            };
        }
    </script>
</body>

</html>
```



最终结果：

![image-20220928171259918](../../../md-photo/image-20220928171259918.png)



### POST请求

服务server.js修改为接受post请求：

```javascript
// 4.创建POST路由规则
app.post('/server', (request, response) => {
    // 设置响应头，设置允许跨域
    response.setHeader("Access-Control-Allow-Origin", "*");
    // 设置响应体
    response.send("Hello, Ajax POST");
});
```



发送Ajax的Html请求（<font color='red'>**只需要将open中的GET修改为POST即可**</font>）：

```javascript
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>2-POST</title>

    <style>
        #result {
            width: 200px;
            height: 100px;
            border: solid 1px #903;
        }
    </style>
</head>

<body>
    <div id="result"></div>

    <script type="text/javascript">
        // 获取元素对象
        const element = document.getElementById("result");
        // 获取事件（鼠标经过该div元素时发送POST请求）
        element.addEventListener("mouseover", function () {
            // 1.创建对象
            const xhr = new XMLHttpRequest();
            // 2.初始化，设置类型和url
            xhr.open("POST", "http://localhost:8000/server");
            // 3.发送
            xhr.send();
            // 4.事件绑定
            xhr.onreadystatechange = function () {
                // 判断
                if (xhr.readyState === 4) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        // 处理服务端返回结果
                        element.innerHTML = xhr.response;
                    }
                }
            }
        });

        element.addEventListener("mouseout", function () {
            element.innerHTML = "";
        });
    </script>
</body>

</html>
```



测试结果：
![image-20220928201359150](../../../md-photo/image-20220928201359150.png)