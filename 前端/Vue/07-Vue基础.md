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

    