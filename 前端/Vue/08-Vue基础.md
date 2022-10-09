# Vue基础

## 路由

### 定义

1. 一个路由就是一组映射关系（key - value）
2. key 为路径, value 可能是 function 或 component

###  路由分类

1. 后端路由：
   1. 理解：value 是 function, 用于处理客户端提交的请求。
   2. 工作过程：服务器接收到一个请求时, 根据**请求路径**找到匹配的**函数**来处理请求, 返回响应数据。 
2. 前端路由： 
   1. 理解：value 是 component，用于展示页面内容。
   2. 工作过程：当浏览器的路径改变时, 对应的组件就会显示。

![image-20221006190735018](../../../md-photo/image-20221006190735018.png)

![image-20221006191334484](../../../md-photo/image-20221006191334484.png)

### vue-router

vue 的一个插件库，专门用来实现 SPA 应用

### SPA 应用

1. 单页 Web 应用（single page web application，SPA）。
2. 整个应用只有<font color='red'>一个完整的页面</font>。
3. 点击页面中的导航链接<font color='red'>不会刷新页面</font>，只会做页面的<font color='red'>局部更新</font>。
4. 数据需要通过 ajax 请求获取。

![image-20221006190853895](../../../md-photo/image-20221006190853895.png)



### 基本使用

1. 安装vue-router：

   ```bash
   # vue2需要安装vue-router3版本
   $ npm i vue-router@3
   ```

2. 在main.js中应用插件

   ```javascript
   // 引入VueRouter(用于Vue应该该插件)
   import VueRouter from "vue-router";
   // 引入路由器
   import router from "./router";
   
   // 应用VueRouter插件
   Vue.use(VueRouter);
   
   new Vue({
       // 配置路由器
       router
   });
   ```

3. 编写路由规则

   ```javascript
   // 该文件专门用于创建整个应用的路由器
   import VueRouter from "vue-router";
   
   import About from "../components/About";
   import Home from "../components/Home";
   
   // 创建一个路由器
   export default new VueRouter({
       routes: [
           // About路由规则
           {
               path: "/about",// 路由规则
               component: About// Vue组件
           },
           // Home路由规则
           {
               path: "/home",// 路由规则
               component: Home// Vue组件
           },
       ]
   });
   ```

4. 实现切换（active-class可配置高亮样式）

   <font color='red'>**router-link最终会被vue解析为a标签**</font>。

   ```html
   <!-- Vue中借助router-link标签实现路由的切换 -->
   <!-- active-class是当路由激活情况下展示的样式 -->
   <!-- router-link最终会被vue解析为a标签 -->
   <router-link class="list-group-item" active-class="active" to="/about">About</router-link>
   <router-link class="list-group-item" active-class="active" to="/home">Home</router-link>
   ```

5. 指定展示位置

   ```html
   <!-- 指定组件的呈现位置 -->
   <router-view />
   ```

6. 最终结果：

   ![image-20221006202321157](../../../md-photo/image-20221006202321157.png)



**<font color='red'>注意点**</font>

- 路由组件通常存放在`pages`文件夹，一般组件通常存放在`components`文件夹。
- 通过切换，“隐藏”了的路由组件，默认是<font color='red'>被销毁掉的，需要的时候再去挂载</font>。
- 每个组件都有自己的`$route`属性，里面存储着自己的路由信息。
- <font color='red'>整个应用只有一个router</font>，可以通过组件的`$router`属性获取到。



### 嵌套（多级）路由

配置路由规则的时候添加children配置项：

**<font color='red'>注意：只有一级路由需要加斜杠/，子路由不需要加</font>**。

```javascript
// 创建一个路由器
export default new VueRouter({
    routes: [
        // Home路由规则
        {
            path: "/home",// 路由规则
            component: Home,// Vue组件
            children: [
                {
                    path: "news",// 只有一级路由需要加/，子路由不需要加
                    component: News
                },
                {
                    path: "message",// 只有一级路由需要加/，子路由不需要加
                    component: Message
                }
            ]
        },
    ]
});
```

页面中使用路由：

**<font color='red'>注意：to里面需要写全路径，不能只写一个/news</font>**。

```html
<!-- to里面的路径需要是路由的全路径 -->
<router-link class="list-group-item" to="/home/news" active-class="active">News</router-link>
```

最终结果：

![image-20221006210119081](../../../md-photo/image-20221006210119081.png)



### 路由Query传参

#### 传递参数

```vue
<!-- 跳转并携带query参数，to的字符串写法 -->
<!-- :to会将后面的字符串当作js语法解析，通过js的模板语法实现参数替换 -->
<router-link :to="`/home/message/detail?id=${m.id}&title=${m.title}`">{{m.title}}</router-link>
				
<!-- 跳转并携带query参数，to的对象写法 -->
<router-link 
	:to="{
		path:'/home/message/detail',
		query:{
		   id: m.id,
           title: m.title
		}
	}"
>跳转</router-link>
```

#### 接收参数

```js
$route.query.id
$route.query.title
```

<font color='red'>**原理：router-link路由传递的参数在$route的query对象中**</font>。

![image-20221007183405488](../../../md-photo/image-20221007183405488.png)



### 命名路由

作用：可以简化路由的跳转。

给路由命名：

```js
export default new VueRouter({
    routes: [
        {
            // 给路由命名
            name: "about",
            path: "/about",
            component: About
        },
        {
            path: "/home",
            component: Home,
            children: [
                {
                    path: "message",
                    component: Message,
                    children: [
                        {
                            // 给路由命名
                            name: "detail",
                            path: "detail",
                            component: Detail
                        }
                    ]
                }
            ]
        },
    ]
});
```

简化跳转：

```vue
<router-link
  :to="{
  // 简化前，需要写完整的路径
  // path: '/home/message/detail',
  // 简化后，直接通过名字跳转
  name: 'detail',
  query: {
	id: m.id,
	title: m.title
  }
}"
>{{m.title}}</router-link>
```



### 路由的params参数

#### 配置路由params参数

```js
{
	// 给路由命名
	name: "detail",
	// 使用占位符声明接收params参数
	// 其中:id和:title为路径参数占位符
	path: "detail/:id/:title",
	component: Detail
}
```

#### 传递参数

```vue
<!-- 跳转并携带params参数，to的字符串写法 -->
<router-link :to="`/home/message/detail/${m.id}/${m.title}`">{{m.title}}</router-link>
				
<!-- 跳转并携带params参数，to的对象写法 -->
<router-link
  :to="{
  name: 'detail',
  // 使用params方式传递参数时，只能使用name不能使用path
  // path: '/home/message/detail',
  params: {
	id: m.id,
	title: m.title
  }
}"
>{{m.title}}</router-link>
```

> <font color='red'>**特别注意：路由携带params参数时，若使用to的对象写法，则不能使用path配置项，必须使用name配置！**</font>

#### 接收参数

```javascript
$route.params.id
$route.params.title
```

<font color='red'>**原理：router-link路由传递的参数在$route的params对象中**</font>。

![image-20221007203138342](../../../md-photo/image-20221007203138342.png)



### 路由的props配置

作用：让路由组件更方便的收到参数

<font color='red'>**第三种写法接收到的是$route参数，可以通过解构赋值的方式，直接将query和params中的所有参数当作props传递给组件**</font>。

```js
{
	// 给路由命名
	name: "detail",
	// 使用占位符声明接收params参数
	// 其中:id和:title为路径参数占位符
	path: "detail/:id/:title",
	// path: "detail",
	component: Detail,
	// props的第一种写法，值为对象，该对象中的所有key-value都会以props的形式传递给Detail组件。
	// props: { serialNo: 1, msg: "hello" }

	// props的第二种写法，值为布尔值，若布尔值为真，则会把该路由收到的所有param参数，以props的形式传递给Detail组件
	// props: true

	// props的第三种写法，值为函数，可以传递普通对象以及$route对象中的属性
	props({ query, params }) {
		return {
			serialNo: 1,
			msg: "hello",
			// id: $route.params.id,
			// title: $route.params.title,
			// id: $route.query.id,
			// title: $route.query.title,
			...query,
			...params
		}
	}
}
```



### router-link的raplace属性

- 作用：控制路由跳转时操作浏览器历史记录的模式。
- 浏览器的历史记录有两种写入方式：分别为`push`和`replace`，`push`是追加历史记录，`replace`是替换当前记录。路由跳转时候默认为`push`。
- 开启replace模式：<font color='red'>**:replace="true"/replace**</font>。

```vue
<router-link replace class="list-group-item" active-class="active" to="/about">About</router-link>
```

测试结果：

![image-20221007210450347](../../../md-photo/image-20221007210450347.png)



如果home和about有记录，但是news和message没有记录，那么<font color='red'>**从message后退会直接到about**</font>。

![image-20221007212906768](../../../md-photo/image-20221007212906768.png)

### 编程式路由导航

作用：不借助`<router-link>`实现路由跳转，让路由跳转更加灵活

具体编码：

```js
// 代替router-link，有痕迹浏览模式 
pushShow(m) {
	this.$router.push({
		name: "detail",
		params: {
			id: m.id,
			title: m.title
		}
	});
}

// 代替router-link，无痕迹浏览模式 
replaceShow(m) {
    this.$router.replace({
        name: "detail",
        params: {
            id: m.id,
            title: m.title
        }
    });
}

this.$router.forward();// 浏览器前进
this.$router.back();// 浏览器后退
this.$router.go(n);// 前进（正数）/后退（负数）n步
```



### 缓存路由组件

作用：让不展示的路由组件保持挂载，不被销毁。

具体编码：

<font color='red'>**注意：include中是不需要被销毁的组件名称；keep-alive应该放在不需要被销毁的组件的上一级（放在使用该组件的位置）**</font>

```vue
<!-- keep-alive让vc切走的时候不销毁，include是其中不需要销毁的vc组件名称 -->
<!-- 缓存单个路由组件 -->
<!-- <keep-alive include="News"> -->
<!-- 缓存多个路由组件 -->
<keep-alive :include="['News', 'Message']">
    <router-view></router-view>
</keep-alive>
```

测试结果：

![image-20221007225422459](../../../md-photo/image-20221007225422459.png)



### 两个新的生命周期钩子

- 作用：路由组件所独有的两个钩子，用于捕获路由组件的激活状态。
- 具体名字：
  - `activated`路由组件被激活时触发。
  - `deactivated`路由组件失活时触发。

实例：

组件激活的时候触发定时器，切走的时候关闭定时器。（<font color='red'>**可以在组件被缓存的情况下让一些动作变得可控[开/关]**</font>）。

```vue
<template>
  <div>
    <ul>
      <li :style="{opacity}">欢迎学习Vue</li>
    </ul>
  </div>
</template>

<script>
export default {
  activated() {
    console.log("News组件激活了...");
    this.timer = setInterval(() => {
      console.log("@");
      this.opacity -= 0.01;
      if (this.opacity <= 0) this.opacity = 1;
    }, 16);
  },

  deactivated() {
    console.log("News组件失活了...");
    console.log("清空定时器：", this.timer);
    clearInterval(this.timer);
  }
};
</script>
```

测试结果：

激活组件的时候触发定时器：

![image-20221008202318244](../../../md-photo/image-20221008202318244.png)

失活组件的时候关闭定时器：

![image-20221008202413310](../../../md-photo/image-20221008202413310.png)



### 路由守卫

作用：对路由进行权限控制

#### 全局守卫

<font color='red'>**1.用户自定义的数据需要放到meta中；2.next只在前置路由守卫中存在；3.后置路由守卫一般用于放行后做一些事，避免在前置路由守卫中出现重复的代码**</font>。

```javascript
{
    ...
    meta: {
        title: "关于",
        isAuth: true
    }
}

// 全局前置路由守卫 --- 初始化的时候被调用、每次路由切换之前被调用
router.beforeEach((to, from, next) => {
    // to是指到哪个路由，from是指从哪个路由过来
    console.log(to, from);
    // 当路由到的组件不是news、message组件或者school名字是尚硅谷时，才展示news、message组件中的内容
    if (/* (to.name !== "news" && to.name != "message") */
        !to.meta.isAuth // 判断是否需要鉴权
        || localStorage.getItem("school") === "尚硅谷") {
        next();// 放行
    } else {
        alert("学校名字不对，无权限查看！");
    }
});

// 全局后置路由守卫 --- 初始化的时候被调用、每次路由切换之后被调用
// 切换完成了，不再需要next进行放行
router.afterEach((to, from) => {
    // to是指到哪个路由，from是指从哪个路由过来
    console.log(to, from);
    document.title = to.meta.title || "硅谷系统";
});


export default router;
```

测试案例：

手动在浏览器的本地内存中添加键为school的数据：

![image-20221008205111617](../../../md-photo/image-20221008205111617.png)



学校名字不是尚硅谷时，没有查看的权限：

![image-20221008213353335](../../../md-photo/image-20221008213353335.png)

通过后置路由守卫实现页签的切换（**路由切换完成之后执行，防止在前置路由守卫处出现冗余的代码**）：

![image-20221008213509313](../../../md-photo/image-20221008213509313.png)



#### 独享守卫

<font color='red'>**1.beforeEnter在beforeEach后执行，如果beforeEach拦截了，那么这段代码不会被执行；2.beforeEnter和afterEach互不影响**</font>。

```js
// beforeEnter在beforeEach后执行，如果beforeEach拦截了，那么这段代码不会被执行
beforeEnter(to, from, next) {
    if (localStorage.getItem("school") === "尚硅谷") {
        next();
    } else {
        alert("学校名字不对，无权限查看！");
    }
}
```



#### 组件内路由守卫

<font color='red'>**beforeRouteLeave不是后置路由守卫，而是在该路由切走前执行的一些操作，如果不放行，那么一直保持该组件不变**</font>。

```javascript
// 通过路由规则，进入该组件的时候被调用
beforeRouteEnter(to, from, next) {
    if (localStorage.getItem("school") === "尚硅谷") {
      next();
    } else {
      alert("学校名字不对，无权限查看！");
    }
},

// 通过路由规则，离开该组件的时候被调用
// 参数同beforeRouteEnter
// 这里不是后置路由守卫，而是在该路由切走前执行的一些操作，如果不放行，那么一直保持该组件不变
beforeRouteLeave(to, from, next) {
	alert("不满足指定的条件，该页面不能被切走！");
}
```

测试结果：

beforeRouteEnter结果跟全局守卫/独享守卫一致。

beforeRouteLeave：

路由切走离开组件的时候进行判断，如果不放行，那么该页面无法被切走。

![image-20221008221912488](../../../md-photo/image-20221008221912488.png)



### 路由器的两种工作模式

#### 定义

1. 对于一个url来说，什么是hash值？—— #及其后面的内容就是hash值。
2. hash值不会包含在 HTTP 请求中，即：hash值不会带给服务器。
3. hash模式：
   1. 地址中永远带着#号，不美观 。
   2. 若以后将地址通过第三方手机app分享，若app校验严格，则地址会被标记为不合法。
   3. 兼容性较好。
4. history模式：
   1. 地址干净，美观 。
   2. 兼容性和hash模式相比略差。
   3. 应用部署上线时需要后端人员支持，解决刷新页面服务端404的问题。



#### 实例

编写后端服务器：

```javascript
const express = require("express");

const app = express();

// 设置静态资源的路径 __dirname相当于./
app.use(express.static(__dirname + "/static/server3"));

app.get("/person", (req, res) => {
    res.send({
        name: "Tom",
        age: 18
    });
});

app.listen(5000, (err) => {
    if (!err) console.log("服务器启动成功了！");
})
```

将Vue项目编译为js、css、html静态资源：

```bash
$ npm run build
```

编译完成之后的文件夹位置

![image-20221008230914896](../../../md-photo/image-20221008230914896.png)

将资源放到服务器的static目录下：

![image-20221008231340900](../../../md-photo/image-20221008231340900.png)



##### histroy模式

![image-20221008231241188](../../../md-photo/image-20221008231241188.png)

解决该问题的方法：

让后端认为这是在访问静态资源即可，通过connect-history-api-fallback应用解决该问题：

```bash
# 需要在服务器位置安装该应用
$ npm i connect-history-api-fallback
```

后端服务器修改为：

```javascript
const history = require("connect-history-api-fallback");

// 判断路径是在访问后端资源还是前端路由
app.use(history());

...
```

成功访问到静态资源：

![image-20221008233036045](../../../md-photo/image-20221008233036045.png)



#### hash模式

hash模式由于传递给后端的路径没有变，所以没有影响，可以正常访问静态资源。

![image-20221008233427771](../../../md-photo/image-20221008233427771.png)





## UI组件库

### 移动端常用 UI 组件库

1. Vant https://youzan.github.io/vant
2. Cube UI https://didi.github.io/cube-ui
3. Mint UI http://mint-ui.github.io

### PC 端常用 UI 组件库

1. Element UI https://element.eleme.cn
2. IView UI https://www.iviewui.com



### Element UI使用

#### 基本使用

安装Element UI

```bash
$ npm i element-ui
```



main.js中引入element-ui：

```javascript
// 引入Vue
import Vue from 'vue';

// 引入Element-UI组件库
import ElementUI from "element-ui";
import 'element-ui/lib/theme-chalk/index.css';

// 使用ElementUI插件
Vue.use(ElementUI);

// 引入App
import App from './App';

// 关闭Vue生产提示
Vue.config.productionTip = false;

new Vue({
    el: '#app',
    render: h => h(App),
});
```

引入对应的组件库使用即可：

```vue
<template>
  <div>
    <button>传统的按钮</button>

    <el-row>
      <el-button type="primary" round>主要按钮</el-button>
      <el-button type="success">成功按钮</el-button>
    </el-row>

    <el-row>
      <el-button icon="el-icon-delete-solid" circle></el-button>
      <el-button type="primary" icon="el-icon-edit" circle></el-button>
    </el-row>

    <el-date-picker
      v-model="value"
      align="left"
      type="date"
      placeholder="选择日期"
      :picker-options="pickerOptions"
    ></el-date-picker>
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      pickerOptions: {
        // 禁止选择的日期
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
        // 设置快捷选项卡
        shortcuts: [
          {
            text: "今天",
            onClick(picker) {
              picker.$emit("pick", new Date());
            }
          },
          {
            text: "昨天",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit("pick", date);
            }
          },
          {
            text: "一周前",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", date);
            }
          }
        ]
      },
      value: ""
    };
  }
};
</script>
```

成功应用了组件：

![image-20221009224424470](../../../md-photo/image-20221009224424470.png)

组件的配置项参见官方的文档说明：

![image-20221009224506698](../../../md-photo/image-20221009224506698.png)



#### 按需引入

安装 babel-plugin-component：

```bash
# D表示的是开发依赖，会把包添加到package.json的devDependencies下，这些包只在做项目的时候会使用到，在项目打包上线后不依赖于这些包项目依然可以正常运行。比如：gulp/webpack、eslint、sass等等。

# -S表示的是生产依赖，会把包添加到package.json的dependencies下，这些包在项目打包上线后依然需要使用项目才能正常运行，比如：axios、element-ui、vue-router等等。

$ npm install babel-plugin-component -D
```



babel.config.js文件中增加配置：

```javascript
module.exports = {
  presets: [
    '@vue/cli-plugin-babel/preset',
    ["es2015", { "modules": false }]
  ],

  "plugins": [
    [
      "component",
      {
        "libraryName": "element-ui",
        "styleLibraryName": "theme-chalk"
      }
    ]
  ]
}
```

在main.js中引入使用到的对应ElementUI中的组件：

```javascript
import { Button, Row, DatePicker } from "element-ui";
Vue.component(Button.name, Button);
Vue.component(Row.name, Row);
// use和component的方式都可以
Vue.use(DatePicker);
```

出现找不到模块的错误：

![image-20221009230125300](../../../md-photo/image-20221009230125300.png)

安装对应的包即可：

```bash
$ npm i babel-plugin-component
$ npm i babel-preset-es2015
```

出现插件预设的错误：

![image-20221009230505796](../../../md-photo/image-20221009230505796.png)

<font color='red'>修改babel.config.js中的**es2015**为**@babel/env**</font>

![image-20221009231354626](../../../md-photo/image-20221009231354626.png)

成功实现了按需引入：

![image-20221009232247768](../../../md-photo/image-20221009232247768.png)

![image-20221009232421082](../../../md-photo/image-20221009232421082.png)