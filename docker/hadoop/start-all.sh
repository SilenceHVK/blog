#!/bin/bash

/etc/init.d/ssh start

if [ ! -d "$HADOOP_DATA/dfs" ]
then
  $HADOOP_HOME/bin/hdfs namenode -format
fi

$HADOOP_HOME/sbin/start-dfs.sh
$HADOOP_HOME/sbin/start-yarn.sh
$HADOOP_HOME/sbin/mr-jobhistory-daemon.sh start historyserver

tail -f /dev/null
