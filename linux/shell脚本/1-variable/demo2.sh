#!/bin/sh
#可以使用语句给变量赋值
#循环遍历/etc路径下的文件名
#for file in `ls /etc`;
for file in $(ls /etc);
do
echo ${file}
done