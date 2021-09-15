FROM ubuntu:latest
LABEL maintainer="hvkcoder <hvkcoder@gmail.com>"

ARG HADOOP_VERSION=3.3.0

WORKDIR /

ENV HADOOP_HOME="/opt/hadoop" \
	HADOOP_FILE="hadoop-${HADOOP_VERSION}"

ENV HADOOP_CONF="$HADOOP_HOME/etc/hadoop" \
	HADOOP_DATA="/var/hadoop" \
	HADOOP_USER="root" \
	JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64" \
	TAR="$HADOOP_FILE.tar.gz"

RUN ln -fs /usr/share/zoneinfo/UTC /etc/localtime \
	&& apt-get update \
	&& apt-get install -y --reinstall build-essential \
	&& apt-get install -y --no-install-recommends ssh wget tar rsync net-tools libxml2-dev libkrb5-dev libffi-dev libssl-dev python-lxml libgmp3-dev libsasl2-dev openjdk-8-jre python2.7-dev \
	&& rm -rf /var/lib/apt/lists/*\
	&& apt-get clean \
	&& if [ ! -e /usr/bin/python ]; then ln -s /usr/bin/python2.7 /usr/bin/python; fi

RUN mkdir -p $HADOOP_DATA \
	&& wget "https://mirrors.cnnic.cn/apache/hadoop/common/$HADOOP_FILE/$TAR" \
	&& tar -zxvf $TAR \
	&& mv $HADOOP_FILE $HADOOP_HOME \
	&& echo "export JAVA_HOME=$JAVA_HOME" >> $HADOOP_CONF/hadoop-env.sh \
	&& echo "export HDFS_DATANODE_USER=$HADOOP_USER" >> $HADOOP_CONF/hadoop-env.sh \
	&& echo "export HDFS_NAMENODE_USER=$HADOOP_USER" >> $HADOOP_CONF/hadoop-env.sh \
	&& echo "export HDFS_SECONDARYNAMENODE_USER=$HADOOP_USER" >> $HADOOP_CONF/hadoop-env.sh \
	&& echo "export YARN_RESOURCEMANAGER_USER=$HADOOP_USER" >> $HADOOP_CONF/yarn-env.sh \
	&& echo "export YARN_NODEMANAGER_USER=$HADOOP_USER" >> $HADOOP_CONF/yarn-env.sh \
	&& echo "PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin" >> ~/.bashrc \
  && echo "hadoop" > $HADOOP_CONF/workers \
	&& rm -rf $TAR \
	&& ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa \
	&& cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys \
	&& chmod 0600 ~/.ssh/authorized_keys

ADD *.xml $HADOOP_CONF/
ADD ssh_config /root/.ssh/config
ADD start-all.sh start-all.sh

EXPOSE 8088 9000 9870 9864 19888 8042 22 50090

CMD [ "bash", "start-all.sh" ]
