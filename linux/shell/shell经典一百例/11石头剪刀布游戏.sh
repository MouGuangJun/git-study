#!/bin/sh
#编写脚本，实现人机<石头,剪刀,布>游戏
game=(石头 剪刀 布);
num=$[RANDOM%3];
computer=${game[${num}]};
echo "${computer}";
#通过随机数获取计算机的出拳
#出拳的可能性保存在一个数组中，game[0],game[1],game[2]分别是三种不同的可能
echo -e "请根据下列提示选择您的出拳手势：\r\n1.石头\r\n2.剪刀\r\n3.布";

read -p "请选择 1 - 3：" person;
case ${person} in 
1)
	if test $[num] -eq 0
	then
		echo "平局";
	elif test $[num] -eq 1
	then
		echo "你赢";
	else
		echo "计算机赢";
	fi;
	;;
2)
	if test $[num] -eq 1
	then
		echo "平局";
	elif test $[num] -eq 2
	then
		echo "你赢";
	else
		echo "计算机赢";
	fi;
	;;
3)
	if test $[num] -eq 2
	then
		echo "平局";
	elif $[num] -eq 0
	then
		echo "你赢";
	else
		echo "计算机赢";
	fi;
	;;
*)
	echo "必须输入1 - 3的数字";
esac;
