#!/bin/sh
#ʹ��readonly������Խ���������Ϊֻ��������ֻ��������ֵ���ܱ��ı�
myUrl="https://www.google.com"
readonly myUrl
#myUrl="https://www.runoob.com"[ִ�и����ᱨ�� => myUrl: readonly variable]

#ʹ��unset�������ɾ���������﷨��unset variable_name[����]
yourUrl="https://www.baidu.com"
unset yourUrl
echo ${yourUrl}
#ִ���������䲻�����κε����