#!/bin/sh
:<<COMMENT
sh中如果else分支没有执行的内容就不能写else
if语句语法格式：
if condition
then
	command1;
	command2;
	...
	commandn;
fi;

COMMENT
#示例
if [ `ps -ef | grep -c "ssh"` -gt 1 ]
then
	echo "true";
fi;

:<<COMMENT
if else语法：
if condition
then
	command1;
	command2
	...
	commandn;
else
	command;
fi;

if else-if else语法：
if condition1
then
	command1;
elif condition2
then
	command2;
else
	commandn;
fi;


COMMENT
#示例
a=10;
b=20;
if [ ${a} == ${b} ]
then
	echo "a 等于 b！";
elif [ ${a} -lt ${b} ]
then
	echo "a 小于 b！";
elif [ ${a} -gt ${b} ]
then
	echo "a 大于 b！";
else
	echo "没有适合的条件！";
fi;

#if else 语句经常与test命令结合使用
num1=`expr 2+3`;
num2=`expr 1+5`;
if test $[num1] -eq $[num2]
then
	echo "两个数字相等！";
else
	echo "两个数字不相等！";
fi;
