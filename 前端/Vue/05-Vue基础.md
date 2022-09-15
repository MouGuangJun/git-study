# Vue基础

## mixin混入

### 基础使用

可以将多个组件中的公共代码部分抽取到一个单独的文件，然后在使用的时候再进行引入（相当于java的工具类）。

App中引入Student和School。

Student：

```vue
<template>
  <div>
    <h2 @click="showName">学生姓名：{{name}}</h2>
    <h2>学生性别：{{sex}}</h2>
  </div>
</template>

<script>
export default {
  name: "Student",
  data() {
    return {
      name: "张三",
      sex: "男"
    };
  },

  methods: {
    showName() {
      alert(this.name);
    }
  }
};
</script>
```



School：

```vue
<template>
  <div>
    <h2 @click="showName">学校姓名：{{name}}</h2>
    <h2>学校地址：{{address}}</h2>
  </div>
</template>

<script>
// 引入混合
import { mixin, data_mixin } from "./../mixin";

export default {
  name: "School",
  data() {
    return {
      name: "尚硅谷",
      address: "北京昌平",
      x: 800
    };
  },

  mounted() {
    console.log("你好啊！！！！！！！！！");
  },

  // 混合配置
  mixins: [mixin, data_mixin]
};
</script>
```



mixin.js：

```javascript
export const mixin = {
    methods: {
        showName() {
            alert(this.name);
        }
    },

    mounted() {
        console.log("你好啊，mixin！！！");
    },
};

export const data_mixin = {
    data() {
        return {
            x: 100,
            y: 200
        }
    },
}
```



可以引入多个mixin，具体做法参见以上内容，如果mixin和源文件中都有的属性，以源文件的为准。如果是生命周期钩子则都会执行（mixin的先执行，源文件的后执行）。

执行结果：

![image-20220914201257723](../../../md-photo/image-20220914201257723.png)

![image-20220914201328349](../../../md-photo/image-20220914201328349.png)



### 全局混入

**<font color='red'>不推荐使用的配置</font>**

在main.js中添加添加混入内容：

```javascript
import {mixin, data_mixin} from './mixin'

// 配置全局混入
Vue.mixin(mixin);
Vue.mixin(data_mixin);
```



执行结果：

![image-20220914202004223](../../../md-photo/image-20220914202004223.png)



## 插件

定义插件，插件中需要有install函数，**<font color='red'>接收到的第一个参数是Vue，第二个开始的参数是自定义参数，可以在Vue身上配置一些公共的方法或者函数</font>**：

```javascript
// 定义一个插件，必须有install函数
export default {
    // 接收到的参数是Vue
    install(Vue, CustParam) {
        console.log(CustParam);

        // console.log("install", Vue);
        // 定义全局的过滤器
        Vue.filter('splitYear', function (val) {
            return val.split('-')[0];
        });


        // 定义全局的自定指令，对象的方式
        Vue.directive('fbind', {

            // 指令与元素成功绑定时调用（一上来就调用）
            bind(element, binding) {
                // console.log("bind");
                element.value = binding.value;
            },

            // 指令所在的元素插入页面时
            inserted(element, binding) {
                // console.log("inserted");
                element.focus();
            },

            // 指令所在的模板被重新解析时
            update(element, binding) {
                // console.log("update");
                element.value = binding.value;
                // element.focus();
            }
        });

        // function的方式
        Vue.directive('big-number', function (element, binding) {
            element.innerText = binding.value * 10;
        });

        // 定义混入
        Vue.mixin({
            // 混入函数
            methods: {
                showName() {
                    alert(this.name);
                }
            },

            // 混入生命周期钩子（都会执行）
            mounted() {
                console.log("你好啊，mixin！！！");
            },
        });

        // 给Vue原型上添加一个方法（vm和vc就都能用了）
        Vue.prototype.hello = () => { alert("您好啊！") };
    }
}
```



使用插件：

在main.js中引入并使用插件：

```javascript
// 引入插件
import plugins from "./plugins";

// 使用插件
Vue.use(plugins, "自定义的参数");
```



Vue组件中使用插件：

```vue
<template>
  <div>
    <!-- 测试Vue插件定义的过滤器 -->
    <h2>当前时间：{{time | splitYear}}</h2>
    <h2>学校姓名：{{name}}</h2>
    <h2>学校地址：{{address}}</h2>
    <!-- 测试Vue自定义的指令 -->
    <input type="text" v-fbind:value='"自动获取焦点"' />
    <!-- 测试全局Vue插件原型上的方法 -->
    <button @click="hello">点我测试hello方法</button>
  </div>
</template>

<script>
export default {
  name: "School",
  data() {
    return {
      time: "2022-09-14",
      name: "尚硅谷",
      address: "北京昌平"
    };
  }
};
</script>
```



测试结果：

![image-20220914204652156](../../../md-photo/image-20220914204652156.png)

插件中还可以接收到自定义的参数：

![image-20220914204914882](../../../md-photo/image-20220914204914882.png)



## scoped样式

### 全局样式问题

默认的style样式是全局共享的。对相同的class赋予不同的属性时，**<font color='red'>以后引入的组件样式为主，推荐在App.vue中使用该方式来全局共享</font>**。

School：

```vue
<template>
  <div class="demo">
      ...
  </div>
</template>

<style>
.demo {
  background-color: skyblue;
}
</style>
```



Student：

```html
<template>
  <div class="demo">
      ...
  </div>
</template>

<style>
.demo {
  background-color: orange;
}
</style>
```



App中引入顺序：

```javascript
import School from "./components/School";
import Student from "./components/Student";
```



最终结果：

可以看到以Student的样式为主。

![image-20220914211838783](../../../md-photo/image-20220914211838783.png)

### 使用scoped解决该问题

修改Student和School的style标签：

```html
<style scoped></style>
```

成功的获取到了自己的样式。

![image-20220914212654239](../../../md-photo/image-20220914212654239.png)

App中样式共享：

App：

```vue
<template>
  <div>
    <h2 class="title">App红色的</h2>
	...
  </div>
</template>

<style>
.title {
  color: red;
}
</style>
```



Student：

```vue
<template>
  <div class="demo">
    <h2 class="title">学生姓名：{{name}}</h2>
    <h2>学生性别：{{sex}}</h2>
  </div>
</template>
```



成功的全局共享了样式：

![image-20220914213231521](../../../md-photo/image-20220914213231521.png)



