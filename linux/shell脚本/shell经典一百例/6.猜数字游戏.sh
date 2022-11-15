#!/bin/sh
#脚本生成一个100以内的随机数，提示用户猜数字，根据用户输入，提示用户猜对了、猜小了、猜大了，直至用户猜对，脚本结束
#RANDOM为系统自带的系统变量，值为0-32767的随机数
#使用取余法将随机数变为1-100的随机数
num=$[RANDOM%100+1];
echo "${num}";

#使用read提示用户猜数字
#使用if判断用户猜数字的大小关系：-eq（等于）,-ne（不等于）,-gt（大于）,-ge（大于等于）,-lt（小于）,-le（小于等于）
while true
do
	read -p "计算机生成了一个1-100的随机数，你猜：" input;
	if test $[input] -eq $[num]
	then
		echo "恭喜,猜对了！";
		exit;
	elif test $[input] -gt $[num]
	then
		echo "Oops,猜大了！";
	else
		echo "Oops,猜小了！";
	fi;
done;
