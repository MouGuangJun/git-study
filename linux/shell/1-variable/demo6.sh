#!/bin/sh
:<<!
Shell���飺
bash֧��һά����(��֧�ֶ�ά����)������û���޶�����Ĵ�С
������C���ԣ�����Ԫ�ص��±���0��ʼ��š���ȡ�����е�Ԫ����Ҫ�����±꣬�±���������������������ʽ����ֵӦ�ô��ڻ��ߵ���0
!
:<<!
��shell�У������ű�ʾ���飬����Ԫ����"�ո�ֿ�"�����������һ����ʽΪ��
������=(ֵ1 ֵ2 ... ֵn)
���磺
array_name=(value0 value1 value2 value3)
����
array_name=(
value0
value1
value2
value3
)
!
arr=(1 3 5 7)


:<<!
�����Ե�����������ĸ�������
array_name[0]=value0
array_name[1]=value1
array_name[2]=value2
���Բ�ʹ���������±꣬�����±�ķ�Χû������
!
arr1[0]=2
arr1[2]=4
arr1[3]=6

#��ȡ���飺��ȡ�����һ���ʽΪ��${������[�±�]}
#���磺valuen=${array_name[n]}
echo ${arr1[3]}

#ʹ��@���ſ��Ի�ȡ�����е�����Ԫ�أ����磺echo ${array_name[@]}
echo ${arr1[@]}

:<<!
��ȡ����ĳ���
!
#��������Ԫ�ظ�����${#array_name[@]} ���� ${#array_name[*]}
length=${#arr[@]}
echo ${length}

length=${#arr1[*]}
echo ${length}

#��õ���Ԫ�صĳ��ȣ�${#array_name[n]}
length2=${#arr1[2]}
echo ${length2}