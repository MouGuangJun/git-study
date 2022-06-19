#!/bin/sh
:<<!
Shell数组：
bash支持一维数组(不支持多维数组)，并且没有限定数组的大小
类似于C语言，数组元素的下标由0开始编号。获取数组中的元素需要利用下标，下标可以是整数或者算术表达式，其值应该大于或者等于0
!
:<<!
在shell中，用括号表示数组，数组元素用"空格分开"，定义数组的一般形式为：
数组名=(值1 值2 ... 值n)
例如：
array_name=(value0 value1 value2 value3)
或者
array_name=(
value0
value1
value2
value3
)
!
arr=(1 3 5 7)


:<<!
还可以单独定义数组的各个分量
array_name[0]=value0
array_name[1]=value1
array_name[2]=value2
可以不使用连续的下标，而且下标的范围没有限制
!
arr1[0]=2
arr1[2]=4
arr1[3]=6

#读取数组：读取数组的一般格式为：${数组名[下标]}
#例如：valuen=${array_name[n]}
echo ${arr1[3]}

#使用@符号可以获取数组中的所有元素，例如：echo ${array_name[@]}
echo ${arr1[@]}

:<<!
获取数组的长度
!
#获得数组的元素个数：${#array_name[@]} 或者 ${#array_name[*]}
length=${#arr[@]}
echo ${length}

length=${#arr1[*]}
echo ${length}

#获得单个元素的长度：${#array_name[n]}
length2=${#arr1[2]}
echo ${length2}