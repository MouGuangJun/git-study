#!/bin/sh
:<<!
һ�������ţ�
����������κ��ַ�����ԭ��������������ַ����еı�������Ч�ģ�
�������ַ������ܳ��ֵ���һ���ĵ�����(�Ե�����ʹ��ת���ַ�Ҳ����)�����ɳɶԳ��֣���Ϊ�ַ���ƴ��ʹ��
!
str='this is a string'
echo ${str}

:<<!
����˫���ţ�
˫����������б���
˫��������Գ���ת���ַ�
!
your_name="runoob"
doublestr="Hello,I know you are \"$your_name\"! \n"
echo -e ${doublestr}

:<<!
ƴ���ַ�����������java���ַ���ƴ�ӣ���������Ҫʹ��+��
���磺
���������your_name='��Ҷ'
your_name=${your_name}"�ͻ���""�Ǻܺõ�����"", ����һ�����������磡"
echo ${your_name}
!
#ʹ��˫����ƴ��
greeting="hello,"$your_name"!"

greeting1="hello,${your_name}!" 
#hello,runoob! hello,runoob!
echo ${greeting} ${greeting1}

#ʹ�õ�����ƴ��
greeting2='hello,'$your_name'!'
#�������ַ����в���ֱ�ӵ��ñ���
greeting3='hello,${your_name}!'
#hello,runoob! hello,${your_name}!
echo ${greeting2} ${greeting3}

:<<!
�ַ������õķ���
!
#��ȡ�ַ����ĳ��ȣ��ؼ��֣�[#]
string="runoob is a great site"
#22
echo ${#string}

#��ȡ���ַ������ؼ��֣�[:]
#unoo
echo ${string:1:4}

#�����ַ����������ַ�i����o��λ�ã��ĸ���ĸ�ȳ��־ͼ����ĸ�:
#4
echo `expr index "${string}" io`