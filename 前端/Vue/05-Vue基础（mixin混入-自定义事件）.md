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



## TodoList案例

### 组件拆分

组件分为以下的4块：
![image-20220921195046332](../../../md-photo/image-20220921195046332.png)



其中Item数据本应该放到List组件中，但是由于Header添加的元素也需要放到List，且Header和List不能直接沟通，所以暂时将数据放到App中，让它们之间可以直接交互。



### 列表展示

App中存放数据并将数据（todos）传递到List中：

```vue
<template>
  <!-- 将todos中的数据传递到list -->
  <List :todos="todos"/>
</template>

<script>

import List from "./components/List";

export default {
  components: { List },
  data() {
    return {
      todos: [
        { id: "00001", title: "吃饭", done: true },
        { id: "00002", title: "睡觉", done: true },
        { id: "00003", title: "抽烟", done: false }
      ]
    };
  }
};
</script>
```



List遍历数据，并将每一个元素的具体展示（todo）交给Item来进行展示：

```vue
<template>
  <div>
    <ul class="todo-main">
      <Item
        v-for="todoObj in todos"
        :key="todoObj.id"
        :todo="todoObj" />
    </ul>
  </div>
</template>

<script>
import Item from "./Item";

export default {
  name: "List",
  components: { Item },
  props: ["todos"]
};
</script>
```



Item中展示具体的元素：

```vue
<template>
  <div>
    <li>
      <label>
        <input :id="todo.id" type="checkbox" :checked="todo.done" />
        <span>{{todo.title}}</span>
      </label>
      <button class="btn btn-danger">删除</button>
    </li>
  </div>
</template>

<script>
export default {
  name: "Item",
  props: ["todo"]
};
</script>
```



### 添加Todo

props可以**<font color='red'>操作函数进行传递（利用传递过来的函数修改传递方的数据）</font>**，以下案例就是App将函数传递给MyHeader，然后MyHeader调用函数进行相应的操作。此时MyHeader和List都可以对数据todos进行操作。

App:

```vue
<template>
  <MyHeader :addTodo="addTodo" />
</template>

<script>
export default {
  data() {
    return {
      todos: [
        { id: "00001", title: "吃饭", done: true },
        { id: "00002", title: "睡觉", done: true },
        { id: "00003", title: "抽烟", done: false }
      ]
    };
  },

  methods: {
    addTodo(todoObj) {
      this.todos.unshift(todoObj);
    }
  }
};
</script>
```



MyHeader：

```vue
<script>
export default {
  methods: {
    add(event) {
      // 将用户的输入包装成一个对象
      const todoObj = { id: nanoid(), title: event.target.value, done: false };
      this.addTodo(todoObj);
    }
  },

  props: ["addTodo"]
};
</script>
```



### 勾选Todo

正常的传递顺序为App -> List -> Item，那么如果需要从App传递到Item的话，最基础的做法就是利用List来进行传递了。传递完成之后，在Item中选择对应的todo编号，让后交给App将todo的done值取反。

App：

```vue
<template>
	<List :todos="todos" :checkTodo="checkTodo" />
</template>

<script>
export default {
  methods: {
    checkTodo(id) {
      this.todos.forEach(todo => {
        if (todo.id === id) {
          // 执行取消勾选的操作
          todo.done = !todo.done;
        }
      });
    }
  }
};
</script>
```



List进行传递的操作：

```vue
<template>
	<Item v-for="todoObj in todos" :key="todoObj.id" :checkTodo="checkTodo" />
</template>

<script>
export default {
  props: ["checkTodo"]
};
</script>
```



Item使用传递过来的方法：

```vue
<template>
	<input :id="todo.id" type="checkbox" :checked="todo.done" @change="handleCheck(todo.id)" />
</template>

<script>
export default {
  props: ["checkTodo"],
  methods: {
    handleCheck(id) {
      this.checkTodo(id);
    }
  }
};
</script>
```



### Footer操作

#### 已完成/未完成

使用计算属性统计已完成和未完成的todo的数量。

```vue
<template>
	<span>
    <span>已完成{{doneTotal}}</span>
    / 全部{{total}}
    </span>
</template>

<script>
export default {
  computed: {
    total() {
      return this.todos.length;
    },

    doneTotal() {
      // const x = this.todos.reduce((pre, current) => {
      //   // pre是上次规约执行后的值，current是此次规约的item元素
      //   // console.log("@", pre, current);
      //   return pre + (current.done ? 1 : 0);
      // }, 0);
      // 使用规约的方式统计已经完成的todo个数
      return this.todos.reduce((pre, todo) => pre + (todo.done ? 1 : 0), 0);
    }
  },
  props: ["todos", "checkAllTodo"]
};
</script>
```



#### 隐藏底部统计信息

total计算属性参见上一步。

```vue
<template>
  <!-- 0 -> false, 其他 -> true -->
  <div class="todo-footer" v-show="total">
  </div>
</template>
```



#### 全选与全不选

##### 复杂方式

App存放对todos操作的方法：

```vue
<template>
  <MyFooter :todos="todos" :checkAllTodo="checkAllTodo" />
</template>


export default {
  methods: {
    // 全选or取消全选
    checkAllTodo(done) {
      this.todos.forEach(todo => (todo.done = done));
    }
  }
};
</script>
```



MyFooter中存放着前端的操作事件：

```vue
<template>
    <!-- 如果全部todo都勾选，底部也需要被勾选 -->
    <input type="checkbox" :checked="isAll" @change="checkAll" />
</template>

<script>
export default {
  computed: {
    isAll: {
      get() {
        return this.total !== 0 && this.total === this.doneTotal;
      }
    }
  },

  methods: {
    checkAll(event) {
      // 获取checkbox是否被选择
      // console.log(event.target.checked);
      this.checkAllTodo(event.target.checked);
    }
  },

  props: ["todos", "checkAllTodo"]
};
</script>
```



##### 简单方式

App同上一步，MyFooter使用v-model进行双向绑定，然后**<font color='red'>使用计算属性的set方法进行全选/全不选</font>**。

```vue
<template>
    <!-- 简写方式，直接通过计算属性的setter方法对todos进行操作 -->
    <input type="checkbox" v-model="isAll" />
</template>

<script>
export default {
  computed: {
    isAll: {
      set(value) {
        this.checkAllTodo(value);
      },

      get() {
        return this.total !== 0 && this.total === this.doneTotal;
      }
    }
  }

  props: ["todos", "checkAllTodo"]
};
</script>
```



#### 清除已经完成的任务

同样的操作todos的方法定义到App中，不再过多描述：

```vue
<template>
  <MyFooter :todos="todos" :clearAllTodo="clearAllTodo" />
</template>

<script>
export default {
  methods: {
    // 清除所有已经完成的todo
    clearAllTodo() {
      this.todos = this.todos.filter(todo => !todo.done);
    }
  }
};
</script>
```



MyFooter：

```vue
<template>
  <button class="btn btn-danger" @click="clearAllTodo">清除已完成任务</button>
</template>

<script>
export default {
  props: ["clearAllTodo"]
};
```



### JS使用技巧

#### 删除集合中的一个元素

```javascript
// 删除一个todo
deleteTodo(id) {
    this.todos = this.todos.filter(todo => todo.id !== id);
}
```



#### 根据条件统计元素个数

```javascript
// 使用规约的方式进行统计
const x = this.todos.reduce((pre, current) => {
    // pre是上次规约执行后的值，current是此次规约的item元素
    // console.log("@", pre, current);
    return pre + (current.done ? 1 : 0);
}, 0);
// 使用规约的方式统计已经完成的todo个数
return this.todos.reduce((pre, todo) => pre + (todo.done ? 1 : 0), 0);
```



### 总结

- 组件化编码流程：
  - 拆分静态组件：组件要按照功能点拆分，命名不要与html元素冲突。
  - 实现动态组件：考虑好数据的存放位置，数据是一个组件在用，还是一些组件在用：
    - 一个组件在用：放在组件自身即可。
    - 一些组件在用：放在他们共同的父组件上（状态提升）。
  - 实现交互：从绑定事件开始。
- props适用于：
  - 父组件 ==> 子组件 通信
  - 子组件 ==> 父组件 通信（要求父先给子一个函数）
- 使用v-model时要切记：v-model绑定的值不能是props传过来的值，因为props是不可以修改的！
- props传过来的若是对象类型的值，修改对象中的属性时Vue不会报错，但不推荐这样做。



## 浏览器本地缓存

### localStorage

浏览器中的查看位置：

![image-20220925213015783](../../../md-photo/image-20220925213015783.png)



常用Api：

#### setItem

保存一个本地缓存文件，<font color='red'>localStorage只能存储字符串，如果需要存储对象，则需要转换为Json的方式进行存储</font>

```javascript
<script type="text/javascript">
    let p = { name: '张三', age: 18 }
    function saveData() {
        // window上的属性可以直接使用
        localStorage.setItem('msg', '哈哈');
        // 将对象以json的方式进行存储，存储结果：{"name":"张三","age":18}
        localStorage.setItem('personJson', JSON.stringify(p));
    }
</script>
```



#### getItem

读取缓存文件中的内容，<font color='red'>当读取一个并没有存进去的值时，得到的结果是null值</font>

```javascript
function readData() {
    console.log(localStorage.getItem('msg'));
    // 读取到的json转换为对象
    const person = localStorage.getItem('personJson');
    console.log(JSON.parse(person));
}
```



#### removeItem

删除指定的key值缓存

```javascript
function deleteData() {
    localStorage.removeItem('msg2');
}
```



#### clear

清空所有的缓存

```javascript
function clearData() {
    localStorage.clear();
}
```



### sessionStorage

和localSession类似，不过它是会话级别的缓存，浏览器关闭之后数据就被清除了。<font color='red'>localSession只有用户主动清除浏览器缓存，或者系统调用清除的api进行删除，数据才会消失</font>。



### TodoList数据缓存

将TodoList的数据存储到本地缓存，防止页面刷新的时候数据丢失。

在App中修改数据的处理方式即可。<font color='red'>如果缓存为空，则给一个空数据，防止todos.length报错</font>。

```javascript
data() {
    return {
        // 数据todos从浏览器本地缓存中读取，但是如果缓存为空，则给一个空数据，防止todos.length报错
        todos: JSON.parse(localStorage.getItem("todos")) || []
    };
}
```



使用watch属性监视data中的todos发生变化时，同时通知localStorage缓存中的内容发生更改。<font color='red'>需要开启深度监视，不然对象中的内容发生变化时，vue无法监视到</font>.

```javascript
// 使用watch属性，如果检测到todos发生变化，则往本地缓存中储存一份
watch: {
    // 将修改后的值存放到本地缓存中
    todos: {
        // 开启深度监视，修改done值的时候，也能监视到
        deep: true,
            handler(newValue) {
            localStorage.setItem("todos", JSON.stringify(newValue));
        }
    }
}
```



### 总结

1. 存储内容大小一般支持5MB左右（不同浏览器可能还不一样）

2. 浏览器端通过 Window.sessionStorage 和 Window.localStorage 属性来实现本地存储机制。

3. 相关API：

   1. `xxxxxStorage.setItem('key', 'value');` 该方法接受一个键和值作为参数，会把键值对添加到存储中，如果键名存在，则更新其对应的值。

   2. `xxxxxStorage.getItem('person');`

      ​	该方法接受一个键名作为参数，返回键名对应的值。

   3. `xxxxxStorage.removeItem('key');`

      ​	该方法接受一个键名作为参数，并把该键名从存储中删除。

   4. `xxxxxStorage.clear()`

      ​	该方法会清空存储中的所有数据。

4. 备注：

   1. SessionStorage存储的内容会随着浏览器窗口关闭而消失。
   2. LocalStorage存储的内容，需要手动清除才会消失。
   3. `xxxxxStorage.getItem(xxx)`如果xxx对应的value获取不到，那么getItem的返回值是null。
   4. `JSON.parse(null)`的结果依然是null。



## 组件的自定义事件

### 绑定事件

#### 直接在组件上绑定

App组件：

```vue
<template>
  <div class="app">
    <!-- 通过父组件给子组件绑定一个自定事件实现：子给父传递数据（第一种写法，使用v-on/@符号） -->
    <Student v-on:stuNameEvent="getStudentName" />
    <!-- 仅需要执行一次 /> -->
    <!-- <Student v-on:stuNameEvent.once="getStudentName" /> -->
  </div>
</template>

<script>
export default {
  methods: {
    // 使用Es6的写法，第一个接收到的参数是name，其他的参数封装为一个数组
    getStudentName(name, ...params) {
      console.log("App收到了学生的名字", name, params);
    }
  }
};
</script>
```



Student组件触发对应的事件：

```vue
<template>
	<button @click="sendStudentName">把学生名给App</button>
</template>

<script>
export default {
  methods: {
    sendStudentName() {
      // 触发Student组件实例身上的stuNameEvent事件
      // 传递多个参数
      this.$emit("stuNameEvent", this.name, "您好", "hello", "扣你几哇");
    }
  }
};
</script>
```



执行结果：

![image-20220925231803004](../../../md-photo/image-20220925231803004.png)



#### 使用ref的方式绑定

Student组件的触发事件不变，修改App组件在页面挂载后，绑定对应的事件。<font color='red'>这种方式更灵活，可以做更多的操作，比如设置定时器等</font>。

```vue
<template>
  <div class="app">
    <!-- 通过父组件给子组件绑定一个自定事件实现：子给父传递数据（第二种写法，使用ref） -->
    <Student ref="student" />
  </div>
</template>

<script>
export default {
  methods: {
    // 使用Es6的写法，第一个接收到的参数是name，其他的参数封装为一个数组
    getStudentName(name, ...params) {
      console.log("App收到了学生的名字", name, params);
    }
  },
  
  mounted() {
    // 可以过段时间再绑定事件，可以做其他更多的操作
    // setTimeout(() => {
    //   this.$refs.student.$on("stuNameEvent", this.getStudentName);
    // }, 3000);

    // 该事件只触发一次
    this.$refs.student.$once("stuNameEvent", this.getStudentName);
  }
};
</script>
```



执行结果：

![image-20220925232203341](../../../md-photo/image-20220925232203341.png)



### 解除绑定

App中再添加一个自定义事件event1:

```vue
<template>
	<Student ref="student" @event1="event1"/>
</template>

<script>
export default {
  methods: {
    event1() {
      console.log("event1被调用了。。。");
    }
  }
};
</script>
```



Student组件中添加解绑函数：

```vue
<template>
	<button @click="unbind">解除绑定事件</button>
</template>

<script>
export default {
  methods: {
    unbind() {
      // 解绑一个自定义事件
      // this.$off("stuNameEvent");

      // 解绑多个自定义事件
      // this.$off(["stuNameEvent", "event1"]);

      // 解绑所有的自定义事件
      this.$off();
    }
  }
};
</script>
```



测试结果：

![image-20220926195103723](../../../md-photo/image-20220926195103723.png)



**<font color='red'>VM、VC实例销毁后，对应VC组件上的自定义事件都会被销毁掉。</font>**

Student中添加销毁方法：

```vue
<template>
    <button @click="death">点我销毁vc实例对象</button>
</template>

<script>
export default {
  methods: {
    death() {
      // 销毁了当前Student组件实例，销毁后所有的Student实例的自定义事件都失效
      this.$destroy();
    }
  }
};
</script>
```



测试结果：

![image-20220926195357966](../../../md-photo/image-20220926195357966.png)

### 注意事项

#### this指向问题

App组件：

```vue
<template>
  <div class="app">
    <h2>{{msg}}， 学生姓名是：{{studentName}}</h2>
    <Student ref="student" />
  </div>
</template>

<script>
export default {
  data() {
    return {
      msg: "您好啊",
      studentName: ""
    };
  },

  methods: {
    getStudentName(name, ...params) {
      console.log("App收到了学生的名字", name, params);
      this.studentName = name;
    }
  },

  mounted() {
    // App组件的methods中存放getStudentName函数，此时的this是App
    // this.$refs.student.$on("stuNameEvent", this.getStudentName);

    // 使用普通函数绑定，此时的this是Studnet
    // this.$refs.student.$on("stuNameEvent", function(name, ...params) {
    //   this.studentName = name;
    //   console.log(this); // 此时的this是student组件对象
    // });

    // 使用箭头函数，将this指向app
    this.$refs.student.$on("stuNameEvent", (name, ...params) => {
      console.log(this); // 此时的this是App组件
      this.studentName = name;
    });
  }
};
</script>
```



**App组件的methods中存放getStudentName函数，此时的this是App**

![image-20220926201920598](../../../md-photo/image-20220926201920598.png)

**使用普通函数绑定，此时的this是Studnet**

![image-20220926202331201](../../../md-photo/image-20220926202331201.png)



**使用箭头函数，将this指向app**

![image-20220926202438723](../../../md-photo/image-20220926202438723.png)



#### dom原始事件使用

<font color='red'>**需要添加.native后缀修饰，否则会被vue认为是一个自定义事件**</font>。

```vue
<template>
    <Student @click.native="show"/>
</template>

<script>
export default {
  methods: {
    show() {
      alert("native event");
    }
  }
};
</script>
```



测试结果：

![image-20220926202923386](../../../md-photo/image-20220926202923386.png)



### 总结

1. 一种组件间通信的方式，适用于：<strong style="color:red">子组件 ===> 父组件</strong>

2. 使用场景：A是父组件，B是子组件，B想给A传数据，那么就要在A中给B绑定自定义事件（<span style="color:red">事件的回调在A中</span>）。

3. 绑定自定义事件：

   - 第一种方式，在父组件中：```<Demo @atguigu="test"/>```  或 ```<Demo v-on:atguigu="test"/>```
   - 第二种方式，在父组件中：

   ```js
   <Demo ref="demo"/>
   ......
   mounted(){
      this.$refs.xxx.$on('atguigu',this.test)
   }
   ```

   - 若想让自定义事件只能触发一次，可以使用```once```修饰符，或```$once```方法。

4. 触发自定义事件：```this.$emit('atguigu',数据)```    

5. 解绑自定义事件```this.$off('atguigu')```

6. 组件上也可以绑定原生DOM事件，需要使用```native```修饰符。

7. 注意：通过```this.$refs.xxx.$on('atguigu',回调)```绑定自定义事件时，回调<span style="color:red">要么配置在methods中</span>，<span style="color:red">要么用箭头函数</span>，否则this指向会出问题！