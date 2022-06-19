#!/bin/sh
:<<!
一、单引号：
单引号里的任何字符都会原样输出，单引号字符串中的变量是无效的；
单引号字符串不能出现单独一个的单引号(对单引号使用转义字符也不行)，但可成对出现，作为字符串拼接使用
!
str='this is a string'
echo ${str}

:<<!
二、双引号：
双引号里可以有变量
双引号里可以出现转义字符
!
your_name="runoob"
doublestr="Hello,I know you are \"$your_name\"! \n"
echo -e ${doublestr}

:<<!
拼接字符串：类似于java的字符串拼接，不过不需要使用+号
例如：
定义变量：your_name='三叶'
your_name=${your_name}"和花泷""是很好的朋友"", 他们一起拯救了世界！"
echo ${your_name}
!
#使用双引号拼接
greeting="hello,"$your_name"!"

greeting1="hello,${your_name}!" 
#hello,runoob! hello,runoob!
echo ${greeting} ${greeting1}

#使用单引号拼接
greeting2='hello,'$your_name'!'
#单引号字符串中不能直接调用变量
greeting3='hello,${your_name}!'
#hello,runoob! hello,${your_name}!
echo ${greeting2} ${greeting3}

:<<!
字符串常用的方法
!
#获取字符串的长度：关键字：[#]
string="runoob is a great site"
#22
echo ${#string}

#提取子字符串：关键字：[:]
#unoo
echo ${string:1:4}

#查找字符串：查找字符i或者o的位置，哪个字母先出现就计算哪个:
#4
echo `expr index "${string}" io`