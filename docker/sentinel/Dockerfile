FROM alpine:latest
LABEL maintainer="Silence H_VK <hvkcoder@gmail>"

ARG SENTINEL_VERSION=1.8.4

ENV SENTINEL_HOME="/opt"

RUN set -eux && \
    apk add --no-cache bash perl openjdk8-jre-base tar wget && cd \opt && \
    wget -O sentinel-dashboard.jar "https://github.com/alibaba/Sentinel/releases/download/${SENTINEL_VERSION}/sentinel-dashboard-${SENTINEL_VERSION}.jar"  && \
    apk del tar wget

WORKDIR ${SENTINEL_HOME}


EXPOSE 8080

ENTRYPOINT ["java", "-jar", "sentinel-dashboard.jar"]