#!/bin/sh
:<<!
��ϵ�����
��ϵ�����ֻ֧�����֣���֧���ַ����������ַ�����ֵ�����֡�
�����г������õĹ�ϵ�������
-eq��������������Ƿ���ȣ���ȷ���true
-ne��������������Ƿ���ȣ�����ȷ���true
-gt�������ߵ����Ƿ�����ұߵģ�����ǣ��򷵻�true
-lt�������ߵ����Ƿ�С���ұߵģ�����ǣ��򷵻�true
-ge�������ߵ����Ƿ���ڵ����ұߵģ�����ǣ��򷵻�true
-ge�������ߵ����Ƿ�С�ڵ����ұߵģ�����ǣ��򷵻�true
!

#ʾ��
a=10;
b=20;
#����
if [ ${a} -eq ${b} ]
then
	echo "${a} -eq ${b}��a ���� b";
else
	echo "${a} -eq ${b}��a ������ b";
fi

#������
if [ ${a} -ne ${b} ]
then
	echo "${a} -ne ${b}��a ������ b";
else
	echo "${a} -ne ${b}��a ���� b";
fi

#����
if [ ${a} -gt ${b} ]
then
	echo "${a} -gt ${b}��a ���� b";
else
	echo "${a} -gt ${b}��a ������ b";
fi

#С��
if [ ${a} -lt ${b} ]
then
	echo "${a} -lt ${b}��a С�� b";
else
	echo "${a} -lt ${b}��a ��С�� b";
fi

#���ڵ���
if [ ${a} -ge ${b} ]
then
	echo "${a} -ge ${b}��a ���ڵ��� b";
else
	echo "${a} -ge ${b}��a С�� b";
fi

#С�ڵ���
if [ ${a} -le ${b} ]
then
	echo "${a} -le ${b}��a С�ڵ��� b";
else
	echo "${a} -le ${b}��a ���� b";
fi
