#!/bin/sh
:<<!
forѭ����һ���ʽΪ��
for var in item1 item2 ... itemn
do
	command1;
	command2;
	...
	commandn;
done;
������ֵ���б��forѭ����ִ��һ���������ʹ�ñ�������ȡ�б��еĵ�ǰȡֵ�������Ϊ�κ���Ч��shell�������䡣in�б���԰���
�滻���ַ������ļ�����in�б��ǿ�ѡ�ģ������������forѭ��ʹ��������λ�ò���
!

for loop in 1 2 3 4 5
do
	echo "This value is��${loop}";
done;

#����˳������ַ����е��ַ�
for str in This is a string
do
	echo ${str};
done;

:<<!
while���
while ѭ�����ڲ���ִ��һϵ�����Ҳ���ڴ������ļ��ж�ȡ���ݡ����﷨Ϊ��
while condtion
do
	command;
done;
!

#�������1��5
intVal=1;
while test $[intVal] -le 5
do
	echo ${intVal};
	intVal=`expr ${intVal} + 1`;
	#let "intVal++";
done;

:<<!
let���������ִ��һ���������ʽ�����������в���Ҫ����$����ʾ����

whileѭ���������ڶ�ȡ������Ϣ����<Ctrl+D>����ѭ��
!
echo "����<Ctrl+D>�˳�";
echo -n "��������ϲ������վ����";
while read SITE
do
	echo "�ǵģ�${SITE}��һ������վ��";
	echo -n "��������ϲ������վ����";
done;

:<<!
����ѭ����
�﷨��ʽ��
while :
do
	command;
done;

while true
do
	command;
done;

for (( ; ; ))
!

while :
do
	echo "1";
done;

while true
do
	echo "a";
done;

for (( ; ; ))
do
	echo "f";
done;

:<<!
untilѭ��
untilѭ��ִ��һϵ����ֱ������Ϊtrueʱֹͣ
untilѭ����whileѭ���ڴ���ʽ�ϸպ��෴
�﷨��ʽ��
util condition
do
	command;
done;
!

#ʾ��
a=0;
until test $[a] -ge 10
do
	echo ${a};
	a=`expr ${a} + 1`;
done;

:<<!
case...esac Ϊ��ѡ����䣬��switch...case������ơ�ÿ��case��֧����Բ���ſ�ʼ���������ֺ�;;����
�﷨��ʽ���£�
case ֵ in
ģʽ1)
	command1;
	command2;
	...
	commandn;
	;;
ģʽ2)
	command1;
	command2;
	...
	commandn;
	;;
esac;
!

#ʾ��
while true
do
	echo "����1��4֮������֣�";
	read aNum;
	case ${aNum} in
		1)
			echo "��ѡ����1";
		;;
		2)
			echo "��ѡ����2";
		;;
		3)
			echo "��ѡ����3";
		;;
		4)
			echo "��ѡ����4";
		;;
		*)
			echo "��û������1��4֮�������";
		;;
	esac;
done;

#ƥ���ַ���
echo "��������վ��";
read aStr;
case ${aStr} in
	"runoob")
		echo "����̳̣�";
	;;
	"google")
		echo "Google ������";
	;;
	"taobao")
		echo "�Ա�����";
	;;
	*)
		echo "δ֪��վ��";
	;;
esac;
	
:<<!
����ѭ����
break�������е�ѭ������ִֹ�к��������ѭ��
continue��break���ƣ������������������е�ѭ��������������ǰѭ��
!

#breakʾ��
while true
do
	echo -n "����1��5֮������֣�";
	read aNum;
	case ${aNum} in 
		1|2|3|4|5)
			echo "�����������Ϊ��${aNum}";
		;;
		*)
			echo "��������ֲ���1��5֮�䣬Game Over��";
			break;
		;;
	esac;
done;

#continueʾ����Game Over����Զ���ᱻִ�У���Ϊcontinue�Ѿ�������һ��ѭ����
while true
do
	echo -n "����1��5֮������֣�";
	read aStr;
	case ${aStr} in
		1|2|3|4|5)
			echo "�����������Ϊ��${aStr}";
		;;
		*)
			echo "��������ֲ���1��5֮��";
			continue;
			echo "Game Over��";
		;;
	esac;
done;