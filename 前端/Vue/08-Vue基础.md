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
<keep-alive include="News"> 
    <router-view></router-view>
</keep-alive>
```

测试结果：

![image-20221007225422459](../../../md-photo/image-20221007225422459.png)