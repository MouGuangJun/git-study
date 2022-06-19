#!/bin/sh
#使用readonly命令可以将变量定义为只读变量，只读变量的值不能被改变
myUrl="https://www.google.com"
readonly myUrl
#myUrl="https://www.runoob.com"[执行该语句会报错 => myUrl: readonly variable]

#使用unset命令可以删除变量。语法：unset variable_name[变量]
yourUrl="https://www.baidu.com"
unset yourUrl
echo ${yourUrl}
#执行上面的语句不会有任何的输出