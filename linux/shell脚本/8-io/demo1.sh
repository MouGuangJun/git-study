#!/bin/sh
:<<!
重定向命令列表：
command > file 将输出重定向到file
command < file 将输入重定向到file
command >> file 将输出以追加的方式重定向到file
n > file 将文件描述符为n的文件重定向到file
n >> file 将文件描述符为n的文件以追加的方式重定向到file
n >& m 将输出文件m和n合并
n <& m 将输入文件m和n合并
<< tag 将开始标记tag和结束标记tag之间的内容作为输入
!


:<<!
输出重定向语法：
command1 > file1
!

#示例 	输出结果：ccms     pts/0        2022-01-08 09:12 (192.168.106.59)
who > users;

#输出重定向会覆盖文件内容	输出结果：菜鸟教程：www.runoob.com
echo "菜鸟教程：www.runoob.com" > users;

#如果不希望文件内容被覆盖，可以使用>>追加到文件末尾		输出结果：菜鸟教程：www.runoob.com\n博客：www.csdn.com
echo "博客：www.csdn.com" >> users;

:<<!
输入重定向语法：
command1 < file
本来需要从键盘获取的输入命令会转移到文件读取内容
!

#示例
wc -l users; #输出结果： 2 users

wc -l < users; #输出结果： 2

#注意：上面两个例子的结果完全不同，第一个例子会输出文件名，第二个不会，因为它仅仅知道从标准输入读取内容
#command1 < infile > outfile 	同时替换输入和输出，执行command1，从文件infile读取内容，然后试过一次输出写入到outfile
grep ".*" < users > grepUsers;

:<<!
重定向深入讲解：
Linux命令运行时都会打开三个文件：
标准输入文件（stdin）：stdin的文件描述符为0，UNIX程序默认从stdin读取数据
标准输出文件（stdout）：stdout的文件描述符为1，Unix程序默认向stdout输出数据
标准错误文件（stderr）：stderr的文件描述符为2，Unix程序会向stderr流中写入错误信息
默认情况下，command > file将stdout重定向到file，command < file 将stdin重定向到file
!

#将stderr重定向到file command 2 > file
:<<!
创建error.sh文件：
#/bin/sh

echo "About to try to access a file that doesn't exist";
cat bad-filename.txt;
!

#执行命令：会将错误信息输入到file文件中 结果：About to try to access a file that doesn't exist
sh error.sh 2 > file;

#将stderr追加到file末尾 command 2 >> file 结果：About to try to access a file that doesn't exist\nAbout to try to access a file that doesn't exist
sh error.sh 2 >> file;

#将stdout和stderr合并后重定向到file：command > file 2 >&1或者command >> file 2>&1
sh error.sh > file 2>&1

#如果希望对stdin和stdout都重定向：command < file1 > file2

:<<!
执行命令：
find /etc -names "*.txt" > file 2>&1
从左往右执行，执行到>file，此时的stdout为file；而执行到2>&1时，表示stderr重定向到stdout，这里也就是file文件
因为该命令是错误的(-names应该是-name)。本来要输出到终端屏幕的错误信息：find: unknown predicate `-names'
被重定向到了stdout也就是file文件中，所以屏幕不会出现错误信息，而是打印到了file文件中
cat file就可以看到find: unknown predicate `-names'就在里面
!



:<<!
Here Document：是shell中的一种特殊的重定向方式，用来将输入重定向到一个交互式Shell脚本或程序
基本形式：
command << delimiter
	decument
delimiter
它的作用是将两个delimiter之间的内容(decument)作为输入传递给command

注意：
	结尾的delimiter一定要顶格写，前面不能有任何字符，后面也不能有任何字符，包括空格和tab缩进
	开始的delimiter前后的空格会被忽略掉
!

#示例 在命令行中通过wc -l 命令计算Here Document的行数：
wc -l << INPUT
	欢迎来到
	菜鸟教程
	www.runoob.com
INPUT

#可以将Here Document用在Shell脚本中
cat << EOF
	欢迎来到
	菜鸟教程
	www.runoob.com
EOF

:<<!
上述脚本输出结果：
	欢迎来到
	菜鸟教程
	www.runoob.com
!
