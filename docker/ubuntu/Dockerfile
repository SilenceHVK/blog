FROM ubuntu:latest
LABEL maintainer="hvkcoder <hvkcoder@gmail.com>"

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

CMD [ "/bin/bash" ]