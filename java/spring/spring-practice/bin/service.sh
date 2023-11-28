#!/usr/bin/env bash

#
# +========================================================================+
# | Author: Silence H_VK                                                   |
# | Since: 2021-12-02                                                      |
# +------------------------------------------------------------------------+
#

SERVICE_NAME="spring-practice"
MAIN_CLASS=me.hvkcoder.spring.practice.PracticeApplication
BASE_DIR=$(dirname $0)

usage() {
	echo "USAGE: $0 [-daemon] [-name servicename] [-loggc] [start|stop|restart]"
	exit 1
}

if [ $# -lt 1 ]; then
	usage
fi

# Verity java env
if [ "x$JAVA_HOME" != "x" ]; then
	$JAVA_HOME/bin/java -version >/dev/null 2>&1
else
	java -version >/dev/null 2>&1
fi
if [ $? -ne 0 ]; then
	echo "+========================================================================+"
	echo "| Error: Java Environment is not available, Please check your JAVA_HOME  |"
	echo "+------------------------------------------------------------------------+"
	exit 1
fi

# Which java to use
if [ -z "$JAVA_HOME" ]; then
	JAVA="java"
	JPS="jps"
else
	JAVA="$JAVA_HOME/bin/java"
	JPS="$JAVA_HOME/bin/jps"
fi

# Log directory to use
if [ "x$LOG_DIR" = "x" ]; then
	LOG_DIR="$BASE_DIR/../logs"
fi

# Config directory to use
if [ "x$SERVICE_CONF" = "x" ]; then
	SERVICE_CONF="$BASE_DIR/../conf"
fi

# Set JAVA_OPTS
if [[ ! ${JAVA_OPTS} ]]; then
	JAVA_OPTS="-Xms1g -Xmx1g -XX:+UseCompressedOops -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8"
fi

# Set CLASS_PATH
CLASS_PATH="$BASE_DIR/../lib/*:${SERVICE_CONF}:./*"

# get process pid
get_process_pid() {
	echo $($JPS -l | grep "$MAIN_CLASS" | awk '{print $1}')
}

# log format
log() {
	current_time=$(date "+%Y-%m-%d %H:%M:%S.%3N")
	echo -e "$current_time [${1}] ${2}"
}

# Set -Daemon
while [ $# -gt 0 ]; do
	COMMAND=$1
	case $COMMAND in
	-daemon)
		DAEMON_MODE="true"
		shift
		;;
	-loggc)
		GC_LOG_ENABLE="true"
		shift
		;;
	-name)
		SERVICE_NAME=$2
		shift 2
		;;
	*)
		break
		;;
	esac
done

# Set GC Log
if [ "x$GC_LOG_ENABLE" = "xtrue" ]; then
	GC_FILE="$SERVICE_NAME-gc.log"
	JAVA_MAJOR_VERSION=$($JAVA -version 2>&1 | sed -E -n 's/.* version "([0-9]*).*$/\1/p')
	if [[ "$JAVA_MAJOR_VERSION" -ge "9" ]]; then
		JAVA_OPTS="$JAVA_OPTS -Xlog:gc*:file=$GC_FILE:time,tags:filecount=10,filesize=100M"
	else
		JAVA_OPTS="$JAVA_OPTS -Xloggc:$GC_FILE -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M -XX:+UseGCCause -XX:+TraceClassLoading -XX:+TraceBiasedLocking"
	fi
fi

progress() {
	i=0
	ch=('|' '\' '-' '/')
	while [ $i -le $(($1 * 2)) ]; do
		current_time=$(date "+%Y-%m-%d %H:%M:%S.%3N")
		printf "$current_time $2 [%s]\r" ${ch[$i % 4]}
		if [ -n "$(get_process_pid)" ] && [ "$3" = "start" ]; then
			return 0
		fi
		if [ -z "$(get_process_pid)" ] && [ "$3" = "stop" ]; then
			return 0
		fi
		let i++
		sleep 0.5
	done
	printf "\r"
	return 1
}

start() {
	if [ -n "$(get_process_pid)" ]; then
		log WARING "$SERVICE_NAME has been started in process"
		exit 0
	fi
	log INFO "$JAVA $JAVA_OPTS -cp \"$CLASS_PATH\" $MAIN_CLASS"
	if [ "x$DAEMON_MODE" = "xtrue" ]; then
		nohup "$JAVA" $JAVA_OPTS -cp "$CLASS_PATH" $MAIN_CLASS >"$SERVICE_NAME.out" 2>&1 &
		progress 20 "[INFO] Waiting $SERVICE_NAME to start complete" "start"
		if [ $? -eq 0 ]; then
			log INFO "$SERVICE_NAME start success"
			return 0
		else
			log ERROR "$SERVICE_NAME start exceeded over 20s" >&2
			return 1
		fi
	else
		exec "$JAVA" $JAVA_OPTS -cp "$CLASS_PATH" $MAIN_CLASS
	fi
}

stop() {
	pid=$(get_process_pid)
	if [ -z "$pid" ]; then
		log WARING "$SERVICE_NAME didn't start successfully, not found in the java process table"
		return 0
	fi
	log INFO "Killing $$SERVICE_NAME (pid $pid) ..."
	case "$(uname)" in
	CYCGWIN*) taskkill /PID "$pid" ;;
	*) kill -SIGTERM "$pid" ;;
	esac
	progress 20 "[INFO] Waiting $SERVICE_NAME to stop complete" "stop"
	if [ $? -eq 0 ]; then
		log INFO "$SERVICE_NAME stop success"
		return 0
	else
		log ERROR "$SERVICE_NAME stop exceeded over 20s" >&2
		return 1
	fi
}

restart() {
	stop
	if [ $? -eq 0 ]; then
		start
		exit $?
	else
		log ERROR "$SERVICE_NAME restart fail" >&2
		exit 1
	fi
}

## Set Command
case $1 in
start) start ;;
stop) stop ;;
restart) restart ;;
*) usage ;;
esac
