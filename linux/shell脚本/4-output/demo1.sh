#!/bin/sh
:<<COMMENT
 echo���������ַ������������ʽΪ��echo string
COMMENT

#1.��ʾ��ͨ�ַ���
#�����ͨ���ַ���
#�����1.It is a test��
echo "1.It is a test��";

#˫����""����ʡ��
#�����2.It is a test��
echo 2.It is a test��;

#2.��ʾת���ַ�
#�����"3.It is a test"��
echo "\"3.It is a test\"��";

#ͬ����˫����""����ʡ��
#�����"4.It is a test"��
echo \"4.It is a test\"��;
 
#3.��ʾ���� read ����ӱ�׼�����ж�ȡһ�У����������е�ÿ���ֶε�ֵָ����shell����
#����hlfan�����1.hlfan It is a test��
read name;
echo "1.${name} It is a test��";
 
#echo -e ����ת��
#4.��ʾ����
#�����
#OK��
#
echo -e "OK��\n";

#5.��ʾ������
#�����OK��It is a test
echo -e "OK��\c";
echo "It is a test";


#6.��ʾ����������ļ�
myFile="/home/ccms/simple/shellscript/log.log";
echo "It is a test��" > ${myFile};

#7.ԭ������ַ�����������ת���ȡ����(�õ�����)
echo '${name}\"';

#8.��ʾ������ִ�н��
#Tue Jan 4 12:01:16 CST 2022
echo `date`;
echo $(ls);