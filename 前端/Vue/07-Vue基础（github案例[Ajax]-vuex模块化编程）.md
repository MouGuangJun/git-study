# Vue基础

## Github案例

### 静态组件

具体的拆解参见gitee上面的，这里只说重要的事情·。

引入第三方样式bootstrap.css

方式一：bootstrap.css放在public文件夹下（<font color='red'>**推荐使用**</font>），通过link的方式引入样式：

![image-20221003220751060](../../../md-photo/image-20221003220751060.png)



方式二，通过js的方式引入样式（<font color='red'>**不推荐使用**</font>）：

![image-20221003212133603](../../../md-photo/image-20221003212133603.png)

这种情况下vue会检查bootstrap的所有样式资源，此时会检查报错：

![image-20221003212244980](../../../md-photo/image-20221003212244980.png)



### github接口地址

搜索所有github用户的<font color='red'>**GET请求**</font>：

https://api.github.com/search/users?q=xxx

```json
"items": [
    {
        # 用户的头像地址
        "avatar_url": "https://avatars.githubusercontent.com/u/383316?v=4"
        # 用户主页地址
        "html_url": "https://github.com/test"
        # 用户登录名
        "login": "test"
    }
]
```



实例：

Search.vue中负责调用ajax请求查询数据，并通过全局事件总线将数据传递给List：

```vue
<script>
export default {
  methods: {
    searchUsers() {
      // 发送Ajax请求github服务器
      // 使用es6的模板插入语法
      axios.get(`https://api.github.com/search/users?q=${this.keyWord}`).then(
        response => {
          // 将搜索返回的数据发送到List组件进行使用
          this.$bus.$emit("users", response.data.items);
        },
        error => {
          console.log("请求发送失败！");
        }
      );
    }
  }
};
</script>
```

List.vue负责从Search中接收数据，并将数据渲染到页面上：

```vue
<template>
  <div class="row">
    <!-- 用户的登录作为key值，保证不重复 -->
    <div class="card" v-for="user in users" :key="user.login">
      <!-- 点击访问用户对应的主页 -->
      <a class="remote" :href="user.html_url" target="_blank">
        <!-- 用户的头像图片 -->
        <img :src="user.avatar_url" style="width: 100px" />
      </a>
      <!-- 展示用户的登录名 -->
      <p class="card-text">{{user.login}}</p>
    </div>
  </div>
</template>

<script>
export default {
  mounted() {
    // 绑定接受用户数据的自定义事件
    this.$bus.$on("users", users => (this.users = users));
  },

  beforeDestroy() {
    this.$bus.$off("users");
  }
};
</script>
```

测试结果：

![image-20221003224112207](../../../md-photo/image-20221003224112207.png)



### 案例完善

welcome：未发送请求前展示提示信息。

loading：发送请求后等待服务器响应提示信息。

users：服务端响应后的列表数据展示。

error：请求失败之后展示错误的信息。

<font color='red'>**在发送Ajax请求过程前后，将状态值通过对象的方式传递给List，List通过对应的状态值展示对应的提示信息**</font>。

通过pubsub消息发布订阅的方式进行数据的传递：

Search.vue：

```vue
<script>
export default {
  methods: {
    searchUsers() {
      // 发送Ajax请求github服务器
      // 使用es6的模板插入语法
      // 关闭欢迎使用的提示，展示正在加载中
      pubsub.publish("updateList", {
        isFirst: false,
        isLoading: true,
        users: []
      });
      axios.get(`https://api.github.com/search/users?q=${this.keyWord}`).then(
        response => {
          // 展示列表的数据信息
          pubsub.publish("updateList", {
            isLoading: false,
            users: response.data.items
          });
        },
        error => {
          // 展示错误信息
          pubsub.publish("updateList", {
            isLoading: false,
            errorMsg: error.message,
            users: []
          });
        }
      );
    }
  }
};
</script>
```

List.vue，<font color='red'>**注意：this.info = { ...this.info, ...dataObj }是复制两个对象之间的属性值**</font>：

```vue
<script>
export default {
  data() {
    return {
      info: {
        isFirst: true,
        isLoading: false,
        errorMsg: "",
        users: []
      }
    };
  },
  mounted() {
    // 消息订阅的方式接收数据
    this.pubId = pubsub.subscribe(
      "updateList",
      // 如果info和dataObj中都存在的熟悉以dataObj为准，否则使用info中的数据
      (_, dataObj) => (this.info = { ...this.info, ...dataObj })
    );
  },

  beforeDestroy() {
    this.$bus.$off("users");
    // 取消消息的订阅
    pubsub.unsubscribe(this.pubId);
  }
};
</script>
```

测试结果：

展示“欢迎使用”：

![image-20221004110403347](../../../md-photo/image-20221004110403347.png)

查询过程中展示“加载中...”：

![image-20221004110428758](../../../md-photo/image-20221004110428758.png)

展示列表数据：

![image-20221004110511953](../../../md-photo/image-20221004110511953.png)



出错的时候，展示错误信息：

![image-20221004110814117](../../../md-photo/image-20221004110814117.png)



## Vue-Resource

Vue的插件。

安装vue-resource，用vue自己的封装发送Ajax请求。

```bash
$ npm i vue-resource
```

在main.js中引入插件：

```javascript
import vueResource from "vue-resource";

// 使用插件
Vue.use(vueResource);
```

用法跟axios一样：

```vue
<script>
import pubsub from "pubsub-js";
  methods: {
    searchUsers() {
      // 使用vue-resource的方式发送Ajax请求
      this.$http.get(`https://api.github.com/search/users?q=${this.keyWord}`).then(
        response => {
          ...
        },
        error => {
          ...
        }
      );
    }
  }
};
</script>
```



## Slot插槽

### 默认插槽

当一个组件中的一些结构相同，但是另一部分结构不一样时，可以使用**<font color='red'>slot标签占个位，具体的结构由组件使用者进行传入</font>**。

实例：

App.vue（<font color='red'>**负责给组件传递结构**</font>）：

```vue
<template>
  <div class="container">
    <!-- 传递一个图片 -->
    <Category title="美食">
      <img src="https://s3.ax1x.com/2021/01/16/srJlq0.jpg" />
    </Category>
    <!-- 传递列表数据 -->
    <Category :listData="foods" title="游戏">
      <ul>
        <li v-for="(item, index) in games" :key="index">{{item}}</li>
      </ul>
    </Category>
    <!-- 传递mp4数据 -->
    <Category title="电影">
      <!-- controls：控制可以播放 -->
      <video controls src="http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4" />
    </Category>
  </div>
</template>

<script>
export default {
  data() {
    return {
      foods: ["火锅", "烧烤", "小龙虾", "牛排"],
      games: ["红色警戒", "穿越火线", "劲舞团", "超级玛丽"],
      films: ["《教父》", "《拆弹专家》", "《你好，李焕英》"]
    };
  }
};
</script>
```

Category.vue（<font color='red'>**定义slot插槽并接收App.vue传递进来的结构**</font>）

```vue
<template>
  <div class="category">
    <h3>{{title}}分类</h3>
    <!-- 定义一个插槽（等着组件的使用者进行填充） -->
    <slot>当使用者没有传递结构时，展示这些文字</slot>
  </div>
</template>

<script>
export default {
  name: "Category",
  props: ["title"]
};
</script>
```

测试结果：

![image-20221004162734306](../../../md-photo/image-20221004162734306.png)



如果外部不传入结构：

![image-20221004162826017](../../../md-photo/image-20221004162826017.png)



### 具名插槽

为插槽指定一个具体的名字，这样在外部调用组件时可以将结构进行对号入座。

Category定义两个带名字的插槽：

```vue
<template>
  <div class="category">
    <h3>{{title}}分类</h3>
    <!-- 定义一个插槽（等着组件的使用者进行填充） -->
    <slot name="center">当中部没有传递结构时，展示这些文字</slot>
    <slot name="footer">当底部没有传递结构时，展示这些文字</slot>
  </div>
</template>
```

App传递结构的时候指定插槽名字（<font color='red'>在元素标签上添加slot="xxx插槽名即可"</font>）：

注意：<font color='red'>如果使用的时候有多个结构，且不想要在外面套一层div标签，可以使用template标签的方式，此时可以用**v-slot:xxx插槽名**的方式指定插槽</font>

```vue
<template>
  <div class="container">
    <!-- 传递一个图片 -->
    <Category title="美食">
      <img slot="center" src="https://s3.ax1x.com/2021/01/16/srJlq0.jpg" />
      <a slot="footer" href="http://www.baidu.com">更多美食</a>
    </Category>
    <!-- 传递列表数据 -->
    <Category :listData="foods" title="游戏">
      <ul slot="center">
        <li v-for="(item, index) in games" :key="index">{{item}}</li>
      </ul>

      <a slot="footer" href="http://www.baidu.com">单机游戏</a>
      <a slot="footer" href="http://www.baidu.com">网络游戏</a>
    </Category>
    <!-- 传递mp4数据 -->
    <Category title="电影">
      <!-- controls：控制可以播放 -->
      <video slot="center" controls src="http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4" />

      <template v-slot:footer>
        <div slot="footer">
          <a href="http://www.baidu.com">经典</a>
          <a href="http://www.baidu.com">热门</a>
          <a href="http://www.baidu.com">推荐</a>
        </div>
        <h4>欢迎来观看电影</h4>
      </template>
    </Category>
  </div>
</template>
```

测试结果：

![image-20221004165151282](../../../md-photo/image-20221004165151282.png)



### 作用域插槽

如果<font color='red'>数据在定义插槽的地方</font>，而在<font color='red'>插槽真实结构体的时候需要使用这些数据</font>，那么此时需要通过作用域来解决该问题。

Category.vue定义插槽：

注意：<font color='red'>**:games="games"的方式将数据传递给结构体，可以传递多个属性，结构体必须要使用template标签且使用scope属性来接收这些数据**</font>。

```vue
<template>
  <div class="category">
    <h3>{{title}}分类</h3>
    <!-- :games="games"将game数据传递给使用该插槽的结构 -->
    <!-- 使用该插槽的结构必须是template标签并且需要定义scope属性进行接收 -->
    <!-- scope接收到的是一个对象，意味着这里可以传递多个属性 -->
    <slot :games="games" msg="hello">当中部没有传递结构时，展示这些文字</slot>
  </div>
</template>
```

App.vue负责编写结构体并接收数据：

注意：

> 1. scope属性接收到的是一个对象,如:{ "games": [ "红色警戒", "穿越火线", "劲舞团", "超级玛丽" ], "msg": "hello" },<font color='red'>因此可以接收到多个属性的数据</font>.
> 2. 可以使用es6的解构赋值来直接获取games等属性,如:<font color='red'>scope="{games}"</font>.
> 3. 之前的版本使用的是scope的方式,后面的版本使用的是:<font color='red'>slot-scope="{games}"</font>的方式.

```vue
<template>
  <div class="container">
    <!-- 传递列表数据 -->
    <Category title="游戏">
      <template scope="data">
        <ul>
          <li v-for="(item, index) in data.games" :key="index">{{item}}</li>
        </ul>
        <!-- 获取提示信息 -->
        <h4>提示信息是：{{data.msg}}</h4>
      </template>
    </Category>

    <Category title="游戏">
      <!-- 使用es6的解构赋值的写法 -->
      <template scope="{games}">
        <ol>
          <li v-for="(item, index) in games" :key="index">{{item}}</li>
        </ol>
      </template>
    </Category>

    <Category title="游戏">
      <!-- 使用vue新的api的方式 -->
      <template slot-scope="{games}">
        <h4 v-for="(item, index) in games" :key="index">{{item}}</h4>
      </template>
    </Category>
  </div>
</template>
```

测试结果:

都成功的获取到了Category.vue中的games数据。

![image-20221004175126869](../../../md-photo/image-20221004175126869.png)

### 总结

1. 作用：让父组件可以向子组件指定位置插入html结构，也是一种组件间通信的方式，适用于<font color='red'>**父组件 ===> 子组件**</font> 。

2. 分类：默认插槽、具名插槽、作用域插槽

3. 使用方式：

   1. 默认插槽：

      ```vue
      父组件中：
              <Category>
                 <div>html结构1</div>
              </Category>
      子组件中：
              <template>
                  <div>
                     <!-- 定义插槽 -->
                     <slot>插槽默认内容...</slot>
                  </div>
              </template>
      ```

   2. 具名插槽：

      ```vue
      父组件中：
              <Category>
                  <template slot="center">
                    <div>html结构1</div>
                  </template>
      
                  <template v-slot:footer>
                     <div>html结构2</div>
                  </template>
              </Category>
      子组件中：
              <template>
                  <div>
                     <!-- 定义插槽 -->
                     <slot name="center">插槽默认内容...</slot>
                     <slot name="footer">插槽默认内容...</slot>
                  </div>
              </template>
      ```

   3. 作用域插槽：

      1. 理解：<font color='red'>数据在组件的自身，但根据数据生成的结构需要组件的使用者来决定</font>。（games数据在Category组件中，但使用数据所遍历出来的结构由App组件决定）

      2. 具体编码：

         ```vue
         父组件中：
         		<Category>
         			<template scope="scopeData">
         				<!-- 生成的是ul列表 -->
         				<ul>
         					<li v-for="g in scopeData.games" :key="g">{{g}}</li>
         				</ul>
         			</template>
         		</Category>
         
         		<Category>
         			<template slot-scope="scopeData">
         				<!-- 生成的是h4标题 -->
         				<h4 v-for="g in scopeData.games" :key="g">{{g}}</h4>
         			</template>
         		</Category>
         子组件中：
                 <template>
                     <div>
                         <slot :games="games"></slot>
                     </div>
                 </template>
         		
                 <script>
                     export default {
                         name:'Category',
                         props:['title'],
                         //数据在子组件自身
                         data() {
                             return {
                                 games:['红色警戒','穿越火线','劲舞团','超级玛丽']
                             }
                         },
                     }
                 </script>
         ```

    

## vuex

### 定义

专门在 Vue 中实现集中式状态（数据）管理的一个<font color='red'>Vue 插件</font>，对vue应用中多个组件的共享状态进行<font color='red'>集中式的管理（读/写）</font>，也是一种<font color='red'>组件间通信的方式</font>，且适用于任意组件间通信。



### 解决问题

当多个组件需要对同一个数据进行修改时，使用全局事件总线实现和vuex方式实现的区别：

![image-20221004212948626](../../../md-photo/image-20221004212948626.png)

![image-20221004213124792](../../../md-photo/image-20221004213124792.png)

### 使用场景

1. 多个组件依赖于同一状态
2. 来自不同组件的行为需要变更同一状态



### 工作原理

![image](../../../md-photo/vuex.png)

**Actions**：响应组件中用户的动作

**State**：保存具体的数据

**Mulations**：修改state中的数据



### 安装vuex

```bash
# 安装vue2指定版本的vuex
$ npm i vuex@3
```

1. 创建文件：`src/store/index.js`

   注意：<font color='red'>**需要在该文件中调用Vue.use(Vuex)，如果在main.js中调用的话，会导致Vue还没有应用Vuex插件，但是已经调用了new Vuex.Store({})的方法，导致报错**</font>。

   ```js
   // 引入Vue
   import Vue from 'vue';
   // 该文件用于创建vuex中的核心store
   // 引入Vuex
   import Vuex from "vuex";
   // 准备actions--用于响应组件中用户的动作
   const actions = {};
   // mulations--用于操作数据（state）
   const mulations = {};
   // 准备state--用于存储数据
   const state = {};
   
   // 由于脚手架会将所有的import先执行，所以需要在这个位置使用Vuex插件
   // 使用Vuex插件
   Vue.use(Vuex);
   // 创建并暴露store
   export default new Vuex.Store({
       // actions: actions的简写方式，引用的是上面的actions数据
       actions,
       mulations,
       state
   });
   ```

2. 在`main.js`中创建vm时传入`store`配置项

   ```js
   // 引入store
   // 引入的时候可以省略默认值index
   import store from "./store";
   
   const vm = new Vue({
       el: '#app',
       render: h => h(App),
       store,
   });
   
   console.log(vm);
   ```



### 基本使用

初始化数据、配置`actions`、配置`mutations`，操作文件`store.js`：

store中的actions：

<font color='red'>**actions里的函数可以获取到两个参数：context：store的上下文对象、value：组件传递过来的值**</font>。

<font color='red'>**actions中负责判断逻辑，不要直接跟state对话**</font>。

<font color='red'>**context中包含commit（提交动作）、dispatch（再次转发）、state（数据）**</font>

```javascript
// 准备actions--用于响应组件中用户的动作
const actions = {
    // 第一个参数是store的上下文对象，能够执行commit方法来调用mutations里面的函数
    // 第二个参数是传递过来的值
    // add(context, value) {
    //     context.commit("ADD", value);
    // },

    sub(context, value) {
        // 通过context转发给另一个action
        context.dispatch("beforeSub", value);
        context.commit("SUB", value);
    },

    // 内部转发过来的action
    beforeSub(_, value) {
        console.log("此次修改的值为：", value);
    },

    // Actions中判断为奇数再加
    addOdd(context, value) {
        // 在这里不要直接获取state中的数据，而是应该通过上下文来获取state中的数据
        if (context.state.sum % 2) {
            context.commit("ADD", value);
        }
    },

    // Actions中触发定时器后再加
    addWait(context, value) {
        setTimeout(() => {
            context.commit("ADD", value);
        }, 1000)
    }
};
```

store中的mutations：

<font color='red'>**mutations中负责对state中的数据进行操作，可以收到两个参数：state：存储的数据、value：组件传递过来的值**</font>。

<font color='red'>**mutations应该只负责对数据进行操作，没有业务逻辑**</font>。

```javascript
// mulations--用于操作数据（state）
const mutations = {
    // 第一个参数是state中存储的数据
    // 第二个参数是传递过来的值
    ADD(state, value) {
        state.sum += value;
    },

    SUB(state, value) {
        state.sum -= value;
    }
};
```

组件调用store实现对数据的操作：

<font color='red'>**组件中读取vuex中的数据：`$store.state.sum`**</font>。

<font color='red'>**组件中修改vuex中的数据：`$store.dispatch('action中的方法名',数据)` 或 `$store.commit('mutations中的方法名',数据)`**</font>。

> 备注：若没有网络请求或其他业务逻辑，组件中也可以越过actions，即不写`dispatch`，直接编写`commit`

```vue
<template>
  <div>
    <!-- 直接通过$store获取state中存储的数据 -->
    <h1>当前求和为：{{$store.state.sum}}</h1>
  </div>
</template>

<script>
export default {
  methods: {
    increment() {
      // 跟actions对话
      // this.$store.dispatch("add", this.number);
      // 直接跟mutations对话
      this.$store.commit("ADD", this.number);
    },
    decrement() {
      // 跟actions对话
      this.$store.dispatch("sub", this.number);
      // 直接跟mutations对话
      // this.$store.commit("SUB", this.number);
    },
    incrementOdd() {
      this.$store.dispatch("addOdd", this.number);
      if (this.$store.state.sum % 2) {
        this.increment();
      }
    },
    incrementWait() {
      this.$store.dispatch("addWait", this.number);
    }
  }
};
</script>
```

测试结果：

![image-20221005154732848](../../../md-photo/image-20221005154732848.png)

### getters配置项

概念：当state中的数据需要经过加工后再使用时，可以使用getters加工。

1. 在`store.js`中追加`getters`配置

   ```js
   const getters = {
   	bigSum(state){
   		return state.sum * 10
   	}
   }
   
   //创建并暴露store
   export default new Vuex.Store({
   	getters
   })
   ```

2. 组件中读取数据：`$store.getters.bigSum`



### 四个map方法的使用

#### mapState方法

用于帮助我们映射`state`中的数据为计算属性

```js
computed: {
    // 使用mapState将Vuex中的state属性解析为计算属性
    // 对象中的key为计算属性名，value为Vuex中的state属性名
    // ...为es6的解构赋值的写法，将mapState对象中的所有属性解析出来给computed对象使用
    ...mapState({ sum: "sum", school: "school", subject: "subject" }),
    // 可以简写为一个数组的的方式，但是此时的key === value
    ...mapState(["sum", "school", "subject"]),
},
```

#### mapGetters方法

用于帮助我们映射`getters`中的数据为计算属性

```js
computed: {
    //借助mapGetters生成计算属性：bigSum（对象写法）
    ...mapGetters({bigSum:'bigSum'}),

    //借助mapGetters生成计算属性：bigSum（数组写法）
    ...mapGetters(['bigSum'])
},
```

这样我们在组件访问Vuex的数据可以直接使用计算属性：

```html
<!-- 直接通过$store获取state中存储的数据 -->
<h1>当前求和为：{{sum}}</h1>
<!-- 使用vuex中的getters方法实现 -->
<h1>当前求和放大10倍为：{{bigSum}}</h1>
<h1>我在{{school}}学习{{subject}}</h1>
```

#### mapActions方法

用于帮助我们生成与`actions`对话的方法，即：包含`$store.dispatch(xxx)`的函数

```js
methods:{
    // 借助mapActions生成对应的方法，方法中会调用dispatch去联系actions
    // 对象写法
    ...mapActions({ addOdd: "addOdd", addWait: "addWait" }),

    // 数组写法
    ...mapActions(["addOdd", "addWait"])
}
```

#### mapMutations方法

用于帮助我们生成与`mutations`对话的方法，即：包含`$store.commit(xxx)`的函数

```js
methods:{
    // 借助mapMutations生成对应的方法，方法中会调用commit去联系mutations
    // 对象写法
    ...mapMutations({ ADD: "ADD", SUB: "SUB" }),
    // 数组写法
    ...mapMutations(["ADD", "SUB"]),
}
```

<font color='red'>**mapActions与mapMutations使用时，若需要传递参数需要：在模板中绑定事件时传递好参数，否则参数是事件对象**</font>。



### 模块化+命名空间

#### 目的

<font color='red'>**让代码更好维护，让多种数据分类更加明确**</font>。

#### 实例

1. 修改`store.js`

   ```javascript
   const countAbout = {
     namespaced:true,// 开启命名空间
     state:{...},
     mutations: { ... },
     actions: { ... },
     getters: { ... }
   }
   
   const personAbout = {
     namespaced:true,// 开启命名空间
     state:{ ... },
     mutations: { ... },
     actions: { ... }
   }
   
   export default new Vuex.Store({
     modules: {
       countAbout,
       personAbout
     }
   })
   ```

2. 开启命名空间后，组件中读取state数据（<font color='red'>**一定需要namespaced:true配置，如果没有的话，会报错**</font>）：

   ```js
   //方式一：自己直接读取
   this.$store.state.aboutPerson.personList
   //方式二：借助mapState读取：
   ...mapState("aboutCount", ["sum", "school", "subject"])
   ```

3. 开启命名空间后，组件中读取getters数据：

   <font color='red'>**this.$store.getters['personAbout/firstPersonName']：读取$store.getters中的'personAbout/firstPersonName'属性**</font>。

   ```js
   //方式一：自己直接读取
   this.$store.getters["aboutPerson/firstPersonName"]
   //方式二：借助mapGetters读取：
   ...mapGetters("aboutCount", ["bigSum"])
   ```

4. 开启命名空间后，组件中调用dispatch

   ```js
   //方式一：自己直接dispatch
   this.$store.dispatch("aboutPerson/addPersonWang", person);
   //方式二：借助mapActions：
   ...mapActions("aboutCount", ["addOdd", "addWait"])
   ```

5. 开启命名空间后，组件中调用commit

   ```js
   //方式一：自己直接commit
    this.$store.commit("aboutPerson/ADD_PERSON", person)
   //方式二：借助mapMutations：
   ...mapMutations("aboutCount", { ADD: "ADD", SUB: "SUB" })
   ```