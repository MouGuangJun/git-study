# Vue基础

## 特点

1.采用**<font color='red'>组件化</font>**模式，提高代码复用率、且让代码更好维护。

2.**<font color='red'>声明式</font>**编码，让编码人员无需直接操作DOM，提高开发效率。

3.使用**<font color='red'>虚拟DOM</font>**+优秀的**<font color='red'>Diff算法</font>**，尽量复用DOM节点



## Vue基础配置

### Vue-Devtools

官网：

[Vue Devtools](https://v2.cn.vuejs.org/v2/guide/installation.html#Vue-Devtools)

下载Vue Devtools，直接拖入到谷歌浏览器的扩展程序列表中即可。



### 关闭开发模式警告

在body标签中添加以下的js配置：

```html
<script type="text/javascript">
    Vue.config.productionTip = false // 阻止Vue启动时生成生产提示。
</script>
```



## HelloWorld

### 引入Vue

```html
script type="text/javascript" src="../js/vue.js"></script>
```



### 使Vue进行工作



- 想让Vue工作，就必须创建一个Vue实例，且要传入一个配置对象；（在body里面进行操作）

```javascript
<script type="text/javascript">
    Vue.config.productionTip = false // 阻止Vue启动时生成生产提示。

    // 创建Vue实例
    new Vue({
        // el用户指定当前Vue实例为哪个容器服务，值通常为css选择器字符串
        el:'#root',
        data:{// data中用于存储数据，数据供el所选定的容器去使用
            name:'Vue'
        }
    });
</script>
```



- root容器里的代码依然符合html规范，只不过混入了一些特殊的Vue语法；

  <font color='red'>其中{{}}是Vue的写法</font>

  ```html
  <div id = "root">
      <h1>Hello, {{name}}</h1>
  </div>
  ```

  

- root容器里的代码被称为【Vue模板】;



### 结果

![image-20220829142651646](../../../md-photo/image-20220829142651646.png)



### 注意事项

1. Vue实例跟容器只能是一对一的关系，真实开发中只有一个Vue实例，并且会配合着组件一起使用
2. {{xxx}}中的xxx要写js表达式（**<font color='red'>xxx可以自动读取到data中的所有属性</font>**），如将当前数据改为大写：{{val.toUpperCase()}}
3. 一旦data中的数据发生改变，那么模板中用到该数据的地方也会自动更新
4. 使用Vue开发者工具进行简单测试

![image-20220829144215648](../../../md-photo/image-20220829144215648.png)



## 模板语法

### 插值语法

功能：用于解析标签体内容。
写法:{{xxx}}，xxx是js表达式，且可以直接读取到data中的所有属性，如

```html
<h3>你好，{{value}}</h3>
```



### 指令语法

功能：用于解析标签（包括:标签属性、标签体内容、绑定事件.....） 。
举例：<font color='bold'>v-bind:href="xxx”或简写为 :href="xxx"</font>，xxx同样要写js表达式，且可以直接读取到data中的所有属性。v-bind**<font color='red'>可以给任意一个标签中的属性绑定值</font>**，如：

```html
<a v-bind:href="value">点击我到百度的官网</a>
<a :href="value">点击我到百度的官网</a>
```



**Tips**

Vue中有很多的指令，且形式都是: v-????，此处我们只是拿v-bind举个例子。

解析多层结构的数据：

```json
data: {
    name: 'jack',
    site: {
        url: 'http://www.baidu.com',
        name: '百度'
    }
}
```

解析：

```html
<a :href="site.url">点击我到{{site.name}}的官网</a>
```



## 数据绑定

### 单项数据绑定

#### 命令

v-bind：数据只能从data流向页面。

#### 案例

略



### 双向数据绑定

#### 命令

v-model：数据不仅能从data流向页面，还可以从页面流向data。

**<font color='red'>v-model只能应用在表单类元素（输入类元素）上，(如:input、select等）</font>**

v-model:value 可以简写为v-model，因为v-model默认收集的就是value值。

#### 案例

```html
<div id='root'>
    <!-- 普通的写法 -->
    <!-- 单向数据绑定：<input type="text" v-bind:value="name" />
<br />
双向数据绑定：<input type="text" v-model:value="name" />
<br /> -->

    <!-- 简写的方式 -->
    单向数据绑定：<input type="text" :value="name" />
    <br />
    双向数据绑定：<input type="text" v-model="name" />
</div>

<script type="text/javascript">
    new Vue({
        el: '#root',
        data: {
            "name":"尚硅谷"
        }
    });
</script>
```



结果：

单向绑定：

![image-20220829164437526](../../../md-photo/image-20220829164437526.png)

双向绑定：

![image-20220829164510873](../../../md-photo/image-20220829164510873.png)

## data与el的2种写法

### el

- new Vue时候配置el属性。
- 先创建Vue实例，随后再通过vm.$mount(' #root')指定el的值。

```javascript
// 初始化Vue对象时进行挂载
new Vue({
    el: '#root',
    data: {
        name: 'vue'
    }
});

// 初始化Vue对象完成后进行挂载
const v = new Vue({
    data: {
        name: 'vue'
    }
});

v.$mount('#root');
```



### data

- 对象式
- 函数式

```javascript
// 对象式写法
new Vue({
    el: '#root',
    data: {
        name: 'vue'
    }
});

// 函数式写法
new Vue({
    el: '#root',
    /* data:function() {
                return {
                    name: 'vue'
                };
            } */
    data() {
        return {
            name: 'vue'
        };
    }
})
```

如何选择:目前哪种写法都可以，以后学习到组件时，data必须使用函数式，否则会报错。

### 重要原则

- 由Vue管理的函数，一定不要写箭头函数，一旦写了箭头函数，this就不再是Vue实例了。



## MVVM模型

- M：模型(Model) ：对应 data 中的数据
- V：视图(View) ：模板
- VM：视图模型(ViewModel) ： Vue 实例对象

![img](../../../md-photo/5b25e59d1c7bb2786a20464ecd222ba2.png)

实际应用：

![image-20220829173743600](../../../md-photo/image-20220829173743600.png)



其中：

把Model中的数据解析到页面上：Model -> ViewModel -> View

把前端的修改保存到Model中：View -> ViewModel -> Model



注意：

- data中所有的属性,最后都出现在了vm身上。
- vm身上所有的属性及Vue原型上所有属性，在Vue模板中都可以直接使用。
