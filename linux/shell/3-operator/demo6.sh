#!/bin/sh
:<<COMMENT
文件测试用于检测文件的各种属性
-b file 检测文件是否是块设备文件，如果是则返回true
-c file 检测文件是否是字符设备文件，如果是返回true
-d file 检测文件是否是目录，如果是返回true
-f file 检测文件是否是普通文件，如果是返回true
-g file 检测文件是否设置了SGID位，如果是返回true
-k file 检测文件是否设置了沾着位，如果是返回true
-p file 检测文件是否是有名管道，如果是则返回true
-u file 检测文件是否设置SUID位，如果是则则返回true
-r file 检测文件是否可读，如果是则返回true
-w file 检测文件是否可写，如果是则返回true
-x file 检测文件是否可执行，如果是则返回true
-s file 检测文件是否为空（文件大小是否大于0），不为空则返回true
-e file 检测文件（包括目录）是否存在，如果是，则返回true
COMMENT

#示例：文件目录[/home/ccms/simple/shellscript/test.sh]，大小为300字节，具有rwx权限
file="/home/ccms/simple/shellscript/test.sh";
if [ -r ${file} ]
then
	echo "文件可读！";
else
	echo "文件不可读！";
fi;

if [ -w ${file} ]
then
	echo "文件可写！";
else
	echo "文件不可写！";
fi;

if [ -x ${file} ]
then
	echo "文件可执行！";
else
	echo "文件不可执行！";
fi;

if [ -f ${file} ]
then 
	echo "文件为普通文件！";
else
	echo "文件为特殊文件！";
fi;

if [ -d ${file} ]
then 
	echo "文件是一个目录！";
else
	echo "文件不是一个目录！";
fi;

if [ -s ${file} ]
then
	echo "文件不为空！";
else
	echo "文件为空！";
fi;

if [ -e ${file} ]
then
	echo "文件存在！";
else
	echo "文件不存在！";
fi;