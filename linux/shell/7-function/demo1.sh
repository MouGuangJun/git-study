#!/bin/sh
:<<!
Shell�п��Զ��庯������shell�ű����������
�����ʽ��
[ function ] funName [()]
{
	action;
	[return int;]
} 

���Դ�function fun()���壬Ҳ����ֱ��fun()���壬�����κβ���
�������أ�������ʾ�ӣ�return���أ�������ӣ��������һ���������н����Ϊ����ֵ��return �����ֵn(0-255
!
#ʾ��

demoFun(){
	echo "�����ҵĵ�һ��shell������";
}

echo "-----������ʼִ��-----";
demoFun;
echo "-----����ִ�����-----"


#����һ������return���ĺ���
#ע�⺯������ֵ�ڵ��ú�����ͨ��${?}��á����к�������Ҫ��ʹ��ǰ����
function funWithReturn(){
	echo "����������������������ֽ����������...";
	echo "�������һ�����֣�";
	read aNum;
	echo "������ڶ������֣�";
	read anotherNum;
	echo "�������ֱַ�Ϊ${aNum} �� ${anotherNum}";
	return `expr ${aNum} + ${anotherNum}`;
}
funWithReturn;
echo "�������������֮��Ϊ${?}��";

:<<!
����������
��Shell�У����ú���ʱ�������䴫�ݲ������ں������ڲ���ͨ��$n����ʽ����ȡ������ֵ�����磬$1��ʾ��һ��������$2��ʾ�ڶ�������

ע�⣺$10���ܻ�ȡ��ʮ����������ȡ��ʮ��������Ҫ��${10}����n>=10ʱ����Ҫ��${n}����ȡ����
!
funWithParam(){
	echo "��һ������Ϊ $1 ��";
	echo "�ڶ�������Ϊ $2 ��";
	echo "��ʮ������Ϊ $10 ��";
	echo "��ʮ������Ϊ ${10} ��";
	echo "��ʮһ������Ϊ${11}��";
	echo "����������$#����";
	echo "��Ϊһ���ַ���������в���${*}��";
}

funWithParam 1 2 3 4 5 6 7 8 9 34 73;


:<<!
$?������һ��ָ���һ���������غ��䷵��ֵû�������������������ô�䷵��ֵ��������ͨ��$?���
!
#ʾ��
function demoFun1(){
	echo "�����ҵĵ�һ��shell������";
	return `expr 1 + 1`;
}

function demoFun2(){
	echo "�����ҵĵڶ���shell����";
	return `expr 2 + 2`;
}

demoFun1;
echo "${?}";

demoFun2;
echo "${?}";

demoFun1;
echo "��������������";
echo "${?}";

:<<!
�����������ִ�н��������Ϊ�������ʹ�á�0����true��0�����ֵ����false
!

echo "Hello World��" | grep -e Hello;
echo "${?}";

echo "Hello World��" | grep -e Bye;
echo "${?}";

if echo "Hello World��" | grep -e Hello
then
	echo "true";
else
	echo "false";
fi;

if echo "Hello World��" | grep -e Bye
then
	echo "true";
else
	echo "false";
fi;

function demoFun1(){
	return 0;
}

function demoFun2(){
	return 12;
}

if demoFun1
then
	echo "true";
else
	echo "false";
fi;

if demoFun2
then
	echo "true";
else
	echo "false";
fi;


#!/bin/sh
if test -z ${1}
then
	echo "�������ѯ�ļ������ƣ�";
	exit;
fi;

aStr=${1};
cd /;
echo $(find ./ -name "*${aStr}");


#!/bin/sh
if test -z ${1}
then
	echo "�������ѯ�ļ������ƣ�";
	exit;
fi;

if test -z ${2}
then
	echo "�������ѯ��Ϣ��";
	exit;
fi;

echo $(cat ${1} | grep ${2});