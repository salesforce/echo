#!/bin/bash

set -e

OPENRESTY_VERSION=1.11.2.1
OPENRESTY_PACKAGE=openresty-${OPENRESTY_VERSION}

REDIS_VERSION=stable
REDIS_PACKAGE=redis-${REDIS_VERSION}

PATH_ECHO=/opt/echo
PATH_OPENRESTY=${PATH_ECHO}/openresty
PATH_ARCHIVE=${PATH_ECHO}/archive
PATH_NGINX_BIN=${PATH_OPENRESTY}/bin/openresty
PATH_REDIS=${PATH_ARCHIVE}/${REDIS_PACKAGE}
PATH_REDIS_BIN=${PATH_REDIS}/src/redis-server

PATH_CONFIG=${PATH_ECHO}/conf
PATH_NGINX_CONFIG=${PATH_CONFIG}/nginx/echo_nginx.conf
PATH_REDIS_CONFIG=${PATH_CONFIG}/redis/echo_redis.conf

do_build_openresty() {
  echo "*** building openresty..."
  cd ${PATH_ARCHIVE}
  tar xfz ${OPENRESTY_PACKAGE}.tar.gz 
  cd ${OPENRESTY_PACKAGE}
  ./configure --prefix=${PATH_OPENRESTY} 
  make
  make install
  echo "*** openresty build done."
}

do_build_redis() {
  echo "*** building redis..."
  cd ${PATH_ARCHIVE}
  tar xzf ${REDIS_PACKAGE}.tar.gz
  cd ${REDIS_PACKAGE}
  make
  echo "*** redis build done."
}

do_copy_configs() {
  cp -r conf ${PATH_ECHO}
}

do_copy_archive() {
  cp -r archive ${PATH_ARCHIVE}
}

do_clean() {
  echo "*** cleaning up..."
  rm -rfv ${PATH_ARCHIVE} ${PATH_ECHO_BIN} ${PATH_CONFIG}
  echo "*** done."
}

do_start_nginx() {
  echo "*** starting nginx..."
  ${PATH_NGINX_BIN} -c ${PATH_NGINX_CONFIG}
  echo "*** done."
}

do_start_redis() {
  echo "*** starting redis..."
  ${PATH_REDIS_BIN} ${PATH_REDIS_CONFIG}
  echo "*** done."
}

do_stop_nginx() {
  echo "*** stopping nginx..."
  pkill nginx
  echo "*** done."
}

do_stop_redis() {
  echo "*** stopping redis..."
  pkill redis-server
  echo "*** done."
}

mkdir -p ${PATH_ECHO}
case "$1" in
  clean)
    do_clean
    ;;
  compile)
    do_copy_archive
    do_copy_configs
    do_build_openresty
    do_build_redis
    ;;
  copy_archive)
    do_copy_archive
    ;;
  copy_config)
    do_copy_configs
    ;;
  restart_nginx)
    do_stop_nginx
    sleep 1
    do_start_nginx
    ;;
  start_nginx)
    do_start_nginx
    ;;
  restart_redis)
    do_stop_redis
    sleep 1
    do_start_redis
    ;;
  start_redis)
    do_start_redis
    ;;
  stop_redis)
    do_stop_redis
    ;;
  stop_nginx)
    do_stop_nginx
    ;;
  *)
    echo $"Usage: $0 {clean|compile|restart_nginx|restart_redis}"
    exit 1
esac

