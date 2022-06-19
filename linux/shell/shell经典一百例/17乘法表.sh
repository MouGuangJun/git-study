#!/bin/sh
#9*9乘法表（编写shell 脚本，打印 9*9 乘法表）
for i in $(seq 9)
do
	for j in $(seq ${i})
	do
		echo -n "${j}*${i}=$[i*j] ";
	done;

echo -e "\r\n";
done;
	