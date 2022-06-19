#!/bin/sh
#使用一个定义过的变量，只要在变量名前面加美元符号即可，如：
your_name="qinxj"
echo $your_name
echo ${your_name}

#建议使用${变量名}进行调用，这样就不会出现变量名使用错误的情况，如下所示：
for skill in Ada Coffe Action Java;
do
#如果不使用${skill}调用，就会把skillScript当作一个变量，达不到我们预期的效果
echo "I am good at ${skill}Script"
done

#已经定义的变量可以重新定义，如下所示：
my_name="tom"
echo ${my_name}
#赋值的时候不能使用$符号，只有在使用的时候才使用$符号进行调用
my_name="alibaba"
echo ${my_name}