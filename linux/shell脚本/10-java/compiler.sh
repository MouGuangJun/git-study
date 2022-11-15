#!/bin/sh
JAVA_HOME="/apphome/IBM/WebSphere/AppServer/java/bin"

APP_HOME="/home/ccms/simple/java/project/myproj";#M
SOURCE_DIR="src";#M

function do_compile(){
	sources_file="${APP_HOME}/sources.list";
	sources_lib="${APP_HOME}/lib";
	sources_class="${APP_HOME}/classes";
	log_dir="${APP_HOME}/log";
	if [ ! -e ${log_dir} ] 
	then
		mkdir -p ${log_dir};
	fi;
	
	if [ -e ${sources_file} ]
	then
		cat /dev/null > ${sources_file};
	else
		touch ${sources_file};
	fi;
	
	SOURCE_PATH="";
	for i in ${SOURCE_DIR}
	do
		SOURCE_PATH="${SOURCE_PATH}:${APP_HOME}/${i}";
		find "${APP_HOME}/${i}" -name "*.java" >> ${sources_file};
	done;
	
	if [ -e ${sources_class} ]
	then
		rm -rf ${sources_class};
	fi;
	
	mkdir -p ${sources_class};
	
	CLASS_PATH=".:${CLASSPATH}";
	for i in "${sources_lib}/"*.jar
	do
		CLASS_PATH="${CLASS_PATH}:${i}";
	done;	
	
	JAVA_CMD="nohup ${JAVA_HOME}/javac -d ${sources_class} -encoding utf-8 -classpath ${CLASS_PATH} @${sources_file} > ${log_dir}/cnohup.log 2>&1 &";
	eval ${JAVA_CMD};		
}

do_compile;
