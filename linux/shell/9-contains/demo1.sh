:<<!
Shell�ļ�������
Shell���԰����ⲿ�ű������������װһЩ�����Ĵ�����Ϊһ���������ļ�
Shell�ļ��������﷨��ʽ���£�
	. filename #ע����(.)���ļ����м���һ�ո�
	����
	source filename
!

#ʾ��
#��������shell�ű���contains1.sh���£�
#!/bin/sh
url="http://www.runoob.com";

#contains2.sh�ű����£�
#!/bin/sh

#ʹ�� . ��������test1.sh�ļ�
. ./contains.sh;
#ʹ�� source ������test1.sh�ļ�
source /home/ccms/simple/shellscript/contains.sh
echo "����̳̹�����ַ��${url}";

#ע�⣺���������ļ�test1.sh����Ҫ��ִ��Ȩ��