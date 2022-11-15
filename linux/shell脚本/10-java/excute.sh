#!/bin/sh
#�ýű�ΪLinux������java�����ͨ�ýű�

#���棡�������ýű�stop����ʹ��ϵͳkill������ǿ����ָֹ����java������̡���ɱ������ǰ��δ���κ�������顣��ĳЩ����£���������ڽ����ļ������ݿ��д����
#���ܻ�������ݶ�ʧ�����ݲ��������������Ҫ�����������������Ҫ��д�νű���������ִ��kill����ǰ��һϵ�м��

#JDK����·��
JAVA_HOME="/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.262.b10-0.el7_8.x86_64/jre";

#Java�������ڵ�Ŀ¼(classes����һ��Ŀ¼)
PROJECT_PATH="/home/ccms/simple/java/project";#M
PROJECT_NAME="myproj";#M
APP_HOME="${PROJECT_PATH}/${PROJECT_NAME}";

#��Ҫ������Java������(main������)
if [ -z "${1}" ]
then
	echo "main class cannot be empty";
	exit;
fi;

APP_MAINCLASS="${1}";

#ƴ��������classpath����������ָ��libĿ¼�����е�jar
CLASSPATH="${APP_HOME}/classes";
for i in "${APP_HOME}/lib/"*.jar;
do
	CLASSPATH="${CLASSPATH}:${i}";
done;

#java�������������
JAVA_OPTS="-ms1024m -mx1024m -Xmn512m -Djava.awt.headless=true";

:<<!
[����]�жϳ����Ƿ�������

˵����
ʹ��JDK�Դ���JPS��grep������ϣ�׼ȷ����pid
JPS �� l��������ʾ��ʾjava��������·��
ʹ��awk���ָ��pid($1)���֣���Java��������($2)
!

#��ʼ��psid����(ȫ��)

psid=0;

#function checkpid() {
#	javaps=`${JAVA_HOME}/bin/jps -l | grep ${APP_MAINCLASS}`;
#	if [ -n ${javaps} ]
#	then
#		psid=`echo ${javaps} | awk 'print ${1}'`;
#	else
#		psid=0;
#	fi;
#}


function checkpid() {
	javaps=`ps -x | grep "java.*\b${APP_MAINCLASS}\b"`;
	if [ -n "${javaps}" ]
	then
		psid=`echo ${javaps} | awk '{print $1}'`;
	else
		psid=0;
	fi;
}

:<<!
[����]��������

˵����
1.���ȵ���checkpid������ˢ��${psid}ȫ�ֱ���
2.��������Ѿ�����(${psid}������0)������ʾ����������
3.�������û�б���������ִ������������
4.��������ִ�к��ٴε���checkpid����
5.�������4�Ľ���ܹ�ȷ�ϳ����pid�����ӡOK�������ӡFailed
ע�⣺echo -n ��ʾ��ӡ�ַ��󣬲�����
ע�⣺"nohup ĳ���� > /dev/null 2>&1 &" ���÷�
!
function start(){
	checkpid;
	if [ ${psid} -ne 0 ] 
	then
		echo "===============================";
		echo "warn:${APP_MAINCLASS} already started! (pid=${psid})";
		echo "===============================";
	else
		echo -n "Starting ${APP_MAINCLASS} ...";
		JAVA_CMD="nohup ${JAVA_HOME}/bin/java ${JAVA_OPTS} -classpath ${CLASSPATH} ${APP_MAINCLASS} > ${APP_HOME}/log/enohup.log 2>&1 &";
		eval ${JAVA_CMD};
		checkpid;
		if [ ${psid} -ne 0 ]
		then
			echo "(pid=${psid}) [OK]";
		else
			echo "[Failed]";
		fi;
	fi;
	
}


:<<!
[����]ֹͣ����

˵����
1.���ȵ���checkpid������ˢ��${psid}ȫ�ֱ���
2.��������Ѿ�����(${psid}������0)����ʼִ��ֹͣ��������ʾ����δ����
3.ʹ��kill -9 pid�������ǿ��ɱ������
4.ִ��kill�������������ϲ鿴��һ������ķ���ֵ��$?
5.�������4�Ľ��$?����0�����ӡ[OK]�������ӡ[Failed]
6.Ϊ�˷�ֹjava����������Σ��������ӷ��������̣�����ɱ���Ĵ���(�ݹ����stop)
ע�⣺echo -n ��ʾ��ӡ�ַ��󣬲�����
ע�⣺��shell����У�"$?"��ʾ��һ���������һ�������ķ���ֵ
!
function stop(){
	checkpid;
	if [ ${psid} -ne 0 ]
	then
		echo -n "Stopping ${APP_MAINCLASS} ...";
		kill -9 ${psid};
		if [ $? -eq 0 ]
		then
			echo "[OK]";
		else
			echo "[Failed]";
		fi;
		
		checkpid;
		if [ ${psid} -ne 0 ]
		then
			stop;
		fi;
	else
		echo "===============================";
		echo "warn��${APP_MAINCLASS} is not running";
		echo "===============================";
	fi;
}

:<<!
[����]����������״̬
˵����
1.���ȵ���checkpid������ˢ��${psid}ȫ�ֱ���
2.��������Ѿ�����(${psid}������0)������ʾ�������в���ʾ��pid
3.������ʾ����δ����
!
function status(){
	checkpid;
	if [ ${psid} -ne 0 ]
	then
		echo "${APP_MAINCLASS} is running! (pid=${psid})";
	else
		echo "${APP_MAINCLASS} is not running";
	fi;
}

:<<!
[����]��ӡϵͳ��������
!

function info(){
	echo "System Infomation";
	echo "*******************************";
	#�鿴����ϵͳ�汾
	echo `head -n 1 /etc/issue`;
	#�鿴CPU��Ϣ
	echo `uname -a`;
	echo "";
	echo "JAVA_HOME=${JAVA_HOME}";
	echo `${JAVA_HOME}/bin/java -version`;
	echo "";
	echo "APP_HOME=${APP_HOME}";
	echo "APP_MAINCLASS=${APP_MAINCLASS}";
	echo "*******************************";
}

:<<!
��ȡ�ű��ĵ�һ������${1}�������ж�
������ȡֵ��Χ��
(start|stop|restart|status|info)
#�����������ָ����Χ֮�ڣ����ӡ������Ϣ
!
case "${2}" in
	'start')
		start;
	;;
	
	'stop')
		stop;
	;;
	
	'restart')
		stop;
		start;
	;;
	
	'status')
		status;
	;;
	
	'info')
		info;
	;;
	
	*)
		echo "Usage:$0 {start|stop|restart|status|info}";
esac;