:<<!
Shell文件包含：
Shell可以包含外部脚本。这样方便封装一些公共的代码作为一个独立的文件
Shell文件包含的语法格式如下：
	. filename #注意点号(.)和文件名中间有一空格
	或者
	source filename
!

#示例
#创建两个shell脚本，contains1.sh如下：
#!/bin/sh
url="http://www.runoob.com";

#contains2.sh脚本如下：
#!/bin/sh

#使用 . 号来引用test1.sh文件
. ./contains.sh;
#使用 source 来引用test1.sh文件
source /home/ccms/simple/shellscript/contains.sh
echo "菜鸟教程官网地址：${url}";

#注意：被包含的文件test1.sh不需要可执行权限