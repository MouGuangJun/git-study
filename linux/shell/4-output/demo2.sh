#!/bin/sh
:<<COMMENT
printf�����﷨��
	printf format-string [arguments...]
format-string��Ϊ��ʽ�����ַ���
arguments��Ϊ�����б�

ע�⣺Ĭ�ϵ�printf�����Զ���ӻ��з������ǿ����ֶ����\n
COMMENT

#ʾ��
printf "Hello, Shell��\n";

#ʾ����printfռλ�����д�ӡ
:<<COMMENT
%s %c %d %f ���Ǹ�ʽ�滻����%s���һ���ַ�����%d���������%c���һ���ַ���%f���ʵ������С������ʽ���
%-10sָһ�����Ϊ10���ַ���-��ʾ����룬û�����ʾ�Ҷ��룩�����Կո���䣬����Ҳ�Ὣ���е�������ʾ����
%-4.2fָ��ʽ��ΪС��������.2��ʾ������λС��
COMMENT
printf "%-10s %-8s %-4s\n" ���� �Ա� ����kg;
printf "%-10s %-8s %-4.2f\n" ���� �� 66.1234;
printf "%-10s %-8s %-4.2f\n" ��� �� 48.6543;
printf "%-10s %-8s %-4.2f\n" ��ܽ Ů 47.9876;

#format-string Ϊ˫����
printf "%d %s\n" 1 "abc";

#�����ź�˫����һ��
printf '%d %s\n' 1 "abc";

#û������Ҳ�������
printf %s\\n abcdef;

#��ʽָֻ����һ��������������Ĳ�����Ȼ�ᰴ�ոø�ʽ�����format-string������
printf %s\\n abc def;

printf "%s %s %s\n" a b c d e f g h i j

#���û��arguments����ô%s��null���棬%d��0����
printf "%s and %d\n";

:<<COMMENT
printf��ת�����У�
\a�������ַ���ͨ��ΪASCII��BEL�ַ�
\b������
\c�����ƣ�����ʾ�����������κν�β�Ļ����ַ��� ��ֻ��%b��ʽָʾ�������µĲ����ַ�������Ч��
\f����ҳ
\n������
\r���س�
\t��ˮƽ�Ʊ��
\v����ֱ�Ʊ��
\\��һ�������ϵķ�б���ַ�
\ddd����ʾ1��3λ���˽��Ƶ��ַ������ٸ�ʽ�ַ�������Ч
\0ddd����ʾ1��3λ�İ˽����ַ�
COMMENT

#�����a string, no processing:<A\nB>
printf "a string, no processing:<%s>\n" "A\nB";
#�����a string, no processing:<A
#B>
printf "a string, no processing:<%b>\n" "A\nB";

printf "www.runnoob.com \a";