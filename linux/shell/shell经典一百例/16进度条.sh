#!/bin/sh
:<<COMMENT
动态时针版本；定义一个显示进度的函数，屏幕快速显示|/-\\
COMMENT

function rotate_line(){
INTERVAL=0.5;#设置间隔时间
COUNT="0";#设置4个形状的编号，默认编号为0（不代表任何图像）
while true
do
	COUNT=`expr ${COUNT} + 1`;	#执行循环，COUNT每次循环加1，（分别代表4种不同的形状）
	case ${COUNT} in			#判断COUNT的值，值不一样显示的形状就不一样
		"1")					#值为1显示-
			echo -e '-'"\b\c";
			sleep ${INTERVAL};
		;;
		
		"2")					#值为2显示\\，第一个\为转义符
			echo -e '\\'"\b\c";
			sleep ${INTERVAL};
		;;
		
		"3")					#值为3显示|
			echo -e '|'"\b\c";
			sleep ${INTERVAL};
		;;
		
		"4")					#值为4显示/
			echo -e '/'"\b\c";
			sleep ${INTERVAL};
		;;
		
		*)					#值为其他的时候将COUNT重置为0
		 COUNT="0";
		 echo -n "-";
		 ;;
	esac;
done;
}

rotate_line;