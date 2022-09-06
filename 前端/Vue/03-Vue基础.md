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



