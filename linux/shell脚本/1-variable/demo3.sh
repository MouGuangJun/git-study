#!/bin/sh
#ʹ��һ��������ı�����ֻҪ�ڱ�����ǰ�����Ԫ���ż��ɣ��磺
your_name="qinxj"
echo $your_name
echo ${your_name}

#����ʹ��${������}���е��ã������Ͳ�����ֱ�����ʹ�ô���������������ʾ��
for skill in Ada Coffe Action Java;
do
#�����ʹ��${skill}���ã��ͻ��skillScript����һ���������ﲻ������Ԥ�ڵ�Ч��
echo "I am good at ${skill}Script"
done

#�Ѿ�����ı����������¶��壬������ʾ��
my_name="tom"
echo ${my_name}
#��ֵ��ʱ����ʹ��$���ţ�ֻ����ʹ�õ�ʱ���ʹ��$���Ž��е���
my_name="alibaba"
echo ${my_name}