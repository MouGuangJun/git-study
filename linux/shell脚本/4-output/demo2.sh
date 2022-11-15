#!/bin/sh
:<<COMMENT
printf命令语法：
	printf format-string [arguments...]
format-string：为格式控制字符串
arguments：为参数列表

注意：默认的printf不会自动添加换行符，我们可以手动添加\n
COMMENT

#示例
printf "Hello, Shell！\n";

#示例：printf占位符进行打印
:<<COMMENT
%s %c %d %f 都是格式替换符，%s输出一个字符串，%d整形输出，%c输出一个字符，%f输出实数，以小数的形式输出
%-10s指一个宽度为10个字符（-表示左对齐，没有则表示右对齐）不足以空格填充，超过也会将所有的内容显示出来
%-4.2f指格式化为小数，其中.2表示保留两位小数
COMMENT
printf "%-10s %-8s %-4s\n" 姓名 性别 体重kg;
printf "%-10s %-8s %-4.2f\n" 郭靖 男 66.1234;
printf "%-10s %-8s %-4.2f\n" 杨过 男 48.6543;
printf "%-10s %-8s %-4.2f\n" 郭芙 女 47.9876;

#format-string 为双引号
printf "%d %s\n" 1 "abc";

#单引号和双引号一样
printf '%d %s\n' 1 "abc";

#没有引号也可以输出
printf %s\\n abcdef;

#格式只指定了一个参数，但多出的参数仍然会按照该格式输出，format-string被重用
printf %s\\n abc def;

printf "%s %s %s\n" a b c d e f g h i j

#如果没有arguments，那么%s用null代替，%d用0代替
printf "%s and %d\n";

:<<COMMENT
printf的转义序列：
\a：警告字符，通常为ASCII的BEL字符
\b：后退
\c：抑制（不显示）输出结果中任何结尾的换行字符， （只有%b格式指示符控制下的参数字符串中有效）
\f：换页
\n：换行
\r：回车
\t：水平制表符
\v：垂直制表符
\\：一个字面上的反斜杠字符
\ddd：表示1到3位数八进制的字符，经再格式字符串中有效
\0ddd：表示1到3位的八进制字符
COMMENT

#输出：a string, no processing:<A\nB>
printf "a string, no processing:<%s>\n" "A\nB";
#输出：a string, no processing:<A
#B>
printf "a string, no processing:<%b>\n" "A\nB";

printf "www.runnoob.com \a";