# JavaScript常识

## 关键字

### let

声明的变量可以改变，值和类型都可以改变。



### const

声明的常量不可以改变，这意味着，const一旦声明，就必须立即初始化，不能以后再赋值。



## 数组的修改

| 指令    | 操作                                                         |
| ------- | ------------------------------------------------------------ |
| push    | 右边添加一个元素，push('元素')                               |
| pop     | 右边删除一个元素，pop()                                      |
| shift   | 左边删除一个元素，shfit()                                    |
| unshift | 左边添加一个元素，unshift('元素')                            |
| splice  | 对指定的索引位置元素进行修改，splice（替换元素的序号, 替换长度[几个元素], '替换内容'） |
| sort    | 对数组进行排序                                               |
| reverse | 对数组进行倒序的操作                                         |



## 解构赋值

```javascript
// 将对象解构
let {foo, bar} = { foo: "aaa", bar: "bbb" };
console.log(foo); // "aaa"

// 将tool对象中的所有属性解析到tools中
let tool = { fool: "aaa", bar: "bbb" };
let tools = {
    baz: "ccc",
    ...tool
}
console.log(tools); // { baz: 'ccc', fool: 'aaa', bar: 'bbb' }
```

