#!/bin/sh
:<<!
�ض��������б�
command > file ������ض���file
command < file �������ض���file
command >> file �������׷�ӵķ�ʽ�ض���file
n > file ���ļ�������Ϊn���ļ��ض���file
n >> file ���ļ�������Ϊn���ļ���׷�ӵķ�ʽ�ض���file
n >& m ������ļ�m��n�ϲ�
n <& m �������ļ�m��n�ϲ�
<< tag ����ʼ���tag�ͽ������tag֮���������Ϊ����
!


:<<!
����ض����﷨��
command1 > file1
!

#ʾ�� 	��������ccms     pts/0        2022-01-08 09:12 (192.168.106.59)
who > users;

#����ض���Ḳ���ļ�����	������������̳̣�www.runoob.com
echo "����̳̣�www.runoob.com" > users;

#�����ϣ���ļ����ݱ����ǣ�����ʹ��>>׷�ӵ��ļ�ĩβ		������������̳̣�www.runoob.com\n���ͣ�www.csdn.com
echo "���ͣ�www.csdn.com" >> users;

:<<!
�����ض����﷨��
command1 < file
������Ҫ�Ӽ��̻�ȡ�����������ת�Ƶ��ļ���ȡ����
!

#ʾ��
wc -l users; #�������� 2 users

wc -l < users; #�������� 2

#ע�⣺�����������ӵĽ����ȫ��ͬ����һ�����ӻ�����ļ������ڶ������ᣬ��Ϊ������֪���ӱ�׼�����ȡ����
#command1 < infile > outfile 	ͬʱ�滻����������ִ��command1�����ļ�infile��ȡ���ݣ�Ȼ���Թ�һ�����д�뵽outfile
grep ".*" < users > grepUsers;

:<<!
�ض������뽲�⣺
Linux��������ʱ����������ļ���
��׼�����ļ���stdin����stdin���ļ�������Ϊ0��UNIX����Ĭ�ϴ�stdin��ȡ����
��׼����ļ���stdout����stdout���ļ�������Ϊ1��Unix����Ĭ����stdout�������
��׼�����ļ���stderr����stderr���ļ�������Ϊ2��Unix�������stderr����д�������Ϣ
Ĭ������£�command > file��stdout�ض���file��command < file ��stdin�ض���file
!

#��stderr�ض���file command 2 > file
:<<!
����error.sh�ļ���
#/bin/sh

echo "About to try to access a file that doesn't exist";
cat bad-filename.txt;
!

#ִ������Ὣ������Ϣ���뵽file�ļ��� �����About to try to access a file that doesn't exist
sh error.sh 2 > file;

#��stderr׷�ӵ�fileĩβ command 2 >> file �����About to try to access a file that doesn't exist\nAbout to try to access a file that doesn't exist
sh error.sh 2 >> file;

#��stdout��stderr�ϲ����ض���file��command > file 2 >&1����command >> file 2>&1
sh error.sh > file 2>&1

#���ϣ����stdin��stdout���ض���command < file1 > file2

:<<!
ִ�����
find /etc -names "*.txt" > file 2>&1
��������ִ�У�ִ�е�>file����ʱ��stdoutΪfile����ִ�е�2>&1ʱ����ʾstderr�ض���stdout������Ҳ����file�ļ�
��Ϊ�������Ǵ����(-namesӦ����-name)������Ҫ������ն���Ļ�Ĵ�����Ϣ��find: unknown predicate `-names'
���ض�����stdoutҲ����file�ļ��У�������Ļ������ִ�����Ϣ�����Ǵ�ӡ����file�ļ���
cat file�Ϳ��Կ���find: unknown predicate `-names'��������
!



:<<!
Here Document����shell�е�һ��������ض���ʽ�������������ض���һ������ʽShell�ű������
������ʽ��
command << delimiter
	decument
delimiter
���������ǽ�����delimiter֮�������(decument)��Ϊ���봫�ݸ�command

ע�⣺
	��β��delimiterһ��Ҫ����д��ǰ�治�����κ��ַ�������Ҳ�������κ��ַ��������ո��tab����
	��ʼ��delimiterǰ��Ŀո�ᱻ���Ե�
!

#ʾ�� ����������ͨ��wc -l �������Here Document��������
wc -l << INPUT
	��ӭ����
	����̳�
	www.runoob.com
INPUT

#���Խ�Here Document����Shell�ű���
cat << EOF
	��ӭ����
	����̳�
	www.runoob.com
EOF

:<<!
�����ű���������
	��ӭ����
	����̳�
	www.runoob.com
!
