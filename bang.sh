#!/bin/sh

set -e

OPENRESTY_VERSION=1.11.2.1
OPENRESTY_PACKAGE=openresty-${OPENRESTY_VERSION}

REDIS_VERSION=stable
REDIS_PACKAGE=redis-${REDIS_VERSION}

PATH_NGINX_CONFIG=conf/nginx.conf
do_build_openresty() {
	echo "*** building openresty..."
	pushd .
	mkdir -p /tmp/.echo/
	cd /tmp/.echo/
	rm -rf ${OPENRESTY_PACKAGE}
	wget "https://openresty.org/download/${OPENRESTY_PACKAGE}.tar.gz"
	tar xfzv ${OPENRESTY_PACKAGE}.tar.gz
	cd ${OPENRESTY_PACKAGE}
	./configure --with-cc-opt="-I/usr/local/include -I/usr/local/opt/openssl/include -I/usr/local/Cellar/pcre/8.39/include/ -I/usr/local/Cellar/openssl/1.0.2g/include/" --with-ld-opt="-L/usr/local/lib -L/usr/local/Cellar/pcre/8.39/lib -L/usr/local/Cellar/openssl/1.0.2g/lib"
	make
	popd
	echo "*** openresty build done."
}

do_build_redis() {
	echo "*** building redis..."
	pushd .
	wget "http://download.redis.io/${REDIS_PACKAGE}.tar.gz"
	tar xvzf ${REDIS_PACKAGE}.tar.gz
	cd ${REDIS_PACKAGE}
	make
	popd
	echo "*** redis build done."
}

do_compile() {
	pushd .
	do_build_openresty
	do_build_redis
	popd 
	echo "*** done."
}

do_run() {
	sudo ${PATH_OPENRESTY}/build/nginx-1.11.2/objs/nginx -c ${PATH_NGINX_CONFIG}
}

case "$1" in
	compile)
		do_compile
		;;
	run)
		do_run
		;;
esac
