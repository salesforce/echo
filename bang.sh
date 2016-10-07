#!/bin/sh

set -e

OPENRESTY_VERSION=1.11.2.1
OPENRESTY_PACKAGE=openresty-${OPENRESTY_VERSION}

REDIS_VERSION=stable
REDIS_PACKAGE=redis-${REDIS_VERSION}

PATH_ECHO=~/echo
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
  pushd .
  cd ${PATH_ARCHIVE}
  tar xfz ${OPENRESTY_PACKAGE}.tar.gz 
  cd ${OPENRESTY_PACKAGE}
  if [[ `uname` == 'Darwin' ]]; then
    ./configure --prefix=${PATH_OPENRESTY} \
             --with-cc-opt="-I /usr/local/include" \
             --with-ld-opt="-L /usr/local/lib"
  elif [[ `uname` == 'linux' ]]; then
    ./configure --prefix=${PATH_OPENRESTY} 
  fi
  make
  make install
  popd
  echo "*** openresty build done."
}

do_build_redis() {
  echo "*** building redis..."
  pushd .
  cd ${PATH_ARCHIVE}
  tar xzf ${REDIS_PACKAGE}.tar.gz
  cd ${REDIS_PACKAGE}
  make
  popd
  echo "*** redis build done."
}

do_copy_configs() {
  cp -r conf ${PATH_ECHO}
}

do_copy_archive() {
  cp -r archive ${PATH_ARCHIVE}
}

do_compile() {
  echo "*** fetch/build..."
  pushd .
  do_clean
  do_copy_archive
  do_build_openresty
  do_build_redis
  popd 
  do_copy_configs
  echo "*** done."
}

do_clean() {
  echo "*** cleaning up..."
  rm -rfv ${PATH_ARCHIVE} ${PATH_ECHO_BIN} ${PATH_CONFIG}
  echo "*** done."
}

do_run() {
  ${PATH_REDIS_BIN} ${PATH_REDIS_CONFIG}
  ${PATH_NGINX_BIN} -c ${PATH_NGINX_CONFIG}
}

mkdir -p ${PATH_ECHO}
case "$1" in
  clean)
    do_clean
    ;;
  compile)
    do_compile
    ;;
  copy_archive)
    do_copy_archive
    ;;
  copy_config)
    do_copy_configs
    ;;
  run)
    do_run
    ;;
esac
