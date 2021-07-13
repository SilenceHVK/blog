FROM alpine:latest
LABEL maintainer="Silence H_VK <hvkcoder@gmail>"

ARG KAFKA_VERSION=2.8.0 
ARG SCALA_VERSION=2.13    

ENV KAFKA_HOME="/opt/kafka" \
    TAR="kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz"


RUN set -eux && \
    apk add --no-cache bash perl openjdk8-jre-base tar wget && cd \opt &&\
    wget -t 10 --retry-connrefused -O "$TAR" "https://downloads.apache.org/kafka/${KAFKA_VERSION}/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz" && \
    tar zxf "kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz" && \
    rm -rf "kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz" && \
    ln -sv "kafka_${SCALA_VERSION}-${KAFKA_VERSION}" kafka && \
    apk del tar wget 

WORKDIR ${KAFKA_HOME} 

CMD ["/bin/bash"]
