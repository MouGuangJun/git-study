#!/bin/sh
:<<COMMENT
��̬ʱ��汾������һ����ʾ���ȵĺ�������Ļ������ʾ|/-\\
COMMENT

function rotate_line(){
INTERVAL=0.5;#���ü��ʱ��
COUNT="0";#����4����״�ı�ţ�Ĭ�ϱ��Ϊ0���������κ�ͼ��
while true
do
	COUNT=`expr ${COUNT} + 1`;	#ִ��ѭ����COUNTÿ��ѭ����1�����ֱ����4�ֲ�ͬ����״��
	case ${COUNT} in			#�ж�COUNT��ֵ��ֵ��һ����ʾ����״�Ͳ�һ��
		"1")					#ֵΪ1��ʾ-
			echo -e '-'"\b\c";
			sleep ${INTERVAL};
		;;
		
		"2")					#ֵΪ2��ʾ\\����һ��\Ϊת���
			echo -e '\\'"\b\c";
			sleep ${INTERVAL};
		;;
		
		"3")					#ֵΪ3��ʾ|
			echo -e '|'"\b\c";
			sleep ${INTERVAL};
		;;
		
		"4")					#ֵΪ4��ʾ/
			echo -e '/'"\b\c";
			sleep ${INTERVAL};
		;;
		
		*)					#ֵΪ������ʱ��COUNT����Ϊ0
		 COUNT="0";
		 echo -n "-";
		 ;;
	esac;
done;
}

rotate_line;