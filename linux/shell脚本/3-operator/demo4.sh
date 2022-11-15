#!/bin/sh
:<<!
&& 逻辑的AND
|| 逻辑的OR
!
a=10;
b=20;
#注意：逻辑运算表达式两边有两个[]中括号
if [[ ${a} -lt 100 && ${b} -gt 100 ]]
then
	echo "${a} 小于 100 并且 ${b} 大于 100：返回true。";
else
	echo "${a} 小于 100 并且 ${b} 大于 100：返回false。"
	
fi

if [[ ${a} -lt 100 || ${b} -gt 100 ]]
then
	echo "${a} 小于 100 或者 ${b} 大于 100：返回true。";
else
	echo "${a} 小于 100 或者 ${b} 大于 100：返回false。";
fi
