#!/bin/sh
#��д�ű���ʵ���˻�<ʯͷ,����,��>��Ϸ
game=(ʯͷ ���� ��);
num=$[RANDOM%3];
computer=${game[${num}]};
echo "${computer}";
#ͨ���������ȡ������ĳ�ȭ
#��ȭ�Ŀ����Ա�����һ�������У�game[0],game[1],game[2]�ֱ������ֲ�ͬ�Ŀ���
echo -e "�����������ʾѡ�����ĳ�ȭ���ƣ�\r\n1.ʯͷ\r\n2.����\r\n3.��";

read -p "��ѡ�� 1 - 3��" person;
case ${person} in 
1)
	if test $[num] -eq 0
	then
		echo "ƽ��";
	elif test $[num] -eq 1
	then
		echo "��Ӯ";
	else
		echo "�����Ӯ";
	fi;
	;;
2)
	if test $[num] -eq 1
	then
		echo "ƽ��";
	elif test $[num] -eq 2
	then
		echo "��Ӯ";
	else
		echo "�����Ӯ";
	fi;
	;;
3)
	if test $[num] -eq 2
	then
		echo "ƽ��";
	elif $[num] -eq 0
	then
		echo "��Ӯ";
	else
		echo "�����Ӯ";
	fi;
	;;
*)
	echo "��������1 - 3������";
esac;
