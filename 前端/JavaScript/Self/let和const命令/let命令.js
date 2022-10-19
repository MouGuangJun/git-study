// let声明的变量只有代码块内有效，var声明的全局都有效
{
    // let a = 10;
    var b = 1;
}

// console.log(a); // ReferenceError: a is not defined.
console.log(b);

var a = [];
for (var i = 0; i < 10; i++) {
    a[i] = function () {
        console.log(i);
    };
}

// var定义的变量全局都有效，所以这里都被修改成10了
a[6]();// 10

var a = [];
for (let i = 0; i < 10; i++) {
    a[i] = function () {
        console.log(i);
    };
}

// let声明时，当前的i只在本轮循环有效，每一次循环的i其实都是一个新的变量，所以最后输出的是6
a[6]();// 6

// for循环设置循环变量的那部分是一个父作用域，而循环体内部是一个单独的子作用域
for (let i = 0; i < 1; i++) {
    let i = "abc";
    console.log(i);
}
// abc

// var存在变量提升（变量可以在声明之前使用，值为undefined）
// var 的情况
console.log(foo); // 输出undefined
var foo = 2;

// let 的情况
// console.log(bar); // 报错ReferenceError
// let bar = 2;

function bar(x = 2, y = x) {
    return [x, y];
}

console.log(bar());
