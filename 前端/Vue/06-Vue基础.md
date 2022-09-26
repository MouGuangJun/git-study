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