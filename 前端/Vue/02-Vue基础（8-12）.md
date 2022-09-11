# Vue基础

## 姓名案例（引出计算属性）

![image-20220830202204130](../../../md-photo/image-20220830202204130.png)

### 数据绑定实现

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        姓：<input type="text" v-model="firstName" />
        <br />
        名：<input type="text" v-model="lastName" />
        <br />
        <!-- slice：截取字符串，这里是截取三位字符串 -->
        全名：<span>{{firstName.slice(0, 3)}} - {{lastName}}</span>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                firstName: '张',
                lastName: '三'
            }
        });
    </script>
</body>
```



### methods实现

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        姓：<input type="text" v-model="firstName" />
        <br />
        名：<input type="text" v-model="lastName" />
        <br />

        <!-- 这里使用fullName是插入函数，而fullName()才是获取返回值 -->
        全名：<span>{{fullName()}}</span>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                firstName: '张',
                lastName: '三'
            },

            methods: {
                fullName() {
                    // 此处的this对象为vue本身
                    return this.firstName + ' - ' + this.lastName;
                }
            }
        });
    </script>
</body>
```



### 计算属性实现

#### 定义

- 定义：要用的属性不存在，要通过**<font color='red'>已有属性（不能是常量等数据）</font>**计算得来。
- 原理：底层借助了Object.defineproperty方法提供的getter和setter。
- get函数什么时候执行?
  - 初次读取时会执行一次。
  - 当依赖的数据发生改变时会被再次调用。
- 优势：与methods实现相比，内部有缓存机制（复用），效率更高，调试方便。
- 备注：
  - 计算属性最终会出现在vm上，直接读取使用即可。
  - 如果计算属性要被修改，那必须写set函数去响应修改，且set中要引起计算时依赖的数据发生改变。

#### get方法

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        姓：<input type="text" v-model="firstName" />
        <br />
        名：<input type="text" v-model="lastName" />
        <br />

        全名：<span>{{fullName}}</span>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                firstName: '张',
                lastName: '三'
            },

            computed: {
                fullName: {
                    // get有什么作用? 当有人读取fullName时，get就会被调用，且返回值就作为fullName的值
                    // get什么时候调用? 1.初次读取fullName时。2.所依赖的数据发生变化时。

                    get() {
                        // 此处的this指的是vue对象
                        return this.firstName + ' - ' + this.lastName;
                    }
                }
            }
        });
    </script>
</body>
```



得到结果：

![image-20220830204424241](../../../md-photo/image-20220830204424241.png)

![image-20220830204508622](../../../md-photo/image-20220830204508622.png)



#### set方法

上面使用get方法获取对应的返回值，计算属性同样提供了set方法：

```javascript
fullName: {
    // get有什么作用? 当有人读取fullName时，get就会被调用，且返回值就作为fullName的值
    // get什么时候调用? 1.初次读取fullName时。2.所依赖的数据发生变化时。

    get() {
        // 此处的this指的是vue对象
        return this.firstName + ' - ' + this.lastName;
    },

    // 当模板中修改了fullName的值时，调用该函数
    set(value) {
        const arr = value.split('-');
        this.firstName = arr[0];
        this.lastName = arr[1];
    }
}
```



此时的执行过程，**vm.fullName='李-四'**，调用set方法，将firstName改为'李'，把lastName改为'四'，然后页面再次读取的时候，发现重新拼接出来的结果也是'李-四'：

![image-20220901224528141](../../../md-photo/image-20220901224528141.png)

#### 简写方式

**<font color='red'>没有setter的时候才能使用简写</font>**

```javascript
computed: {
    // 简写方式
    fullName() {
        return this.firstName + ' - ' + this.lastName;
    }
}
```

结果：

![image-20220901225835753](../../../md-photo/image-20220901225835753.png)





## 天气案例（引出监视属性）

### 原来的方法实现

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <h1>今天天气很{{info}}</h1>
        <br />
        <button @click='changeWeather'>切换天气</button>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                isHot: true
            },

            methods: {
                changeWeather() {
                    this.isHot = !this.isHot;
                }
            },

            computed: {
                info() {
                    return this.isHot ? '炎热' : '凉爽';
                }
            }
        });
    </script>
</body>
```



也可以在click事件中写简单的代码块语句（**<font color='red'>不推荐使用</font>**）：

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <h1>今天天气很{{info}}</h1>
        <br />
        <!-- 事件里面可以写简单的操作语句，多个语句之间通过;分号隔开 -->
        <button @click='isHot = !isHot'>切换天气</button>
    </div>

    <script type="text/javascript">
        const vm = new Vue({
            el: '#root',
            data: {
                isHot: true
            },
            computed: {
                info() {
                    return this.isHot ? '炎热' : '凉爽';
                }
            }
        });
    </script>
</body>
```



实现结果：

![image-20220902223826274](../../../md-photo/image-20220902223826274.png)



![image-20220902223846754](../../../md-photo/image-20220902223846754.png)



出现的问题：

![image-20220902224018289](../../../md-photo/image-20220902224018289.png)

![image-20220902224104060](../../../md-photo/image-20220902224104060.png)





### 监视属性方法实现

#### 定义

监视属性watch

- 当被监视的属性变化时，回调函数自动调用，进行相关操作
- 监视的属性必须存在，才能进行监视！！
- 监视的两种写法:
  - new Vue时传入watch配置
  - 通过vm.$watch监视



#### 示例

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <h1>今天天气很{{info}}</h1>
        <br />
        <button @click='changeWeather'>切换天气</button>
    </div>

    <script type="text/javascript">
        const vm = new Vue({
            el: '#root',
            data: {
                isHot: true
            },

            methods: {
                changeWeather() {
                    this.isHot = !this.isHot;
                }
            },

            computed: {
                info() {
                    return this.isHot ? '炎热' : '凉爽';
                }
            },

            // 直接在vm内部使用监视属性
            // watch: {
            //     // handler什么时候调用？当isHot发生改变时
            //     isHot: {
            //         immediate: true, // 初始化的时候让handler调用一下
            //         // 第一个参数为修改后的值，第二个参数为修改前的值
            //         handler(newValue, oldValue) {
            //             console.log(newValue, oldValue);
            //         }
            //     }
            // }
        });

        // 通过vm的方式添加监视属性
        // 在vm对象内部的时候直接用isHot一种简写方式，这里写单引号的话会把isHot当成一个变量
        vm.$watch('isHot', {
            immediate: true,
            handler(newValue, oldValue) {
                console.log(newValue, oldValue);
            }
        });
    </script>
</body>
```



#### 结果

![image-20220902230647795](../../../md-photo/image-20220902230647795.png)



### 深度监视

#### 定义

深度监视：

- Vue中的watch默认不监测对象内部值的改变(一层)。
- 配置deep:true可以监测对象内部值改变（多层）。

备注:

- Vue自身可以监测对象内部值的改变，但Vue提供的watch默认不可以！
- 使用watch时根据数据的具体结构，决定是否采用深度监视。



#### 案例

注意：<font color='red'>只监视对象中的某一个属性</font>：

```javascript
data: {
    numbers: {
        a: 1,
        b: 1
    }
},

watch: {
	// 监视多级结构中某个属性的变化
	'numbers.a': {
		handler(newValue, oldValue) {
			console.log(newValue, oldValue);
		}
	}
}     
```



```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <h1>a的值为：{{numbers.a}}</h1>
        <button @click='numbers.a++;'>点我让a加1</button>

        <br>
        <h1>b的值为：{{numbers.b}}</h1>
        <button @click='numbers.b--;'>点我让b减1</button>
    </div>

    <script type="text/javascript">
        const vm = new Vue({
            el: '#root',
            data: {
                numbers: {
                    a: 1,
                    b: 1
                }
            },

            watch: {
                // 监视多级结构中所有属性的变化
                numbers: {
                    deep: true,// 为true时，对象内的一个属性发生变化，会让vm监视到这个对象发生了变化
                    handler(newValue, oldValue) {
                        console.log(newValue, oldValue);
                    }
                }
            }
        });
    </script>
</body>
```



#### 测试结果

当deep为false时：

![image-20220903163611264](../../../md-photo/image-20220903163611264.png)

当deep为true时：

![image-20220903163704621](../../../md-photo/image-20220903163704621.png)

### 监视属性简写

当配置项中只有handler的时候才能使用简写的方式（<font color='red'>不能添加immediate、deep等配置项</font>）。

vm对象内部的简写方式：

```javascript
watch: {
    // handler什么时候调用？当isHot发生改变时
    isHot(newValue, oldValue) {
        console.log(newValue, oldValue);
    }
}
```



对vm进行修改的简写方式：

```javascript
// 对vm添加监视属性的简写
vm.$watch('isHot', function(newValue, oldValue){
    console.log(newValue, oldValue);
});
```



## 计算属性和监视属性的区别

computed和watch之间的区别:

- computed能完成的功能，watch都可以完成。
- watch能完成的功能，computed不一定能完成，例如: watch可以进行异步操作。

两个重要的小原则:

- 所被Vue管理的函数，最好写成普通函数，这样this的指向才是vm或组件实例对象
- 所有不被Vue所管理的函数（定时器的回调函数、ajax的回调函数等），最好写成箭头函数，这样this的指向才是vm或组件实例对象。



```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        姓：<input type="text" v-model="firstName" />
        <br />
        名：<input type="text" v-model="lastName" />
        <br />

        全名（计算属性）：<span>{{cFullName}}</span>
        <br />
        全名（监视属性）：<span>{{fullName}}</span>
    </div>

    <script type="text/javascript">
        const vm = new Vue({
            el: '#root',
            data: {
                firstName: '张',
                lastName: '三',
                fullName: '张-三'
            },

            computed: {
                cFullName() {
                    setTimeout(() => {
                        // 此时的return语句为Window中的返回值，所以vm中的计算属性无结果。
                        return this.firstName + '-' + this.lastName;
                    }, 1000);
                    // 这里缺少了vm计算属性需要的返回值
                    // return '请给我一个返回值！';
                }
            },

            watch: {
                // 新的姓 + 原来的名字
                firstName(newValue) {
                    // 这里用箭头函数，this往外层找，找到vm。如果不用箭头函数，那么this指向的是Window。
                    setTimeout(() => {
                        this.fullName = newValue + '-' + this.lastName;
                    }, 1000);
                },

                // 新的名字 + 原来的姓
                lastName(newValue) {
                    this.fullName = this.firstName + '-' + newValue;
                }
            },
        });
    </script>
</body>
```



运行结果：

![image-20220903174123726](../../../md-photo/image-20220903174123726.png)



## 绑定样式

### class样式

写法：class="xxx" xxx可以是字符串、对象、数组。

- 字符串写法适用于:类名不确定，要动态获取。
- 对象写法适用于:要绑定多个样式，个数不确定，名字也不确定。
-   数组写法遇用于:要绑定多个样式，个数确定，名字也确定，但不确定用不用

准备好所有的style：

```css
<style>
.basic {
    width: 200px;
    height: 100px;
    /* 边框长度默认 3px，边框颜色为黑色 */
    border-style: solid;
}

.happy {
    border-color: red;
    /* 第一个为倾斜角度（当然也可以to right[从左到右的方式]），后面过度的颜色 */
    background: linear-gradient(135deg, #DC143C, #FFC0CB, #FFB6C1);
}

.sad {
    border-color: green;
    background-color: #696969
}

.normal {
    background-color: turquoise
}

.style1 {
    background-color: greenyellow;
}

.style2 {
    font-size: 18px;
    /* 文字加粗 */
    font-weight: bold;
    /* 文字阴影，阴影X轴、Y轴、模糊半径、颜色 */
    text-shadow: 2px 3px 3px pink;
}

.style3 {
    /* 设置边框为圆角 */
    border-radius: 10%;
}
</style>
```



#### 字符串写法

适用于：样式的类名不确定，需要动态指定

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <!-- 需求：在原来的样式上，随机添加新样式 -->
        <!-- 绑定class样式--字符串写法，适用于：样式的类名不确定，需要动态指定 -->
        <!-- 使用:class，vm会将mood的值拼接在原来的class的后面，如basic normal -->
        <div id='demo' class='basic' :class='mood' @click='changeMood'>{{name}}</div>
        <br />
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                name: '尚硅谷',
                mood: 'normal'
            },

            methods: {
                changeMood() {
                    // 传统的方式不建议使用
                    // document.getElementById('demo').className = 'basic happy';
                    // this.mood = 'happy';

                    // 随机切换心情
                    const arr = ['happy', 'sad', 'normal'];
                    // 生成0 - 2的随机数
                    // Math.random()方法生成[0, 1)之间的随机数
                    const index = Math.floor(Math.random() * 3);
                    this.mood = arr[index];
                }
            }
        });
    </script>
</body>
```



结果：点击的时候动态切换样式

![image-20220903220932254](../../../md-photo/image-20220903220932254.png)

![image-20220903220940734](../../../md-photo/image-20220903220940734.png)



#### 数组写法

适用于：要绑定的样式的个数不确定、名字也不确定

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <!-- 需求：同时拥有多个样式 -->
        <!-- 绑定class样式--数组写法，适用于：要绑定的样式的个数不确定、名字也不确定 -->
        <!-- 可以直接写 ['style1', 'style2', 'style3'] -->
        <!-- 如果写成 [a, b, c]，那么会去vm身上找a、b、c这三个属性 -->
        <div class='basic' :class='classArr' @click='changeMood'>{{name}}</div>
        <br />
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                name: '尚硅谷',
                classArr: ['style1', 'style2', 'style3']
            }
        });
    </script>
</body>
```



结果：同时拥有三个样式

![image-20220903221223633](../../../md-photo/image-20220903221223633.png)



#### 对象写法

适用于：要绑定的样式的个数确定、名字也确定，但是动态决定用不用

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <!-- 需求：用哪些样式是固定的，但是不确定实际使用时候的个数 -->
        <!-- 绑定class样式--对象写法，适用于：要绑定的样式的个数确定、名字也确定，但是动态决定用不用 -->
        <!-- 也可以直接把对象绑定在class里面（不推荐使用） :class='{style1: false, style2: false}'-->
        <div class='basic' :class='classObj'>{{name}}</div>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                name: '尚硅谷',
                classObj: {
                    style1: false,
                    style2: false,
                }
            },
        });
    </script>
</body>
```



结果：可以动态决定是否使用样式

![image-20220903221524143](../../../md-photo/image-20220903221524143.png)



### 直接绑定style样式写法（了解）

**<font color='red'>fontSize、backgroundColor是样式对象，对应css中的font-size和background-color，不能随便乱写</font>**

- :style="{fontSize: xxx}"，其中xxx是动态值。
- :style="[a,b]"，其中a、b是样式对象。

#### 对象写法

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <!-- 直接用style的方式设置样式 -->
        <!-- 可以像js写法一样直接绑定一个对象：const x = { fontSize: fSize + 'px', backgroundColor: 'red' }，代码块后面不能加分号 -->
        <!-- <div class='basic' :style='{ fontSize: fSize + "px", backgroundColor: "red" }'>{{name}}</div> -->

        <!-- 对象写法，直接绑定vm中的数据对象 -->
        <div class='basic' :style='styleObj'>{{name}}</div>
        <br>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                name: '尚硅谷',
                fSize: 40,
                styleObj: {
                    fontSize: '40px',
                    backgroundColor: 'red'
                }
            },
        });
    </script>
</body>
```



#### 数组写法

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <!-- 数组写法，通过数组的方式绑定多个对象 -->
        <!-- 或者直接写为：:style='[styleObj1, styleObj2]' -->
        <div class='basic' :style='styleArr'>{{name}}</div>

    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                name: '尚硅谷',
                styleArr: [{
                    fontSize: '40px',
                    color: 'blue'
                },
                {
                    backgroundColor: 'gray'
                }]
            }
        });
    </script>
</body>
```



结果：成功应用了对应的样式

![image-20220903223700515](../../../md-photo/image-20220903223700515.png)



## 条件渲染

### v-if

- 写法：

  - v-if="表达式"

  - v-else-if="表达式"
  - v-else="表达式"

- 适用于：切换频率较低的场景。

- 特点：不展示的DOM元素直接被移除。
- 注意：v-if可以和v-else-if、v-else一起使用，但要求结构不能被"打断"。



### v-show

- 写法：v-show="表达式"
- 适用于：切换频率较高的场景。
- 特点：不展示的DOM元素未被移除，仅仅是使用样式隐藏掉



### 代码案例

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <!-- 使用v-show做条件渲染 -->
        <!-- v-show底层调整display:none属性 -->
        <!-- <h1 v-show='false'>欢迎来到{{name}}</h1> -->
        <!-- <h1 v-show='1 === 1'>欢迎来到{{name}}</h1> -->
        <!-- 也可以绑定vm中的属性 -->
        <!-- <h1 v-show='isShow'>欢迎来到{{name}}</h1> -->

        <!-- 使用v-if做条件渲染 -->
        <!-- 如果v-if条件不满足，直接不渲染这个标签 -->
        <!-- <h1 v-if='false'>欢迎来到{{name}}</h1> -->
        <!-- <h1 v-if='1 === 1'>欢迎来到{{name}}</h1> -->

        <h1>当前n的值为：{{n}}</h1>
        <button @click='n++'>点我n+1</button>

        <!-- 显示/隐藏频率高的时候使用v-show、频率低的时候可以选择使用v-if -->
        <div v-show='n === 1'>Angular</div>
        <div v-show='n === 2'>React</div>
        <div v-show='n === 3'>Vue</div>

        <!-- if... else if... else...的结构 -->
        <div v-if='n === 1'>Angular</div>
        <!-- 使用该数据结构的时候必须连续，不能被打断 -->
        <!-- <div>Break</div> -->
        <div v-else-if='n === 2'>React</div>
        <div v-else>Vue</div>

        <!-- 同一个判断条件影响多个标签 -->
        <!-- <div v-if='n === 1'>
            <h1>你好</h1>
            <h1>尚硅谷</h1>
            <h1>北京</h1>
        </div> -->

        <!-- 使用template的时候不会改变原来的结构，外层无需套多余的div标签 -->
        <!-- template只能配合v-if使用，不能配合v-show使用 -->
        <template v-if='n === 1'>
            <h1>你好</h1>
            <h1>尚硅谷</h1>
            <h1>北京</h1>
        </template>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                name: '尚硅谷',
                isShow: false,
                n: 0
            }
        });
    </script>
</body>
```



页面展示结果：

![image-20220904095910696](../../../md-photo/image-20220904095910696.png)



### 备注

使用v-if的时，元素可能无法获取到（<font color='red'>页面不会渲染对应的标签</font>），而使用v-show一定可以获取到。



## 列表渲染

### v-for指令

- 用于展示列表数据
- 语法：v-for="(item,index) in xxx" :key="yyy"
- 可遍历：数组、对象、字符串（用的很少）、指定次数（用的很少)



#### 代码案例

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <!-- 遍历数组 -->
        <h1>人员列表</h1>
        <ul>
            <!-- 遍历数据，必须绑定key值（作为唯一主键） -->
            <!-- 使用index索引也可以当作唯一主键 -->
            <!-- <li v-for='(person, index) in persons' :key='index'> -->
            <!-- v-for可以接收到两个参数，第一个为数组中的单个元素，第二个为元素的索引值 -->
            <!-- 可以用of代替in关键字 -->
            <li v-for='(person, index) in persons' :key='person.id'>
                {{index}}.{{person.name}}-{{person.age}}
            </li>
        </ul>

        <!-- 遍历对象 -->
        <ul>
            <!-- 第一个参数为对象属性值，第二个参数为对象属性名称 -->
            <li v-for='(value, key) in car' :key='key'>
                {{key}} --> {{value}}
            </li>
        </ul>

        <!-- 遍历字符串 -->
        <ul>
            <!-- 第一个参数为字符串，第二个为索引值 -->
            <li v-for='(char, index) in str' :key='index'>
                {{index}} --> {{char}}
            </li>
        </ul>

        <!-- 遍历指定次数 -->
        <ul>
            <!-- 第一个参数为字符串，第二个为索引值 -->
            <li v-for='(number, index) in 5' :key='index'>
                {{index}}.{{number}}
            </li>
        </ul>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                persons: [
                    {
                        id: '001',
                        name: '张三',
                        age: 18
                    },
                    {
                        id: '002',
                        name: '李四',
                        age: 19
                    },
                    {
                        id: '003',
                        name: '王五',
                        age: 20
                    }
                ],

                car: {
                    name: '奥迪A8',
                    price: '70万',
                    color: '黑色'
                },

                str: 'hello'
            }
        });
    </script>
</body>
```



#### 页面结果

![image-20220904102323622](../../../md-photo/image-20220904102323622.png)



### v-for中的key

#### 出现问题

使用index作为key值时：

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <h1>人员列表</h1>
        <button @click.once='add'>新增人员</button>
        <ul>
            <!-- 会导致vm从新渲染所有数据，而且input框中输入的内容会错位 -->
            <li v-for='(person, index) in persons' :key='index'>
                {{index}}.{{person.name}}-{{person.age}}
                <input type="text">
            </li>
        </ul>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                persons: [
                    {
                        id: '001',
                        name: '张三',
                        age: 18
                    },
                    {
                        id: '002',
                        name: '李四',
                        age: 19
                    },
                    {
                        id: '003',
                        name: '王五',
                        age: 20
                    }
                ]
            },

            methods: {
                add() {
                    const p = {
                        id: '004',
                        name: '老刘',
                        age: 30
                    };

                    // unshift：左侧添加一个元素，push：右侧添加一个元素
                    this.persons.unshift(p);
                }
            },
        });
    </script>
</body>
```

![image-20220904104947859](../../../md-photo/image-20220904104947859.png)



出现该情况的原因：

**<font  color='red'>其中，虚拟DOM对比算法不仅会对比标签内的文字，还会对比标签自身（如果标签一样也会被复用）</font>**

![image-20220904105839872](../../../md-photo/image-20220904105839872.png)



使用person对象中的唯一id作为key值时：

```javascript
<!-- 仅仅渲染新增的数据，input框中输入的内容不会错位 -->
<li v-for='(person, index) in persons' :key='person.id'>
	{{index}}.{{person.name}}-{{person.age}}
	<input type="text">
</li>
```

![image-20220904105332243](../../../md-photo/image-20220904105332243.png)



没有发生错位的原因：

![image-20220904105923722](../../../md-photo/image-20220904105923722.png)



#### 总结问题

面试题:react、 vue中的key有什么作用?(key的内部原理)

- 虚拟DOM中key的作用:
  -  key是虚拟DOM对象的标识，当数据发生变化时，Vue会根据【新数据】生成【新的虚拟DOM】， 随后Vue进行【新虚拟DOM】与【旧虚拟DOM】的差异比较，比较规则如下:
- 对比规则:
  - 旧虚拟DOM中找到了与新虚拟DOM相同的key:
    - 若虚拟DOM中内容没变，直接使用之前的真实DOM！
    - 若虚拟DOM中内容变了，则生成新的真实DOM，随后替换掉页面中之前的真实DOM。
  - 旧虚拟DOM中未找到与新虚拟DOM相同的key
    - 创建新的真实DOM，随后渲染到到页面。
- 用index作为key可能会引发的问题:
  - 若对数据进行:逆序添加、逆序删除等破坏顺序操作:
    - 会产生没有必要的真实DOM更新==>界面效果没问题，但效率低。
  - 如果结构中还包含输入类的DOM:
    - 会产生错误DOM更新==>界面有问题。
- 开发中如何选择key? 
  - 最好使用每条数据的唯一标识作为key，比如id、手机号、身份证号、学号等唯一值。
  - 如果不存在对数据的逆序添加、逆序删除等破坏顺序操作，仅用于渲染列表用于展示，使用index作为key是没有问题的。



### 列表过滤

#### 监视属性实现

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <h1>人员列表</h1>
        <input type="text" placeholder="请输入名字" v-model='keyWord'>
        <ul>
            <li v-for='(person, index) in filterPersons' :key='person.id'>
                {{index}}.{{person.name}}-{{person.age}}-{{person.sex}}
            </li>
        </ul>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                keyWord: null,
                persons: [
                    { id: '001', name: '马冬梅', age: 18, sex: '女' },
                    { id: '002', name: '周冬雨', age: 19, sex: '女' },
                    { id: '003', name: '周杰伦', age: 20, sex: '男' },
                    { id: '004', name: '温兆伦', age: 21, sex: '男' },
                ],

                filterPersons: []
            },

            watch: {
                // 使用该方法，原persons对象被修改，数据越查越少
                // keyWord(val) {
                //     this.persons = this.persons.filter((p) => {
                //         return p.name.indexOf(val) >= 0;
                //     });
                // }

                keyWord: {
                    immediate: true,
                    handler(val) {
                        // 使用!val判断字符串是否为空
                        if (!val) {
                            this.filterPersons = this.persons;
                        } else {
                            this.filterPersons = this.persons.filter((p) => {
                                return p.name.indexOf(val) >= 0;
                            });
                        }
                    }
                }
            }
        });
    </script>
</body>
```



#### 计算属性实现

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <h1>人员列表</h1>
        <input type="text" placeholder="请输入名字" v-model='keyWord'>
        <ul>
            <li v-for='(person, index) in filterPersons' :key='person.id'>
                {{index}}.{{person.name}}-{{person.age}}-{{person.sex}}
            </li>
        </ul>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                keyWord: null,
                persons: [
                    { id: '001', name: '马冬梅', age: 18, sex: '女' },
                    { id: '002', name: '周冬雨', age: 19, sex: '女' },
                    { id: '003', name: '周杰伦', age: 20, sex: '男' },
                    { id: '004', name: '温兆伦', age: 21, sex: '男' },
                ]
            },

            computed: {
                filterPersons() {
                    if (!this.keyWord) {
                        return this.persons;
                    }

                    return this.persons.filter((p) => {
                        return p.name.indexOf(this.keyWord) >= 0;
                    });
                }
            }
        });
    </script>
</body>
```



#### 结果

![image-20220904160200132](../../../md-photo/image-20220904160200132.png)





### 列表排序

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <h1>人员列表</h1>
        <input type="text" placeholder="请输入名字" v-model='keyWord'>
        <button @click='sortType = 2'>年龄升序</button>
        <button @click='sortType = 1'>年龄降序</button>
        <button @click='sortType = 0'>原顺序</button>
        <ul>
            <li v-for='(person, index) in filterPersons' :key='person.id'>
                {{index}}.{{person.name}}-{{person.age}}-{{person.sex}}
            </li>
        </ul>
    </div>

    <script type="text/javascript">
        const vm = new Vue({
            el: '#root',
            data: {
                keyWord: null,
                sortType: 0,// 0：原顺序；1：降序；2：升序
                persons: [
                    { id: '001', name: '马冬梅', age: 22, sex: '女' },
                    { id: '002', name: '周冬雨', age: 19, sex: '女' },
                    { id: '003', name: '周杰伦', age: 20, sex: '男' },
                    { id: '004', name: '温兆伦', age: 21, sex: '男' },
                ]
            },

            computed: {
                filterPersons() {
                    let filterPersons;
                    if (!this.keyWord) {
                        // 复制persons一个副本为filterPersons
                        filterPersons = [...this.persons];
                        // 该种写法是直接内存指针的引用，修改filterPersons就等于在修改persons
                        // filterPersons = this.persons;
                    } else {
                        filterPersons = this.persons.filter((p) => {
                            return p.name.indexOf(this.keyWord) >= 0;
                        });
                    }

                    // 判断是否需要排序
                    // 0为布尔的false，1，2为布尔的true
                    if (this.sortType) {
                        filterPersons.sort((p1, p2) => {
                            return this.sortType === 2 ? p1.age - p2.age : p2.age - p1.age;
                        });
                    }

                    // 不需要排序就直接返回原数组
                    return filterPersons;
                }
            }
        });
    </script>
</body>
```



结果：

![image-20220904162959969](../../../md-photo/image-20220904162959969.png)



![image-20220904163020261](../../../md-photo/image-20220904163020261.png)



![image-20220904163044614](../../../md-photo/image-20220904163044614.png)





### Vue数据监测原理

#### 监测数据改变的原理（对象）

Vue data中的数据通过观察者模式创建新的\_data对象，\_data对象对data中的所有元素添加了getter、setter，在setter方法的时候就可以监听到数据的变化，最后再把\_data通过数据代理传递到vm身上。

```html
<body>
    <script type="text/javascript">
        let data = {
            name: '尚硅谷',
            address: '鸿福科技园'
        };

        // 这种写法会导致内存溢出get、set方法会被无限的调用
        // Object.defineProperty(data, 'name', {
        //     get() {
        //         return data.name;
        //     },

        //     set(val) {
        //         data.name = val;
        //     }
        // });


        // 创建一个监视实例的对象，用于监视data中的属性变化
        const obs = new Observer(data);

        // 准备一个vm对象
        let vm = {};
        vm._data = data = obs;

        // 使用观察者模式
        function Observer(obj) {
            // 遍历
            const keys = Object.keys(obj);
            keys.forEach((k) => {
                // 这里的this是观察者对象自身
                Object.defineProperty(this, k, {
                    get() {
                        return obj[k];
                    },

                    set(val) {
                        // 其中`${k}属性被修改了...`为模板引擎的写法
                        console.log(`${k}属性被修改了...`);
                        obj[k] = val;
                    }
                });
            });
        };
    </script>
</body>
```



如果get、set无限被调用会出现一下结果：

get方法：

![image-20220904172117604](../../../md-photo/image-20220904172117604.png)

set方法：

![image-20220904172200212](../../../md-photo/image-20220904172200212.png)

测试结果：

![image-20220904172002828](../../../md-photo/image-20220904172002828.png)



#### Vue.set方法

普通方式追加属性（this.student.sex = '男';），Vue不会给这个属性添加getter和setter，导致vm中的数据变了，但是vm无法监视到其已经改变，我们可以通过Vue的api来解决这个问题：

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <h1>姓名：{{student.name}}</h1>
        <button @click='addSex'>添加性别属性，默认为男</button>
        <!-- 使用v-show控制性别标签是否展示 -->
        <h1 v-show='student.sex'>性别：{{student.sex}}</h1>
        <h1>朋友们</h1>
        <ul>
            <li v-for='(friend, index) in student.friends' :key='index'>
                {{friend.name}} -- {{friend.age}}
            </li>
        </ul>
    </div>

    <script type="text/javascript">
        const vm = new Vue({
            el: '#root',
            data: {
                student: {
                    name: '张三',
                    // sex: '男',
                    friends: [
                        { name: 'jerry', age: 35 },
                        { name: 'tony', age: 36 },
                    ]
                }
            },

            methods: {
                addSex() {
                    // 这种方式追加的属性在vm中没有getter和setter
                    // this.student.sex = '男';
                    // 使用Vue的api进行数据的追加，以下两种方式都可以
                    // this.$set(this.student, 'sex', '男');
                    // set方法只能给data里面的属性（如student）追加属性，而不能直接给data追加属性
                    Vue.set(this.student, 'sex', '男');
                }
            },
        });
    </script>
</body>
```



结论：

使用this.student.sex = '男';

![image-20220904205546019](../../../md-photo/image-20220904205546019.png)



使用Vue的api的方式：

![image-20220904205659551](../../../md-photo/image-20220904205659551.png)





#### 监测数据改变的原理（数组）

```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <h1>朋友们</h1>
        <ul>
            <li v-for='(friend, index) in student.friends' :key='index'>
                {{friend.name}} -- {{friend.age}}
            </li>
        </ul>

        <h1>爱好</h1>
        <ul>
            <li v-for='(hobby, index) in student.hobbys' :key='index'>
                {{hobby}}
            </li>
        </ul>

    </div>

    <script type="text/javascript">
        const vm = new Vue({
            el: '#root',
            data: {
                student: {
                    hobbys: ['抽烟', '喝酒', '烫头'],
                    friends: [
                        { name: 'jerry', age: 35 },
                        { name: 'tony', age: 36 },
                    ]
                }
            }
        });
    </script>
</body>
```



结果：

![image-20220904211907310](../../../md-photo/image-20220904211907310.png)



由此可见，我们使用普通的数组赋值语句进行修改的时候，vm无法监测到变化：

```javascript
// vm无法监测到数据的变化
vm.student.hobbys[0] = '打游戏';
```



使用Array的<font color='red'>[push、pop、shift、unshift、splice、sort、reverse]</font>方法时，可以让vm监测到数据的变化，Vue使用了代理模式，在调用这些方法时，可以被vm监测到。

```javascript
vm.student.hobbys.unshift('打游戏');
```

![image-20220904212411761](../../../md-photo/image-20220904212411761.png)



当然使用Vue.set方法也可以实现，不过不推荐使用：

```javascript
// 也可以用来添加元素，只要第二个元素比最大索引大就可以实现
Vue.set(vm.student.hobbys, 0, '打游戏');
```

![image-20220904212657855](../../../md-photo/image-20220904212657855.png)



#### 数据监测总结

Vue监视数据的原理:

- vue会监视data中所有层次的数据。

- 如何监测对象中的数据?

  > 通过setter实现监视，且要在new Vue时就传入要监测的数据。

  - 对象中后追加的属性，Vue默认不做响应式处理

  - 如需给后添加的属性做响应式，请使用如下API:
    - Vue.set(target, propertyName/index, value) 
    - vm.$set(target, propertyName/index, value)

- 如何监测数组中的数据?

  >  通过包裹数组更新元素的方法实现，本质就是做了两件事:

  - 调用原生对应的方法对数组进行更新。

  - 重新解析模板，进而更新页面。

    

- 在Vue修改数组中的某个元素一定要用如下方法:
  - 使用这些API:push()、pop()、shift()、unshift()、splice()、sort()、reverse()
  - Vue.set()或vm.$set()

​       **<font color='red'> 特别注意: Vue.set()和vm.$set()不能给vm或vm的根数据对象添加属性！！！</font>**



```html
<body>
    <!-- 准备一个容器 -->
    <div id='root'>
        <h1>学生信息</h1>
        <!-- 添加对应的按钮事件 -->
        <button @click='student.age++'>年龄+1岁</button><br />
        <button @click='addSex'>添加性别属性，默认值：男</button><br />
        <button @click='student.sex = "未知"'>修改性别</button><br />
        <button @click='addFriend'>在列表首位添加一个朋友</button><br />
        <button @click='updateName'>修改第一个朋友的名字为：张三</button><br />
        <button @click='addHobby'>添加一个爱好</button><br />
        <button @click='updateHobby'>修改第一个爱好为：开车</button><br />
        <button @click='filterHobby'>过滤掉爱好中的抽烟</button><br />

        <h2>姓名：{{student.name}}</h2>
        <h2>年龄：{{student.age}}</h2>
        <h2 v-show='student.sex'>性别：{{student.sex}}</h2>
        <h2>爱好：</h2>
        <ul>
            <li v-for='(hobby, index) in student.hobbys' ::key="index">
                {{hobby}}
            </li>
        </ul>

        <h2>朋友们：</h2>
        <ul>
            <li v-for='(friend, index) in student.friends' ::key="index">
                {{friend.name}} -- {{friend.age}}
            </li>
        </ul>
    </div>

    <script type="text/javascript">
        new Vue({
            el: '#root',
            data: {
                student: {
                    name: '张三',
                    age: 18,
                    hobbys: ['抽烟', '喝酒', '烫头'],
                    friends: [
                        { name: 'jerry', age: 35 },
                        { name: 'tony', age: 36 },
                    ]
                }
            },

            methods: {
                addSex() {
                    // Vue.set(this.student, 'sex', '男');
                    this.$set(this.student, 'sex', '男');
                },

                addFriend() {
                    this.student.friends.unshift({ name: 'Tom', age: 65 });
                },

                updateName() {
                    this.student.friends[0].name = '张三';
                },

                addHobby() {
                    this.student.hobbys.unshift('打游戏');
                },

                updateHobby() {
                    // 数组中的元素不是对象，不能使用直接赋值的方式进行修改
                    // this.student.hobbys[0] = '开车';
                    this.$set(this.student.hobbys, 0, '开车');
                },

                filterHobby() {
                    // 不是变更原来的数组时，直接用新的数组替换掉原来的数组即可
                    this.student.hobbys = this.student.hobbys.filter((hobby) => {
                        return hobby !== '抽烟';
                    });
                }
            },
        });
    </script>
</body>
```



案例测试结果：

![image-20220904222638141](../../../md-photo/image-20220904222638141.png)
