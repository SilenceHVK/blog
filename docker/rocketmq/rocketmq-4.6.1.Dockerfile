FROM centos:7
LABEL maintainer="Silence H_VK <hvkcoder@gmail>"

RUN yum install -y java-1.8.0-openjdk-devel.x86_64 && yum clean all -y

ENV ROCKETMQ_VERSION 4.6.1
ENV ROCKETMQ_HOME /opt/rocketmq-${ROCKETMQ_VERSION}

WORKDIR ${ROCKETMQ_HOME}

ADD ./rocketmq-4.6.1/ .

# expose namesrv port
EXPOSE 9876

# expose broker ports
EXPOSE 10909 10911 10912

WORKDIR ${ROCKETMQ_HOME}/bin

RUN echo "export PATH=$PATH:${ROCKETMQ_HOME}/bin" >> /root/.bashrc && source /root/.bashrc

CMD [ "/bin/bash" ]
