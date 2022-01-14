FROM ubuntu:latest
LABEL maintainer="hvkcoder <hvkcoder@gmail.com>"

ARG NACOS_VERSION=2.0.3

WORKDIR /opt

ENV BASE_DIR="/opt/nacos" \
		NACOS_FILE="nacos-server-${NACOS_VERSION}" \
		JAVA_HOME="/usr/lib/jvm/java-8-openjdk-arm64"

ENV CLUSTER_CONF="${BASE_DIR}/conf/cluster.conf" \
		FUNCTION_MODE="all" \
		MODE="cluster" \
    JVM_XMS="1g" \
    JVM_XMX="1g" \
    JVM_XMN="512m" \
    JVM_MS="128m" \
    JVM_MMS="320m" \
		NACOS_DEBUG="n" \
    TOMCAT_ACCESSLOG_ENABLED="false" \
    TIME_ZONE="Asia/Shanghai" \
		TAR="$NACOS_FILE.tar.gz"

RUN ln -fs /usr/share/zoneinfo/UTC /etc/localtime \
  && sed -i s@/archive.ubuntu.com/@/mirrors.aliyun.com/@g /etc/apt/sources.list \
	&& sed -i s@/security.ubuntu.com/@/mirrors.aliyun.com/@g /etc/apt/sources.list \
	&& apt-get clean \
	&& apt-get update \
	&& apt-get install -y --reinstall build-essential \
	&& apt-get install -y --no-install-recommends ssh wget tar rsync net-tools libxml2-dev libkrb5-dev libffi-dev libssl-dev python-lxml libgmp3-dev libsasl2-dev openjdk-8-jre python2.7-dev \
	&& rm -rf /var/lib/apt/lists/*\
	&& apt-get clean \
	&& if [ ! -e /usr/bin/python ]; then ln -s /usr/bin/python2.7 /usr/bin/python; fi

RUN wget https://github.com/alibaba/nacos/releases/download/${NACOS_VERSION}/${TAR} \
		&& tar -zxvf ${TAR} \
		&& rm -rf ${TAR} ${BASE_DIR}/bin/* ${BASE_DIR}/conf/*.properties ${BASE_DIR}/conf/*.example ${BASE_DIR}/conf/nacos-mysql.sql

ADD startup.sh ${BASE_DIR}/bin/startup.sh
ADD application.properties ${BASE_DIR}/conf/application.properties

EXPOSE 8848
ENTRYPOINT ["nacos/bin/startup.sh"]