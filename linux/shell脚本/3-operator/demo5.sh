#!/bin/sh
:<<COMMENT
字符串运算符
=  检测两个字符串是否相等，相等返回true
!= 检测两个字符串是否不相等，不相等返回true
-z 检测字符串长度是否为0，为0返回true
-n 检测字符串长度是否不为0，不为0返回true
$  检测字符是否为空，不为空返回true
COMMENT

a="abc";
b="efg";

if [ "${a}" = "${b}" ]
then 
	echo "${a} = ${b}：a 等于 b";
else
	echo "${a} = ${b}：a 不等于 b";
fi;

if [ "${a}" != "${b}" ]
then 
	echo "${a} != ${b}：a 不等于 b";
else
	echo "${a} != ${b}：a 等于 b";
fi;

if [ -z "${a}" ]
then
	echo "-z ${a}：字符串长度为 0";
else
	echo "-z ${a}：字符串长度不为 0";
fi;

if [ -n "${a}" ]
then
	echo "-n ${a}：字符串长度不为 0";
else
	echo "-n ${a}：字符串长度为 0";
fi;

if [ "${a}" ]
then
	echo "${a}：字符串不为空！";
else
	echo "${a}：字符串为空！";
fi;