#!/bin/sh
:<<COMMENT
�ַ��������
=  ��������ַ����Ƿ���ȣ���ȷ���true
!= ��������ַ����Ƿ���ȣ�����ȷ���true
-z ����ַ��������Ƿ�Ϊ0��Ϊ0����true
-n ����ַ��������Ƿ�Ϊ0����Ϊ0����true
$  ����ַ��Ƿ�Ϊ�գ���Ϊ�շ���true
COMMENT

a="abc";
b="efg";

if [ "${a}" = "${b}" ]
then 
	echo "${a} = ${b}��a ���� b";
else
	echo "${a} = ${b}��a ������ b";
fi;

if [ "${a}" != "${b}" ]
then 
	echo "${a} != ${b}��a ������ b";
else
	echo "${a} != ${b}��a ���� b";
fi;

if [ -z "${a}" ]
then
	echo "-z ${a}���ַ�������Ϊ 0";
else
	echo "-z ${a}���ַ������Ȳ�Ϊ 0";
fi;

if [ -n "${a}" ]
then
	echo "-n ${a}���ַ������Ȳ�Ϊ 0";
else
	echo "-n ${a}���ַ�������Ϊ 0";
fi;

if [ "${a}" ]
then
	echo "${a}���ַ�����Ϊ�գ�";
else
	echo "${a}���ַ���Ϊ�գ�";
fi;