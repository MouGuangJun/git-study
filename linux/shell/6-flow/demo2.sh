#!/bin/sh
:<<!
for循环的一般格式为：
for var in item1 item2 ... itemn
do
	command1;
	command2;
	...
	commandn;
done;
当变量值在列表里，for循环即执行一次所有命令，使用变量名获取列表中的当前取值。命令可为任何有效的shell命令和语句。in列表可以包含
替换、字符串和文件名。in列表是可选的，如果不用它，for循环使用命令行位置参数
!

for loop in 1 2 3 4 5
do
	echo "This value is：${loop}";
done;

#按照顺序输出字符串中的字符
for str in This is a string
do
	echo ${str};
done;

:<<!
while语句
while 循环用于不断执行一系列命令，也用于从输入文件中读取数据。其语法为：
while condtion
do
	command;
done;
!

#输出数字1到5
intVal=1;
while test $[intVal] -le 5
do
	echo ${intVal};
	intVal=`expr ${intVal} + 1`;
	#let "intVal++";
done;

:<<!
let命令：它用于执行一个或多个表达式，变量计算中不需要加上$来表示变量

while循环可以用于读取键盘信息。按<Ctrl+D>结束循环
!
echo "按下<Ctrl+D>退出";
echo -n "输入你最喜欢的网站名：";
while read SITE
do
	echo "是的！${SITE}是一个好网站！";
	echo -n "输入你最喜欢的网站名：";
done;

:<<!
无限循环：
语法格式：
while :
do
	command;
done;

while true
do
	command;
done;

for (( ; ; ))
!

while :
do
	echo "1";
done;

while true
do
	echo "a";
done;

for (( ; ; ))
do
	echo "f";
done;

:<<!
until循环
until循环执行一系列命直到条件为true时停止
until循环与while循环在处理方式上刚好相反
语法格式：
util condition
do
	command;
done;
!

#示例
a=0;
until test $[a] -ge 10
do
	echo ${a};
	a=`expr ${a} + 1`;
done;

:<<!
case...esac 为多选择语句，与switch...case语句类似。每个case分支用右圆括号开始，用两个分号;;结束
语法格式如下：
case 值 in
模式1)
	command1;
	command2;
	...
	commandn;
	;;
模式2)
	command1;
	command2;
	...
	commandn;
	;;
esac;
!

#示例
while true
do
	echo "输入1到4之间的数字：";
	read aNum;
	case ${aNum} in
		1)
			echo "您选择了1";
		;;
		2)
			echo "您选择了2";
		;;
		3)
			echo "您选择了3";
		;;
		4)
			echo "您选择了4";
		;;
		*)
			echo "您没有输入1到4之间的数字";
		;;
	esac;
done;

#匹配字符串
echo "请输入网站：";
read aStr;
case ${aStr} in
	"runoob")
		echo "菜鸟教程！";
	;;
	"google")
		echo "Google 搜索！";
	;;
	"taobao")
		echo "淘宝网！";
	;;
	*)
		echo "未知网站！";
	;;
esac;
	
:<<!
跳出循环：
break跳出所有的循环，终止执行后面的所有循环
continue与break类似，不过它不会跳出所有的循环，仅仅跳出当前循环
!

#break示例
while true
do
	echo -n "输入1到5之间的数字：";
	read aNum;
	case ${aNum} in 
		1|2|3|4|5)
			echo "您输入的数字为：${aNum}";
		;;
		*)
			echo "输入的数字不在1到5之间，Game Over！";
			break;
		;;
	esac;
done;

#continue示例：Game Over！永远不会被执行，因为continue已经跳到下一次循环了
while true
do
	echo -n "输入1到5之间的数字：";
	read aStr;
	case ${aStr} in
		1|2|3|4|5)
			echo "您输入的数字为：${aStr}";
		;;
		*)
			echo "输入的数字不在1到5之间";
			continue;
			echo "Game Over！";
		;;
	esac;
done;