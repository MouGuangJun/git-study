#!/bin/sh
#��*�Ŵ�ӡ������
for i in $(seq 10)
do
	for j in $(seq ${i})
	do
		echo -n "*";
	done;
echo -e "\r\n";
done;