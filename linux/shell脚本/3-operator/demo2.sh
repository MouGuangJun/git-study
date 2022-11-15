#!/bin/sh
:<<!
关系运算符
关系运算符只支持数字，不支持字符串，除非字符串的值是数字。
下列列出来常用的关系运算符：
-eq：检测两个数字是否相等，相等返回true
-ne：检测两个数字是否不相等，不相等返回true
-gt：检测左边的数是否大于右边的，如果是，则返回true
-lt：检测左边的数是否小于右边的，如果是，则返回true
-ge：检测左边的数是否大于等于右边的，如果是，则返回true
-ge：检测左边的数是否小于等于右边的，如果是，则返回true
!

#示例
a=10;
b=20;
#等于
if [ ${a} -eq ${b} ]
then
	echo "${a} -eq ${b}：a 等于 b";
else
	echo "${a} -eq ${b}：a 不等于 b";
fi

#不等于
if [ ${a} -ne ${b} ]
then
	echo "${a} -ne ${b}：a 不等于 b";
else
	echo "${a} -ne ${b}：a 等于 b";
fi

#大于
if [ ${a} -gt ${b} ]
then
	echo "${a} -gt ${b}：a 大于 b";
else
	echo "${a} -gt ${b}：a 不大于 b";
fi

#小于
if [ ${a} -lt ${b} ]
then
	echo "${a} -lt ${b}：a 小于 b";
else
	echo "${a} -lt ${b}：a 不小于 b";
fi

#大于等于
if [ ${a} -ge ${b} ]
then
	echo "${a} -ge ${b}：a 大于等于 b";
else
	echo "${a} -ge ${b}：a 小于 b";
fi

#小于等于
if [ ${a} -le ${b} ]
then
	echo "${a} -le ${b}：a 小于等于 b";
else
	echo "${a} -le ${b}：a 大于 b";
fi
