#!/bin/sh
:<<!
Shell中可以定义函数，在shell脚本中随机调用
定义格式：
[ function ] funName [()]
{
	action;
	[return int;]
} 

可以带function fun()定义，也可以直接fun()定义，不带任何参数
参数返回，可以显示加：return返回，如果不加，将以最后一条命令运行结果作为返回值。return 后跟数值n(0-255
!
#示例

demoFun(){
	echo "这是我的第一个shell函数！";
}

echo "-----函数开始执行-----";
demoFun;
echo "-----函数执行完毕-----"


#定义一个带有return语句的函数
#注意函数返回值在调用函数后通过${?}获得、所有函数都需要在使用前定义
function funWithReturn(){
	echo "这个函数会对输入的两个数字进行相加运算...";
	echo "请输入第一个数字：";
	read aNum;
	echo "请输入第二个数字：";
	read anotherNum;
	echo "两个数字分别为${aNum} 和 ${anotherNum}";
	return `expr ${aNum} + ${anotherNum}`;
}
funWithReturn;
echo "输入的两个数字之和为${?}！";

:<<!
函数参数：
在Shell中，调用函数时可以向其传递参数。在函数体内部，通过$n的形式来获取参数的值，例如，$1表示第一个参数，$2表示第二个参数

注意：$10不能获取第十个参数，获取第十个参数需要用${10}，当n>=10时，需要用${n}来获取参数
!
funWithParam(){
	echo "第一个参数为 $1 ！";
	echo "第二个参数为 $2 ！";
	echo "第十个参数为 $10 ！";
	echo "第十个参数为 ${10} ！";
	echo "第十一个参数为${11}！";
	echo "参数总数有$#个！";
	echo "作为一个字符串输出所有参数${*}！";
}

funWithParam 1 2 3 4 5 6 7 8 9 34 73;


:<<!
$?仅对上一条指令负责，一旦函数返回后其返回值没有立即保存入参数，那么其返回值将不再能通过$?获得
!
#示例
function demoFun1(){
	echo "这是我的第一个shell函数！";
	return `expr 1 + 1`;
}

function demoFun2(){
	echo "这是我的第二个shell函数";
	return `expr 2 + 2`;
}

demoFun1;
echo "${?}";

demoFun2;
echo "${?}";

demoFun1;
echo "在这里输入命令";
echo "${?}";

:<<!
函数与命令的执行结果可以作为条件语句使用。0代表true，0以外的值代表false
!

echo "Hello World！" | grep -e Hello;
echo "${?}";

echo "Hello World！" | grep -e Bye;
echo "${?}";

if echo "Hello World！" | grep -e Hello
then
	echo "true";
else
	echo "false";
fi;

if echo "Hello World！" | grep -e Bye
then
	echo "true";
else
	echo "false";
fi;

function demoFun1(){
	return 0;
}

function demoFun2(){
	return 12;
}

if demoFun1
then
	echo "true";
else
	echo "false";
fi;

if demoFun2
then
	echo "true";
else
	echo "false";
fi;


#!/bin/sh
if test -z ${1}
then
	echo "请输入查询文件的名称！";
	exit;
fi;

aStr=${1};
cd /;
echo $(find ./ -name "*${aStr}");


#!/bin/sh
if test -z ${1}
then
	echo "请输入查询文件的名称！";
	exit;
fi;

if test -z ${2}
then
	echo "请输入查询信息！";
	exit;
fi;

echo $(cat ${1} | grep ${2});