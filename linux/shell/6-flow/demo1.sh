#!/bin/sh
:<<COMMENT
sh�����else��֧û��ִ�е����ݾͲ���дelse
if����﷨��ʽ��
if condition
then
	command1;
	command2;
	...
	commandn;
fi;

COMMENT
#ʾ��
if [ `ps -ef | grep -c "ssh"` -gt 1 ]
then
	echo "true";
fi;

:<<COMMENT
if else�﷨��
if condition
then
	command1;
	command2
	...
	commandn;
else
	command;
fi;

if else-if else�﷨��
if condition1
then
	command1;
elif condition2
then
	command2;
else
	commandn;
fi;


COMMENT
#ʾ��
a=10;
b=20;
if [ ${a} == ${b} ]
then
	echo "a ���� b��";
elif [ ${a} -lt ${b} ]
then
	echo "a С�� b��";
elif [ ${a} -gt ${b} ]
then
	echo "a ���� b��";
else
	echo "û���ʺϵ�������";
fi;

#if else ��侭����test������ʹ��
num1=`expr 2+3`;
num2=`expr 1+5`;
if test $[num1] -eq $[num2]
then
	echo "����������ȣ�";
else
	echo "�������ֲ���ȣ�";
fi;
