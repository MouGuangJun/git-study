#!/bin/sh
:<<COMMENT
�ļ��������ڼ���ļ��ĸ�������
-b file ����ļ��Ƿ��ǿ��豸�ļ���������򷵻�true
-c file ����ļ��Ƿ����ַ��豸�ļ�������Ƿ���true
-d file ����ļ��Ƿ���Ŀ¼������Ƿ���true
-f file ����ļ��Ƿ�����ͨ�ļ�������Ƿ���true
-g file ����ļ��Ƿ�������SGIDλ������Ƿ���true
-k file ����ļ��Ƿ�������մ��λ������Ƿ���true
-p file ����ļ��Ƿ��������ܵ���������򷵻�true
-u file ����ļ��Ƿ�����SUIDλ����������򷵻�true
-r file ����ļ��Ƿ�ɶ���������򷵻�true
-w file ����ļ��Ƿ��д��������򷵻�true
-x file ����ļ��Ƿ��ִ�У�������򷵻�true
-s file ����ļ��Ƿ�Ϊ�գ��ļ���С�Ƿ����0������Ϊ���򷵻�true
-e file ����ļ�������Ŀ¼���Ƿ���ڣ�����ǣ��򷵻�true
COMMENT

#ʾ�����ļ�Ŀ¼[/home/ccms/simple/shellscript/test.sh]����СΪ300�ֽڣ�����rwxȨ��
file="/home/ccms/simple/shellscript/test.sh";
if [ -r ${file} ]
then
	echo "�ļ��ɶ���";
else
	echo "�ļ����ɶ���";
fi;

if [ -w ${file} ]
then
	echo "�ļ���д��";
else
	echo "�ļ�����д��";
fi;

if [ -x ${file} ]
then
	echo "�ļ���ִ�У�";
else
	echo "�ļ�����ִ�У�";
fi;

if [ -f ${file} ]
then 
	echo "�ļ�Ϊ��ͨ�ļ���";
else
	echo "�ļ�Ϊ�����ļ���";
fi;

if [ -d ${file} ]
then 
	echo "�ļ���һ��Ŀ¼��";
else
	echo "�ļ�����һ��Ŀ¼��";
fi;

if [ -s ${file} ]
then
	echo "�ļ���Ϊ�գ�";
else
	echo "�ļ�Ϊ�գ�";
fi;

if [ -e ${file} ]
then
	echo "�ļ����ڣ�";
else
	echo "�ļ������ڣ�";
fi;