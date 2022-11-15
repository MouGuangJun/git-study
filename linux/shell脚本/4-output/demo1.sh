#!/bin/sh
:<<COMMENT
 echo命令用于字符串的输出：格式为：echo string
COMMENT

#1.显示普通字符串
#输出普通的字符串
#输出：1.It is a test！
echo "1.It is a test！";

#双引号""可以省略
#输出：2.It is a test！
echo 2.It is a test！;

#2.显示转义字符
#输出："3.It is a test"！
echo "\"3.It is a test\"！";

#同样的双引号""可以省略
#输出："4.It is a test"！
echo \"4.It is a test\"！;
 
#3.显示变量 read 命令从标准输入中读取一行，并把输入行的每个字段的值指定给shell变量
#输入hlfan，输出1.hlfan It is a test！
read name;
echo "1.${name} It is a test！";
 
#echo -e 开启转义
#4.显示换行
#输出：
#OK！
#
echo -e "OK！\n";

#5.显示不换行
#输出：OK！It is a test
echo -e "OK！\c";
echo "It is a test";


#6.显示结果到定向文件
myFile="/home/ccms/simple/shellscript/log.log";
echo "It is a test！" > ${myFile};

#7.原样输出字符串，不进行转义或取变量(用单引号)
echo '${name}\"';

#8.显示命令行执行结果
#Tue Jan 4 12:01:16 CST 2022
echo `date`;
echo $(ls);