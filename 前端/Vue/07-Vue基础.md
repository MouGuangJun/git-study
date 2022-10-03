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