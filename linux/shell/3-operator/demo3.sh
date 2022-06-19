#!/bin/sh

:<<COMMENT
下表列出了常用的布尔运算符号
!：非运算，表达式为true则返回false，否则返回true
/-o：或运算，有一个表达式为true则返回true
/-a：与运算，两个表达式都是true才返回true
COMMENT

#示例
a=10;
b=20;
#非运算
if [ ${a} != ${b} ]
then
	echo "${a} != ${b}：a 不等于 b";
else
	echo "${a} == ${b}：a 等于 b";
fi

#与运算
if [ ${a} -lt 100 -a ${b} -gt 15 ]
then
	echo "${a} 小于 100 且 ${b} 大于 15：返回true";
else
	echo "${a} 小于 100 且 ${b} 大于 15：返回false";
fi

#或运算
if [ ${a} -lt 100 -o ${b} -gt 100 ]
then
	echo "${a} 小于 100 且 ${b} 大于 100：返回true";
else
	echo "${a} 小于 100 且 ${b} 大于 100：返回false";
fi

if [ ${a} -lt 5 -o ${b} -gt 100 ]
then
	echo "${a} 小于 5 且 ${b} 大于 100：返回true";
else
	echo "${a} 小于 5 且 ${b} 大于 100：返回false";
fi
