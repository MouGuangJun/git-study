#!/bin/sh
#用*号打印三角形
for i in $(seq 10)
do
	for j in $(seq ${i})
	do
		echo -n "*";
	done;
echo -e "\r\n";
done;