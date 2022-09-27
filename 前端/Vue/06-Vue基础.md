# Vue基础

## TodoList自定义事件传输数据

涉及到给App传递数据，让App调用相应的函数处理的地方都可以修改为通过自定义事件，实现在父、子组件之间传递数据。

### MyHeader

为App中的MyHeader绑定自定义事件：

```vue
<template>
  <MyHeader @addTodo="addTodo" />
</template>

<script>
export default {
   methods: {
    // 添加一个todo
    addTodo(todoObj) {
      this.todos.unshift(todoObj);
    }
  }
};
</script>
```



MyHeader中触发事件：

```vue
<template>
  <input type="text" v-model="title" placeholder="请输入你的任务名称，按回车键确认" @keyup.enter="add" />
</template>

<script>
export default {
  methods: {
    add(event) {
      // 输入的是空，不进行新增的操作
      if (!this.title.trim()) return alert("输入不能为空！");
      const todoObj = { id: nanoid(), title: this.title, done: false };
      // 触发自定义事件，发送响应的数据
      this.$emit("addTodo", todoObj);
      // 新增完成后，清空输入的内容
      this.title = "";
    }
  }
};
</script>
```



执行结果：
![image-20220926222829902](../../../md-photo/image-20220926222829902.png)



### MyFooter

App修改部分：

```vue
<template>
  <!--将原来通过props方式传递v-bind修改为自定义事件的方式v-on-->
  <MyFooter :todos="todos" @checkAllTodo="checkAllTodo" @blur="clearAllTodo" />
</template>

<script>
export default {
  methods: {
    // 全选or取消全选
    checkAllTodo(done) {
      this.todos.forEach(todo => (todo.done = done));
    },

    // 清除所有已经完成的todo
    clearAllTodo() {
      this.todos = this.todos.filter(todo => !todo.done);
    }
  }
};
</script>
```



MyFooter中修改部分：

```vue
<template>
	<input type="checkbox" v-model="isAll" />
    <button class="btn btn-danger" @click="clearAllTodo">清除已完成任务</button>
</template>

<script>
export default {
  computed: {
    isAll: {
      set(value) {
        // 触发自定义事件
        this.$emit("checkAllTodo", value);
      },

      get() {
        return this.total !== 0 && this.total === this.doneTotal;
      }
    }
  },

  methods: {
    clearAllTodo() {
      // 触发自定义事件
      this.$emit("clearAllTodo");
    }
  },

  props: ["todos"]
};
</script>
```



测试结果：

![image-20220926224855043](../../../md-photo/image-20220926224855043.png)





## 全局事件总线

### 定义

实现任意组件之间的通信。

通过中间件X实现不同组件间的通信。**<font color='red'>在A组件上写一个回调函数，X绑定该回调函数的事件demo，B组件编写代码触发X上的事件demo</font>**。从而完成从B组件到A组件的数据传递，**其中X要对所有组件可见**。

![image-20220926230634007](../../../md-photo/image-20220926230634007.png)



如何保证X被所有组件可见？

使用Vue中的重要内置关系：**<font color='red'>VueComponent.prototype._proto__ === Vue.prototype</font>**。

在mian.js中的Vue的原型上添加一个属性：

```javascript
Vue.prototype.x = { a: 1, b: 2 };
```



此时在School组件中可以直接获取该属性：

```vue
<script>
export default {
  mounted() {
    // 这里是找自身的x没找到，然后找原型对象this.__proto__，然后找vm的原型对象
    console.log(this.x);
  }
};
</script>
```



测试结果：

![image-20220926233437302](../../../md-photo/image-20220926233437302.png)



### 实例

main.js中创建全局事件总线：<font color='red'>包含vc和vm的两种创建方式，**其中通过vm生命周期钩子创建的方式常用**</font>。

```javascript
// 使用组件的方式
// const VComponent = Vue.extend({});
// Vue.prototype.$bus = new VComponent();

new Vue({
    el: '#app',
    render: h => h(App),
    // 在vm实例对象创建完成后，在其身上绑定全局事件总线
    beforeCreate() {
        Vue.prototype.$bus = this;// 安装全局事件总线
    },
});
```



School中绑定和解绑事件：<font color='red'>此时vc组件销毁时需要解绑自定义事件，否则自定义事件永远无法解绑</font>。

```vue
<template>
    <h2>学生姓名：{{studentName}}</h2>
</template>

<script>
export default {
  data() {
    return {
      studentName: ""
    };
  },

  // 绑定全局的自定义事件
  mounted() {
    this.$bus.$on("sendStudentName", studentName => {
      console.log("成功收到Student传递过来的数据！", studentName);
      this.studentName = studentName;
    });
  },

  // 将当前School上的自定义事件解绑
  beforeDestroy() {
    this.$bus.$off("sendStudentName");
  }
};
</script>
```



Student组件去触发对应的自定义事件：

```vue
<script>
export default {
  methods: {
    // 触发自定义事件
    sendStudentName() {
      this.$bus.$emit("sendStudentName", this.name);
    }
  }
};
</script>
```



测试结果，**<font color='red'>可以看到触发这个事件的是Root（vm自身）</font>**：

![image-20220927224649358](../../../md-photo/image-20220927224649358.png)

### 总结

1. 一种组件间通信的方式，适用于<span style="color:red">任意组件间通信</span>。

2. 安装全局事件总线：

   ```js
   new Vue({
   	......
   	beforeCreate() {
   		Vue.prototype.$bus = this //安装全局事件总线，$bus就是当前应用的vm
   	},
       ......
   }) 
   ```

3. 使用事件总线：

   1. 接收数据：A组件想接收数据，则在A组件中给$bus绑定自定义事件，事件的<span style="color:red">回调留在A组件自身。</span>

      ```js
      methods(){
        demo(data){......}
      }
      ......
      mounted() {
        this.$bus.$on('xxxx',this.demo)
      }
      ```

   2. 提供数据：```this.$bus.$emit('xxxx',数据)```

4. 最好在beforeDestroy钩子中，用$off去解绑<span style="color:red">当前组件所用到的</span>事件。



## 消息的订阅与发布

