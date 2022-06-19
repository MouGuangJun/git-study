#!/bin/sh
#该脚本为Linux下启动java程序的通用脚本

#警告！！！：该脚本stop部分使用系统kill命令来强制终止指定的java程序进程。在杀死进程前，未作任何条件检查。在某些情况下，如程序正在进行文件或数据库的写操作
#可能会造成数据丢失或数据不完整。如果必须要考虑这类情况，则需要改写次脚本。增加在执行kill命令前的一系列检查

#JDK所在路径
JAVA_HOME="/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.262.b10-0.el7_8.x86_64/jre";

#Java程序所在的目录(classes的上一级目录)
PROJECT_PATH="/home/ccms/simple/java/project";#M
PROJECT_NAME="myproj";#M
APP_HOME="${PROJECT_PATH}/${PROJECT_NAME}";

#需要启动的Java主程序(main方法类)
if [ -z "${1}" ]
then
	echo "main class cannot be empty";
	exit;
fi;

APP_MAINCLASS="${1}";

#拼凑完整的classpath参数，包括指定lib目录下所有的jar
CLASSPATH="${APP_HOME}/classes";
for i in "${APP_HOME}/lib/"*.jar;
do
	CLASSPATH="${CLASSPATH}:${i}";
done;

#java虚拟机启动参数
JAVA_OPTS="-ms1024m -mx1024m -Xmn512m -Djava.awt.headless=true";

:<<!
[函数]判断程序是否已启动

说明：
使用JDK自带的JPS和grep命令组合，准确查找pid
JPS 加 l参数，表示显示java的完整包路径
使用awk，分割出pid($1)部分，及Java程序名称($2)
!

#初始化psid变量(全局)

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
[函数]启动程序

说明：
1.首先调用checkpid函数，刷新${psid}全局变量
2.如果程序已经启动(${psid}不等于0)，则提示程序已启动
3.如果程序没有被启动，则执行启动命令行
4.启动命令执行后，再次调用checkpid函数
5.如果步骤4的结果能够确认程序的pid，则打印OK，否则打印Failed
注意：echo -n 表示打印字符后，不换行
注意："nohup 某命令 > /dev/null 2>&1 &" 的用法
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
[函数]停止程序

说明：
1.首先调用checkpid函数，刷新${psid}全局变量
2.如果程序已经启动(${psid}不等于0)，则开始执行停止，否则，提示程序未运行
3.使用kill -9 pid命令进行强制杀死进程
4.执行kill命令紧接其后，马上查看上一句命令的返回值：$?
5.如果步骤4的结果$?等于0，则打印[OK]，否则打印[Failed]
6.为了防止java程序被启动多次，这里增加反复检查进程，反复杀死的处理(递归调用stop)
注意：echo -n 表示打印字符后，不换行
注意：在shell编程中，"$?"表示上一句命令或者一个函数的返回值
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
		echo "warn：${APP_MAINCLASS} is not running";
		echo "===============================";
	fi;
}

:<<!
[函数]检查程序运行状态
说明：
1.首先调用checkpid函数，刷新${psid}全局变量
2.如果程序已经启动(${psid}不等于0)，则提示正在运行并表示出pid
3.否则提示程序未运行
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
[函数]打印系统环境参数
!

function info(){
	echo "System Infomation";
	echo "*******************************";
	#查看操作系统版本
	echo `head -n 1 /etc/issue`;
	#查看CPU信息
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
读取脚本的第一个参数${1}，进行判断
参数的取值范围：
(start|stop|restart|status|info)
#如果参数不在指定范围之内，则打印帮助信息
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