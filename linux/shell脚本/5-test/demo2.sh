#!/bin/sh
:<<!
判断指定目录[/home/ccms/simple/shellscript]是否存在temp文件夹，如果不存在则创建文件夹
!
if [ ! -d "/home/ccms/simple/shellscript/temp" ]
then
	mkdir /home/ccms/simple/shellscript/temp;
	echo "文件夹创建成功！"
else
	echo "文件夹已存在！";
fi;

:<<!
判断指定目录[/home/ccms/simple/shellscript]是否存在temp文件夹，如果存在则删除文件夹
!
if [ -d "/home/ccms/simple/shellscript/temp" ]
then
	rm -rf /home/ccms/simple/shellscript/temp;
	echo "文件夹移除成功！";
else
	echo "文件不存在！";
fi;

:<<!
判断指定目录[/home/ccms/simple/shellscript]是否存在temp.txt文件，如果不存在则创建文件
!

if [ ! -f "/home/ccms/simple/shellscript/temp.txt" ]
then
	touch /home/ccms/simple/shellscript/temp.txt;
	echo "文件创建成功！";
else
	echo "文件已经存在！";
fi;

:<<!
判断指定目录[/home/ccms/simple/shellscript]是否存在temp.txt文件，如果存在则删除文件
!

if [ -f "/home/ccms/simple/shellscript/temp.txt" ]
then
	rm /home/ccms/simple/shellscript/temp.txt;
	echo "文件删除成功！";
else
	echo "文件不存在！";
fi;