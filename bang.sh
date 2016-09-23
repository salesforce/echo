#!/bin/sh

set -e

PATH_OPENRESTY=src/openresty-1.11.2.1
PATH_NGINX_CONFIG=conf/nginx.conf

do_compile() {
	pushd .
	echo "*** running configure $@..."
	cd ${PATH_OPENRESTY}
	./configure --with-cc-opt="-I/usr/local/include -I/usr/local/opt/openssl/include -I/usr/local/Cellar/pcre/8.39/include/ -I/usr/local/Cellar/openssl/1.0.2g/include/" --with-ld-opt="-L/usr/local/lib -L/usr/local/Cellar/pcre/8.39/lib -L/usr/local/Cellar/openssl/1.0.2g/lib"
	echo "*** running make..."
	make
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
