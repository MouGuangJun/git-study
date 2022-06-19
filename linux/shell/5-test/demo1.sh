#!/bin/sh

:<<COMMENT
Shell中的test命令用于检查某个条件是否成立，它可以进行数值、字符和文件三个方面的测试
COMMENT

:<<COMMENT
数值测试:
-eq：等于则为真
-ne：不等则为真
-gt：大于则为真
-ge：大于等于则为真
-lt：小于则为真
-le：小于等于则为真
COMMENT

#示例
num1=100;
nums=100;
if test $[num1] -eq $[num2]
then
	echo "两个数相等！";
else
	echo "两个数不相等！";
fi;

#代码中的[]执行基本的算数操作，如：
a=5;
b=6;
result=$[a+b];
echo "result为：${result}！";

:<<COMMENT
字符串测试:
=：等于则为真
!=：不等于则为真
-z：字符串的长度为0则为真
-n：字符串的长度不为0则为真
COMMENT

#示例
str1="runoob1";
str2="runoob2";
if test ${str1} = ${str2}
then
	echo "两个字符串相等！";
else
	echo "两个字符串不相等！";
fi;

:<<COMMENT
文件测试：
-e file 如果文件存在则为真
-r file 如果文件存在且可读则为真
-w file 如果文件存在且可写则为真
-x file 如果文件存在且可执行则为真
-s file 如果文件存在且至少有一个字符则为真
-d file 如果文件存在且为目录则为真
-f file 如果文件存在且为普通文件则为真
-c file 如果文件存在且为字符型特殊文件则为真
-b file 如果文件存在且为块特殊文件则为真
COMMENT
#示例
cd /bin
if test -e ./bash
then
	echo "文件已存在！";
else
	echo "文件不存在！";
fi;

#另外，Shell还提供了与(-a)、或(-o)、非(!)三个逻辑操作符用于将测试的条件连起来，优先级：! -a -o
#示例
cd /bin
if test -e ./notFile -o -e ./bash
then
	echo "至少有一个文件存在！";
else
	echo "两个文件都不存在！";
fi;