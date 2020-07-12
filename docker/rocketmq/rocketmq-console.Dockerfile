FROM centos:7
LABEL maintainer="Silence H_VK <hvkcoder@gmail>"

RUN yum install -y java-1.8.0-openjdk-devel.x86_64 && yum clean all -y
WORKDIR /opt/

ADD rocketmq-console-ng-*.jar rocketmq-console-ng.jar
EXPOSE 8080

ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar rocketmq-console-ng.jar" ]
