#!/bin/sh
#����ʹ������������ֵ
#ѭ������/etc·���µ��ļ���
#for file in `ls /etc`;
for file in $(ls /etc);
do
echo ${file}
done