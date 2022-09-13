# Vue基础

## Vue组件化编程

实现应用中**<font color='red'>局部</font>**功能**<font color='red'>代码</font>**和**<font color='red'>资源</font>**的**<font color='red'>集合</font>**

![image-20220911105518402](../../../md-photo/image-20220911105518402.png)



![image-20220911105418305](../../../md-photo/image-20220911105418305.png)



vm实现对组件的管理，组件中还可以嵌套组件。

![image-20220911105608810](../../../md-photo/image-20220911105608810.png)



### 模块与组件、模块化与组件化

#### 模块

1. 理解：向外提供特定功能的js程序，一般就是一个js文件
2. 为什么: js文件很多很复杂
3. 作用:复用js，简化js的编写，提高js运行效率



#### 组件

1. 理解：用来实现局部(特定)功能效果的代码集合(html/css/js/image…)
2. 为什么：一个界面的功能很复杂
3. 作用：复用编码,简化项目编码,提高运行效率



#### 模块化

当应用中的js都以模块来编写的，那这个应用就是一个模块化的应用。

#### 组件化

当应用中的功能都是多组件的方式来编写的,那这个应用就是一个组件化的应用,。



### 非单文件组件

#### 组件的基本使用

- Vue中使用组件的三大步骤
  - 定义组件(创建组件)
  - 注册组件
  - 使用组件(写组件标签)



- 如何定义一个组件?

  使用Vue.extend(options)创建，其中options和new Vue(options)时传入的那个options几乎一样，但也有点区别;

  区别如下：

  - el不要写，为什么? 最终所有的组件都要经过一个vm的管理，由vm中的el决定服务哪个容器。
  - data必须写成函数，为什么? 避免组件被复用时，数据存在引用关系。

​        备注：使用template可以配置组件结构。



- 如何注册组件?

  - 局部注册：靠new Vue的时候传入components选项
  - 全局注册：靠Vue.component('组件名'，组件)

  

- 编写组件标签：

  - \<school>\</school>



##### 定义组件

```javascript
// 1.创建school组件
const school = Vue.extend({
    // 需要将html结构定义在组件中的template模板中
    template: `
                <div>
                    <h2>学校名称：{{schoolName}}</h2>
                    <h2>学校地址：{{address}}</h2>
                    <button @click='showName'>点我提示学校名</button>
                </div>
            `,

    // el不能在组件中使用，因为最终所有的组件都要被vm管理，由vm决定服务于哪个容器
    // el: '#root',

    // data只能使用函数式写法，不能使用对象式写法
    data() {
        return {
            schoolName: '尚硅谷',
            address: '北京昌平'
        }
    },

    methods: {
        showName() {
            alert(this.schoolName);
        }
    },
});

// 1.创建student组件
const student = Vue.extend({
    // 需要将html结构定义在组件中的template模板中
    template: `
                <div>
                    <h2>学生姓名：{{studentName}}</h2>
                    <h2>学生年龄：{{age}}</h2>
                </div>
            `,
    data() {
        return {
            studentName: '张三',
            age: 18
        }
    }

});
```



**注意事项：**

1. el不能在组件中使用，因为最终所有的组件都要被vm管理，由vm决定服务于哪个容器

![image-20220911171210272](../../../md-photo/image-20220911171210272.png)



2. data只能使用函数式写法，不能使用对象式写法

![image-20220911171326171](../../../md-photo/image-20220911171326171.png)



3. 需要将html结构定义在组件中的template模板中

```html
template: `
    <div>
        <h2>学校名称：{{schoolName}}</h2>
        <h2>学校地址：{{address}}</h2>
        <button @click='showName'>点我提示学校名</button>
    </div>
`
```



##### 注册组件

```javascript
// 2.注册组件（全局注册），全局注册组件后，可以被多个Vue实例引用
Vue.component('student', student);

// 创建vm
new Vue({
    el: '#root',
    // 2.注册组件（局部注册）
    components: {
        // 第一个为组件名（key），第二个为组件（value）
        // school: school,
        // student: student

        // key和value都一样的时候，直接使用简写形式
        school,
        // student
    },

    // Vue实例中可以存在自己的数据
    data: {
        msg: '你好啊'
    }
});
```



##### 使用组件

```html
<!-- 3.使用组件（引入组件的key值），编写组件标签 -->
<school></school>
```



#### 命令注意事项

- 关于组件名：
  - 一个单词组成：
    - 第一种写法(首字母小写)：school
    - 第二种写法(首字母大写)：School
  -   多个单词组成：
    - 第一种写法(kebab-case命名)：my-school
    - 第二种写法(CamelCase命名)：MySchool（需要Vue脚手架支持)
  - 备注：
    - 组件名尽可能回避HTML中已有的元素名称，例如：h2、H2都不行。
    - 可以使用name配置项指定组件在开发者工具中呈现的名字。
- 关于组件标签：
  - 第一种写法：\<school>\</school>
  - 第二种写法：\<school/>
  - 备注：不用使用脚手架时，\<school/>会导致后续组件不能渲染。
- 一个简写方式：
  - const school = Vue.extend(options）可简写为： const school = options



简写形式：

```javascript
const school = {
    // 可以在定义组件的时候就指定名字，在使用Vue开发者工具时，名字会以这里为准
    name: 'ori-school',
    template: `
                <div>
                    <h2>学校名称：{{name}}</h2>
                    <h2>学校地址：{{address}}</h2>
                </div>
            `,

    data() {
        return {
            name: '尚硅谷',
            address: '北京昌平'
        }
    },
};
```

其中，可以自定义name，最后的Vue开发者工具看到的就是该名字。



创建Vue实例：

**<font color='red'>使用大驼峰/HTML元素标签命名时，前端都不会展示对应的组件内容</font>**

```javascript
new Vue({
    el: '#root',
    components: {
        // MySchool: school
        H2: school,
    },

    data: {

    }
});
```



执行结果：

![image-20220912154311958](../../../md-photo/image-20220912154311958.png)



使用以下的方式注册组件：

```javascript
components: {
    school
},
```

使用以下的方式引用组件：

```html
<school></school>

<!-- 这种写法第二个school无法展示，只能在脚手架的环境下使用 -->
<school />
<school />
```

最终结果：

![image-20220912154619291](../../../md-photo/image-20220912154619291.png)



#### 组件的嵌套

组件之间还可以嵌套组件：

**<font color='red'>注意：组件A被组件B引用，那么需要在组件B中使用组件A；一般有一个管理其他所有组件的组件：App</font>**

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>

    </div>

    <script type="text/javascript">
        // 定义Student组件
        const student = {
            name: 'ori-student',
            template: `
                <div>
                    <h2>学生姓名：{{name}}</h2>
                    <h2>学生年龄：{{age}}</h2>
                </div>
            `,

            data() {
                return {
                    name: '张三',
                    age: 18
                }
            }
        };

        // 定义school组件
        const school = {
            name: 'ori-school',
            // student是school的组件，需要需要在school的模板中引用school组件
            template: `
                <div>
                    <h2>学校名称：{{name}}</h2>
                    <h2>学校地址：{{address}}</h2>
                    <student></student>
                </div>
            `,

            data() {
                return {
                    name: '尚硅谷',
                    address: '北京昌平'
                }
            },

            // 组件中还可以引入子组件（引用的组件需要在该组件定义之前）
            components: {
                student
            }
        };


        // 定义一个hello组件
        const hello = {
            name: 'ori-hello',
            template: `
                <div>
                    <h1>{{msg}}</h1>
                </div>
            `,
            data() {
                return {
                    msg: '欢迎来到尚硅谷学习！'
                }
            }
        };

        // 定义App组件，用于管理所有其他的组件
        const app = {
            template: `
                <div>
                    <hello></hello>
                    <school></school>
                </div>
            `,
            components: {
                hello,
                school
            }
        }

        // 创建Vue实例
        new Vue({
            template: `<app></app>`,
            el: '#root',
            // 注册组件（局部）
            components: {
                app
            }
        });
    </script>
</body>
```



最终结果:

![image-20220912160827454](../../../md-photo/image-20220912160827454.png)



#### VueComponent

- school组件本质是一个名为VueComponent的构造函数，且不是程序员定义的，是Vue.extend生成的。
- 我们只需要写\<school/>或\<school>\</school>，Vue解析时会帮我们创建school组件的实例对象，即vue帮我们执行的：new VueComponent(options)。
- 特别注意：每次调用Vue.extend，返回的都是一个全新的VueComponent!!!，**<font color='red'>而且此时的VueComponent是一个构造函数，使用\<school/>标签时才创建对应的实例</font>**。
- 关于this指向：
  - 组件配置中：data函数、methods中的函数、watch中的函数、computed中的函数它们的this均是【VueComponent实例对象】。
  - new Vue(options)配置中: data函数、methods中的函数、watch中的函数、computed中的函数它们的this均是【Vue实例对象】。
- VueComponent的实例对象，以后简称vc（也可称之为：组件实例对象），Vue的实例对象，以后简称vm。

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <school></school>
    </div>

    <script type="text/javascript">

        // 定义school组件
        // 这里返回的是VueComponent构造函数，真正的实例对象在使用<school/>标签引用的时候才创建
        const school = Vue.extend({
            name: 'ori-school',
            template: `
                <div>
                    <h2>学校名称：{{name}}</h2>
                    <h2>学校地址：{{address}}</h2>
                </div>
            `,

            data() {
                return {
                    name: '尚硅谷',
                    address: '北京昌平'
                }
            }
        });

        console.log("school", school);

        new Vue({
            el: '#root',
            components: {
                school
            }
        });
    </script>
</body>
```



这里输出的school是一个VueComponent构造函数：

![image-20220912162852713](../../../md-photo/image-20220912162852713.png)



每次调用Vue.extend（创建Vue组件），返回的都是一个新的VueCompent，新定义一个hello组件：

```javascript
const hello = Vue.extend({
    template: `
                <div>
                    <h2>{{msg}}</h2>
                </div>
            `,

    data() {
        return {
            msg: '欢迎来到尚硅谷学习'
        }
    }
});

console.log("school === hello ? ：", school === hello);
```



执行结果：

![image-20220912163233109](../../../md-photo/image-20220912163233109.png)



修改hello组件为以下内容：

```javascript
const hello = Vue.extend({
    template: `
                <div>
                    <h2>{{msg}}</h2>
                    <button @click='showMsg'>点我弹出信息</button>
                </div>
            `,

    data() {
        return {
            msg: '欢迎来到尚硅谷学习'
        }
    },

    methods: {
        showMsg() {
            console.log('VueComponent：', this);
            alert(this.msg);
        }
    },
});
```



得到结果：

![image-20220912163727930](../../../md-photo/image-20220912163727930.png)



#### 内置关系

##### js中的原型对象

可以显示的给实例对象添加属性，而这个属性又可以通过实例对象隐式的获取：

```javascript
// 定义一个构造函数
function Demo() {
    this.a = 1;
    this.b = 2;
};

// 创建一个Demo的实例对象
const d = new Demo();

console.log(Demo.prototype);// 显示原型属性

console.log(d.__proto__);// 隐式原型属性

// 通过显示原型属性操作原型对象，追加一个x属性，值为99
Demo.prototype.x = 99;

// 此时可以隐射的从实例对象中获取到x属性（类似于java的静态变量）
console.log("x = " + d.x);
console.log(d.__proto__.x === d.x);
```



程序运行结果：

![image-20220912170548867](../../../md-photo/image-20220912170548867.png)



##### Vue中的重要内置关系

1. 一个重要的内置关系：**<font color='red'>VueComponent.prototype._proto__ === Vue.prototype。</font>**
2. 为什么要有这个关系：**<font color='red'>让组件实例对象(vc）可以访问到 Vue原型上的属性、方法。</font>**

![image-20220912173829028](../../../md-photo/image-20220912173829028.png)



验证：

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <school></school>
    </div>

    <script type="text/javascript">
        // 定义school组件
        Vue.prototype.x = 99;// 对vm原型对象添加一个属性x
        // 这里返回的是构造函数，所以school只能使用prototype方法来获取原型对象
        const school = Vue.extend({
            name: 'ori-school',
            template: `
                <div>
                    <h2>学校名称：{{name}}</h2>
                    <h2>学校地址：{{address}}</h2>
                    <button @click='showX'>点我获取x属性</button>
                </div>
            `,

            data() {
                return {
                    name: '尚硅谷',
                    address: '北京昌平'
                }
            },

            methods: {
                // 可以在vc中获取vm中原型对象的属性
                showX() {
                    alert(this.x);
                }
            },
        });

        new Vue({
            el: '#root',
            data: {
                msg: '你好'
            },

            components: {
                school
            }
        });

        // vc的原型对象的原型对象 === vm的原型对象
        console.log("school.prototype.__proto__ === Vue.prototype?：" + (school.prototype.__proto__ === Vue.prototype));
    </script>
</body>
```



得到结果：

![image-20220912175036072](../../../md-photo/image-20220912175036072.png)



### 单文件组件

#### 暴露组件

直接暴露出vue组件（常用）：

```javascript
export default {/*vc组件的相关内容*/};
```

默认暴露：

```javascript
const school = {...};
export default school;
```

单个暴露：

```javascript
export const student = {...};
```

统一暴露：

```javascript
const school = {...};
export { school };
```



#### Vue脚手架

参见：./Vue环境搭建.md & 下一章节内容



#### 普通组件

<font color='red'>**其中template写组件的结构（html元素）、script中写数据、方法（js）等、style中写样式。**</font>

School.vue：

```vue
<template>
  <!-- 组件的结构 -->
  <div class="demo">
    <h2>学校名称：{{name}}</h2>
    <h2>学校地址：{{address}}</h2>
    <button @click="showName">点我提示学校名</button>
  </div>
</template>

<script>
// 组件交互的代码（数据、方法等等）
// 直接暴露出Vue的组件
export default {
  name: "School",// 定义组件的名称
  data() {
    return {
      name: "尚硅谷",
      address: "北京昌平"
    };
  },

  methods: {
    showName() {
      alert(this.name);
    }
  }
};

// ======暴露组件======
// 默认暴露（常用）
// export default school;
// 单个暴露
// export const student = {...};
// 统一暴露
// export { school };
</script>

<style>
/* 组件的样式 */
.demo {
  background-color: orange;
}
</style>
```



Student.vue：

```vue
<template>
  <div>
    <h2>学生名称：{{name}}</h2>
    <h2>学生年龄：{{age}}</h2>
  </div>
</template>

<script>
export default {
  name: "Student",
  data() {
    return {
      name: "张三",
      age: 18
    };
  }
};
</script>
```



#### 总组件

App.vue：

```vue
<template>
  <div>
    <img src="./assets/logo.png"/>
    <School></School>
    <Student></Student>
  </div>
</template>

<script>
// 引入组件
import School from './components/School';
import Student from "./components/Student";

export default {
  name: "App",
  components: {
    School,
    Student
  }
};
</script>
```



整体结构：

![image-20220912215808048](../../../md-photo/image-20220912215808048.png)



#### 运行结果

![image-20220912215928179](../../../md-photo/image-20220912215928179.png)



## Vue脚手架

### 基本介绍

src/main.js是<font color='red'>整个项目的入口文件</font>

```javascript
/**
 * 该文件是整个项目的入口文件
 */
// 引入vue
import Vue from 'vue'
// 引入App组件，它是所有组件的父组件
import App from './App.vue'

// 关闭vue的生产提示
Vue.config.productionTip = false

// 创建服务Vue实例对象
new Vue({
  // 将App组件放入容器中
  render: h => h(App),
}).$mount('#app')
```



public/index.html是定义容器的地方：<font color='red'>注意其中的<%= BASE_URL %>指定的public路径</font>

```html
<!DOCTYPE html>
<html lang="">
  <head>
    <meta charset="utf-8">
    <!-- 针对IE浏览器的一个特殊配置，让IE浏览器以最好的渲染级别渲染页面 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- 开启移动端的理想视口 -->
    <meta name="viewport" content="width=device-width,initial-scale=1.0">
    <!-- 配置页签的图标 <%= BASE_URL %>指定的public路径 -->
    <link rel="icon" href="<%= BASE_URL %>favicon.ico">
    <!-- 配置网页标签 -->
    <title><%= htmlWebpackPlugin.options.title %></title>
  </head>
  <body>
    <!-- 如果浏览器不支持js，提示以下的内容 -->
    <noscript>
      <strong>We're sorry but <%= htmlWebpackPlugin.options.title %> doesn't work properly without JavaScript enabled. Please enable it to continue.</strong>
    </noscript>

    <!-- 定义一个app容器 -->
    <div id="app"></div>
    <!-- built files will be auto injected -->
  </body>
</html>
```



### render函数

main.js中默认引入的vue是残缺版的vue，不能解析模板：

![image-20220912223646312](../../../md-photo/image-20220912223646312.png)

![image-20220912223705372](../../../md-photo/image-20220912223705372.png)



#### 完整的vue

在main.js中引入正常的vue：

```js
// 引入完整版vue
import Vue from 'vue/dist/vue.js'

new Vue({
  el: '#app',
  // 需要引入完整版vue
  template: `<h2>你好啊</h2>`
});
```



此时可以正常解析模板：

![image-20220912223921802](../../../md-photo/image-20220912223921802.png)

#### 残缺的vue

也可以引入残缺版的vue，使用render函数创建元素：

```javascript
// 引入残缺版vue
import Vue from 'vue'

new Vue({
  el: '#app',
  // 只需要引入残缺版的vue即可
  // 使用render函数创建元素
  render(createElement) {
    return createElement('h2', '你好啊');
  },
  // render的简写形式
  // render: h => h('h2', '你好啊'),
});
```

此时页面同样可以正常解析模板：

![image-20220912224342379](../../../md-photo/image-20220912224342379.png)

由render的简写形式可以得到<font color='red'>**render: h => h(App)，指的是通过App来创建对应的元素**</font>。



#### 不同版本vue的区别

-  vue.js与vue.runtime.xxx.js的区别：
  - vue.js是完整版的Vue，包含：核心功能+模板解析器。
  - vue.runtime.xxx.js是运行版的Vue，只包含：核心功能；没有模板解析器。
- 因为vue.runtime.xxx.js没有模板解析器，所以不能使用template配置项，需要使用render函数接收到的createElement函数去指定具体内容。



### 修改默认配置

参见官网的配置：https://cli.vuejs.org/zh/config/

修改完代码后，编译js报错：

```bash
ERROR in [eslint]                                                                                                                          
D:\Vue\vue_test\src\components\School.vue
  14:9  error  Component name "School" should always be multi-word  vue/multi-word-component-names

D:\Vue\vue_test\src\components\Student.vue
  10:9  error  Component name "Student" should always be multi-word  vue/multi-word-component-names
```

在 *vue.config.js* 中关闭语法提示，**<font color='red'>重启Vue的服务</font>**即可。

```javascript
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false, //关闭eslint检查
})
```



## ref属性

1. 被用来给元素或子组件注册引用信息（id的替代者）
2. 应用在html标签上获取的是真实DOM元素，应用在组件标签上是组件实例对象（vc）
3. 使用方式：
   1. 打标识：```<h1 ref="xxx">.....</h1>``` 或 ```<School ref="xxx"></School>```
   2. 获取：```this.$refs.xxx```



实例：

School组件：

```vue
<template>
  <div class="school">
    <h2>学校名称：{{name}}</h2>
    <h2>学校地址：{{address}}</h2>
  </div>
</template>

<script>
export default {
  name: "School",
  data() {
    return {
      name: "尚硅谷",
      address: "北京昌平"
    };
  }
};
</script>

<style>
.school {
  background-color: gray;
}
</style>
```



App组件：

```vue
<template>
  <div>
    <h1 v-text="msg" ref='title' id="title"></h1>
    <button ref="btn" @click="showDOM">点我输出上方的DOM元素</button>
    <School ref="sch" id = "sch"/>
  </div>
</template>

<script>
import School from "./components/School";

export default {
  name: "App",
  components: {
    School
  },

  data() {
    return {
      msg: "欢迎学习vue"
    };
  },

  methods: {
    showDOM() {
      // 使用ref获取元素（id的替代者）
      console.log(document.getElementById('title'));
      console.log(this.$refs.title);
      // 获取所有标注有ref属性的元素
      console.log(this.$refs);

      // 这里获取到的是一个vc组件
      console.log(this.$refs.sch);
      // 这里获取到的是School组件的真实dom元素
      console.log(document.getElementById("sch"));
    }
  }
};
</script>
```



最终结果：

![image-20220913232804646](../../../md-photo/image-20220913232804646.png)



**<font color='red'>之后的所有vue组件只挑选重要部分进行笔记，完整项目参见</font>**：[牟光俊/vue-cli (gitee.com)](https://gitee.com/guang_jun_mu/vue-cli)
