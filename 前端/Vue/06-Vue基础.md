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

### 实例

安装pubsub.js进行消息的发布与订阅：

```bash
$ npm i pubsub-js@1.6.0
```



School组件中订阅消息，<font color='red'>**subscribe中的回调函数需要使用箭头函数，否则this是undefined，如果参数msgName不需要用到，可以用下划线_进行参数占位**</font>：

```vue
<template>
    <button @click="destory">销毁组件</button>
</template>

<script>
// 引入消息发布订阅js
import pubsub from "pubsub-js";

export default {
  methods: {
    // 销毁组件时，看消息是否还能订阅到
    destory() {
      this.$destroy();
    }
  },

  mounted() {
    // 订阅消息（如果用普通的函数的话，这里的this是undefined）
    // 第一个参数是订阅消息名称，第二个是数据
    this.pubId = pubsub.subscribe("sendStudentName", (msgName, data) => {
      console.log(this);// 普通函数 === undefined
      console.log("订阅到了学生的名字", data);
      this.studentName = data;
    });
  },

  // 组件销毁的时候取消消息的订阅
  beforeDestroy() {
    pubsub.unsubscribe(this.pubId);
  }
};
</script>
```



Student组件发送消息：

```vue
<script>
// 引入消息发布订阅js
import pubsub from "pubsub-js";
export default {
  methods: {
    sendStudentName() {
      // 发布消息
      pubsub.publish("sendStudentName", this.name);
    }
  }
};
</script>
```



测试结果：

![image-20220928214450209](../../../md-photo/image-20220928214450209.png)

如果使用箭头函数：

```javascript
this.pubId = pubsub.subscribe("sendStudentName", function(msgName, data) {
    console.log(this);
    console.log("订阅到了学生的名字", data);
    this.studentName = data;
});
```

![image-20220928214626329](../../../md-photo/image-20220928214626329.png)



如果vc组件销毁时没有取消消息订阅：

![image-20220928214736780](../../../md-photo/image-20220928214736780.png)

### 总结

1. 一种组件间通信的方式，适用于任意组件间通信。

2. 使用步骤：

   1. 安装pubsub：`npm i pubsub-js`

   2. 引入: `import pubsub from 'pubsub-js'`

   3. 接收数据：A组件想接收数据，则在A组件中订阅消息，订阅的回调留在A组件自身。

      ```js
      methods(){
        demo(data){......}
      }
      ......
      mounted() {
        this.pid = pubsub.subscribe('xxx',this.demo) //订阅消息
      }
      ```

   4. 提供数据：`pubsub.publish('xxx',数据)`

   5. 最好在beforeDestroy钩子中，用`PubSub.unsubscribe(pid)`去取消订阅。



## $nextTick

 默认情况下，vue会等回调函数完成之后才重新解析模板生成dom元素，使用nextTick api可以等<font color='red'>重新解析模板生成dom元素结束后才调用对应的函数</font>。

1. 语法：`this.$nextTick(回调函数)`
2. 作用：在下一次 DOM 更新结束后执行其指定的回调。
3. 什么时候用：当改变数据后，要基于更新后的新DOM进行某些操作时，要在nextTick所指定的回调函数中执行。

```vue
<template>
	<input ref="inputTitle"/>
	<button @click="handleEdit(todo)">编辑</button>
</template>

<script>
export default {
    // 点击编辑按钮的时候，前面的文字修改为输入框
    handleEdit(todo) {
      // 如果todo上没有isEdit属性，才追加属性
      // 通过hasOwnProperty("xxx");api判断vm身上是否存在对应的属性
      if (todo.hasOwnProperty("isEdit")) {
        todo.isEdit = true;
      } else {
        console.log("todo身上没有isEdit属性！");
        this.$set(todo, "isEdit", true);
      }

      // 默认情况下，vue会等回调函数完成之后才重新解析模板生成dom元素
      // 使用nextTick api可以等重新解析模板生成dom元素结束后才调用对应的函数
      this.$nextTick(function() {
        this.$refs.inputTitle.focus();
      });
    }
  }
};
</script>
```

在vue重新解析模板生成dom元素结束后才触发获得焦点事件，否则就是在vue未重新解析模板的时候就触发了获取焦点事件。重新解析后就无法再获得焦点了，达不到想要的效果。



测试结果：

![image-20220929215414620](../../../md-photo/image-20220929215414620.png)



如果不使用$nextTick api：

![image-20220929215516322](../../../md-photo/image-20220929215516322.png)



## Vue动画

### 动画效果

使用transition标签实现动画的效果：

通过绑定v-show事件，<font color='red'>当isShow为true时执行v-enter-active动画效果，为false时执行v-leave-active动画效果</font>。

```vue
<template>
  <div>
    <button @click="isShow = !isShow">显示/隐藏</button>
    <!-- 开启动画过渡效果 -->
    <transition>
      <h2 v-show="isShow">您好啊</h2>
    </transition>
  </div>
</template>

<script>
export default {
  name: "Animation",
  data() {
    return {
      isShow: true
    };
  }
};
</script>
```



编写css效果，<font color='blue'>其中h1中的配置是对css动画的学习</font>：

```vue
<style scoped>
h2 {
  background-color: orange;
}

/* 出现的动画 */
.am-enter-active {
  animation-name: start;
  animation-duration: 1s;
  animation-timing-function: linear;
}

/* 消失的动画 */
.am-leave-active {
  animation-name: start;
  animation-duration: 1s;
  animation-direction: reverse;
  animation-timing-function: linear;
}

/* 仅仅作为案例使用 */
h1 {
  /* 选择关键帧 */
  animation-name: start;
  /* 关键帧持续时间 */
  animation-duration: 2s;
  /* 关键帧执行次数 */
  animation-iteration-count: 2;
  /* 关键帧[from -> to] [to -> from] 的切换 */
  animation-direction: alternate;
  /* 动画保持结束时的样子 */
  animation-fill-mode: forwards;
  /* 运动方式：匀速运动/贝塞尔曲线等 */
  animation-timing-function: linear;
}

/* 定义关键帧 */
@keyframes start {
  /* 关键帧开始的效果 */
  from {
    /* 用于X轴的偏移量 */
    transform: translateX(-100%);
  }

  /* 关键帧结束的效果 */
  to {
    transform: translateX(0);
  }
}
</style>
```



注意事项：

1. 当transition标签中定义name时，应该使用[name]-enter-active、[name]-leave-active的方式来定义入场、出场动画。

```vue
<template>
	<transition name="ts">
    	<h2 v-show="isShow">您好啊</h2>
    </transition>
</template>

<style scoped>
/* 出现的动画 */
.ts-enter-active {
  animation-name: start;
  animation-duration: 1s;
}

/* 消失的动画 */
.ts-leave-active {
  animation-name: start;
  animation-duration: 1s;
  animation-direction: reverse;
}
</style>
```



2. 如果想要动画一上来就执行，使用apper属性，<font color='red'>其中appear === :appear="true"</font>。

```vue
<template>
	<transition name="ts" appear>
    	<h2 v-show="isShow">您好啊</h2>
    </transition>
</template>
```



### 过渡效果

使用xxx-enter（动画进入的起点）、xxx-enter-to（动画进入的终点）、xxx-leave（动画离开的起点）、xxx-leave-to（动画离开的终点）来实现动画效果。

编写模板、js：

```vue
<template>
  <!-- 动画效果 -->
  <div>
    <transition name="ts" appear>
      <h2 v-show="isShow">您好啊</h2>
    </transition>
  </div>
</template>

<script>
export default {
  name: "TransitionT",
  props: ["isShow", "update"]
};
</script>
```



编写css样式，<font color='red'>**可以将相同的过渡样式放到ts-enter/leave-active进行统一的管理**</font>：

```vue
<style scoped>
h2 {
  background-color: orange;
}

/* 将统一的过渡样式放到一起 */
.ts-enter-active,
.ts-leave-active {
  transition-duration: 1s;
  transition-timing-function: linear;
}

/* 进入的起点、离开的终点 */
.ts-enter,
.ts-leave-to {
  transform: translateX(-100%);
}

/* 进入的终点、离开的起点 */
.ts-enter-to,
.ts-leave {
  transform: translateX(0);
}
</style>
```



最终结果（*动画效果*和*过渡效果*都能达到一样的效果）：

![image-20221002175237367](../../../md-photo/image-20221002175237367.png)



### 多个元素过渡

使用<font color='red'>transition-group</font>标签实现为多个标签使用相同的动画效果，<font color='red'>其中每一个标签都要绑定key值</font>，如下案例所示：

**动画效果与上一个案例一致**

```vue
<template>
  <!-- 多个元素过渡效果 -->
  <div>
    <transition-group name="tsg" appear>
      <h2 v-show="!isShow" key="key1">您好啊</h2>
      <h2 v-show="isShow" key="key2">您也很好</h2>
    </transition-group>
  </div>
</template>
```

测试结果：
![image-20221002220830191](../../../md-photo/image-20221002220830191.png)



### 集成第三方动画

animate.css，参见./../第三方css样式效果.md

安装animate.css：

```bash
$ npm install animate.css --save
```

在需要用到样式的js中引入对应的css：

```vue
<script>
import "animate.css";
export default {
  name: "AnimateT"
};
</script>
```



在需要动画效果的<font color='red'>transition标签</font>上绑定name：animate\__animated animate__bounce，已经想要使用的动画样式：
![image-20221002222952067](../../../md-photo/image-20221002222952067.png)



引入对应的样式，分别绑定vue中的**<font color='red'>enter-active-class（进入动画样式）、 leave-active-class（离开动画样式）</font>**：

```vue
<template>
  <!-- 引入第三方样式 -->
  <div>
    <transition
      appear
      name="animate__animated animate__bounce"
      enter-active-class="animate__zoomInUp"
      leave-active-class="animate__zoomOutRight"
    >
      <h2 v-show="isShow">您好啊（第三方样式）</h2>
    </transition>
  </div>
</template>
```



测试结果：

![image-20221002223425061](../../../md-photo/image-20221002223425061.png)



### TodoList动画改造

#### Item修改

注意：此时的Item<font color='red'>li标签外层由transition标签包裹，而非div标签（否则删除的时候没有动画效果）</font>，<font color='red'>使用leave、leave-to过渡效果时，必须用todo-leave-active指定持续时间才有效。</font>。

```vue
<template>
  <transition name="todo" appear>
    ...
  </transition>
</template>

<style scoped>
/* 添加动画效果 */
/* .todo-enter-active {
  animation-name: start;
  animation-duration: 1s;
}

.todo-leave-active {
  animation-name: start;
  animation-duration: 1s;
  animation-direction: reverse;
}

@keyframes start {
  from {
    transform: translateX(100%);
  }

  to {
    transform: translateX(0);
  }
} */

/* 设置动画的持续时长 */
.todo-enter-active,
.todo-leave-active {
  transition-duration: 1s;
}

.todo-enter,
.todo-leave-to {
  transform: translateX(100%);
}

.todo-enter-to,
.todo-leave {
  transform: translateX(0);
}
</style>
```



测试结果：
![image-20221002230843982](../../../md-photo/image-20221002230843982.png)    



#### List修改



### 总结

1. 作用：在插入、更新或移除 DOM元素时，在合适的时候给元素添加样式类名。

2. 图示：

   ![image](../../../md-photo/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAamF5TG9n,size_20,color_FFFFFF,t_70,g_se,x_16.png)

3. 写法：

   1. 准备好样式：

      - 元素进入的样式：
        1. v-enter：进入的起点
        2. v-enter-active：进入过程中
        3. v-enter-to：进入的终点
      - 元素离开的样式：
        1. v-leave：离开的起点
        2. v-leave-active：离开过程中
        3. v-leave-to：离开的终点

   2. 使用`<transition>`包裹要过度的元素，并配置name属性：

      ```vue
      <transition name="hello">
      	<h1 v-show="isShow">你好啊！</h1>
      </transition>
      ```

   3. 备注：若有多个元素需要过度，则需要使用：`<transition-group>`，且每个元素都要指定`key`值。
