#!/bin/sh
:<<!
�ж�ָ��Ŀ¼[/home/ccms/simple/shellscript]�Ƿ����temp�ļ��У�����������򴴽��ļ���
!
if [ ! -d "/home/ccms/simple/shellscript/temp" ]
then
	mkdir /home/ccms/simple/shellscript/temp;
	echo "�ļ��д����ɹ���"
else
	echo "�ļ����Ѵ��ڣ�";
fi;

:<<!
�ж�ָ��Ŀ¼[/home/ccms/simple/shellscript]�Ƿ����temp�ļ��У����������ɾ���ļ���
!
if [ -d "/home/ccms/simple/shellscript/temp" ]
then
	rm -rf /home/ccms/simple/shellscript/temp;
	echo "�ļ����Ƴ��ɹ���";
else
	echo "�ļ������ڣ�";
fi;

:<<!
�ж�ָ��Ŀ¼[/home/ccms/simple/shellscript]�Ƿ����temp.txt�ļ�������������򴴽��ļ�
!

if [ ! -f "/home/ccms/simple/shellscript/temp.txt" ]
then
	touch /home/ccms/simple/shellscript/temp.txt;
	echo "�ļ������ɹ���";
else
	echo "�ļ��Ѿ����ڣ�";
fi;

:<<!
�ж�ָ��Ŀ¼[/home/ccms/simple/shellscript]�Ƿ����temp.txt�ļ������������ɾ���ļ�
!

if [ -f "/home/ccms/simple/shellscript/temp.txt" ]
then
	rm /home/ccms/simple/shellscript/temp.txt;
	echo "�ļ�ɾ���ɹ���";
else
	echo "�ļ������ڣ�";
fi;