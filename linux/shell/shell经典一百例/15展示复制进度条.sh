#!/bin/sh

#����ctrl + c�������ɱ
trap "pkill -f `basename $0`" SIGINT;
echo $BASHPID;

function jindu(){
	while true
	do
		echo -n '#';
		sleep 0.2;
	done;
}

jindu & cp -a ${1} ${2};

#������ɱ�Ĳ���
PID=$!;
if [ 0 -ne ${PID}  ]
then
	kill ${PID};
fi;

PRO_NAME=`basename $0`;

if [ -n "${PRO_NAME}" ]
then
	pkill -f "${PRO_NAME}";
fi;

echo "�������";