#!/bin/sh
#9*9�˷�����дshell �ű�����ӡ 9*9 �˷���
for i in $(seq 9)
do
	for j in $(seq ${i})
	do
		echo -n "${j}*${i}=$[i*j] ";
	done;

echo -e "\r\n";
done;
	