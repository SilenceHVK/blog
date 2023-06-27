FROM alpine:3.14
LABEL maintainer="Silence H_VK <hvkcoder@gmail>"

ARG NAME=thrift
ARG VERSION=0.18.1

ENV DIR="${NAME}-${VERSION}" \
    TAR="${NAME}-${VERSION}.tar.gz"

WORKDIR /opt

RUN set -eux && \
    sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories && \
    apk update && \
    apk add --no-cache openjdk11-jre-headless  g++ && \
    apk add --no-cache --virtual .build-deps bash perl curl make && \
    mkdir ${DIR} && \
    curl -L -O "https://mirrors.tuna.tsinghua.edu.cn/apache/${NAME}/${VERSION}/${TAR}" && \
    tar -zxvf ${TAR} -C ${DIR} --strip-components 1 && \
		cd ${DIR} && \
    ./configure \
        --without-cpp \
        --without-qt4 \
        --without-qt5 \
        --without-c_glib \
        --without-csharp \
        --without-erlang \
        --without-nodejs \
        --without-lua \
        --without-python \
        --without-perl \
        --without-php \
        --without-php_extension \
        --without-ruby \
        --without-haskell \
        --without-go \
        --without-haxe \
        --without-d && \
    make -j $(getconf _NPROCESSORS_ONLN) && \
    make install && \
		rm -rf ${TAR} && \
		rm -rf /opt/${DIR} && \
		rm -rf /var/cache/apk/* && \
		apk del --purge .build-deps

CMD ["/usr/local/bin/thrift"]