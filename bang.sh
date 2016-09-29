#!/bin/sh

set -e

OPENRESTY_VERSION=1.11.2.1
OPENRESTY_PACKAGE=openresty-${OPENRESTY_VERSION}

REDIS_VERSION=stable
REDIS_PACKAGE=redis-${REDIS_VERSION}

PATH_ECHO=~/echo
PATH_INSTALL=${PATH_ECHO}/pkg
PATH_NGINX_BIN=${PATH_ECHO}/bin/openresty
PATH_REDIS=${PATH_INSTALL}/${REDIS_PACKAGE}
PATH_REDIS_BIN=${PATH_REDIS}/src/redis-server

PATH_CONFIG=${PATH_ECHO}/conf
PATH_NGINX_CONFIG=${PATH_CONFIG}/nginx.conf
PATH_REDIS_CONFIG=${PATH_CONFIG}/redis.conf

do_build_openresty() {
  echo "*** building openresty..."
  pushd .
  rm -rf ${PATH_INSTALL}
  mkdir -p ${PATH_INSTALL}
  cd ${PATH_INSTALL}
  rm -rf ${OPENRESTY_PACKAGE}
  wget "https://openresty.org/download/${OPENRESTY_PACKAGE}.tar.gz"
  tar xfzv ${OPENRESTY_PACKAGE}.tar.gz
  cd ${OPENRESTY_PACKAGE}
  if [[ `uname` == 'Darwin' ]]; then
    ./configure --prefix=${PATH_ECHO} \
             --with-cc-opt="-I /usr/local/include" \
             --with-ld-opt="-L /usr/local/lib"
  elif [[ `uname` == 'linux' ]]; then
    ./configure --prefix=${PATH_ECHO} 
  fi
  make
  make install
  popd
  echo "*** openresty build done."
}

do_build_redis() {
  echo "*** building redis..."
  pushd .
  mkdir -p ${PATH_INSTALL}
  cd ${PATH_INSTALL}
  wget "http://download.redis.io/${REDIS_PACKAGE}.tar.gz"
  tar xvzf ${REDIS_PACKAGE}.tar.gz
  cd ${REDIS_PACKAGE}
  make
  popd
  echo "*** redis build done."
}

do_copy_configs() {
  cp -r conf ${PATH_ECHO}
}

do_compile() {
  echo "*** fetch/build..."
  pushd .
  do_build_openresty
  do_build_redis
  popd 
  do_copy_configs
  echo "*** done."
}

do_clean() {
  echo "*** cleaning up..."
  rm -rfv ${PATH_INSTALL} ${PATH_ECHO_BIN} ${PATH_CONFIG}
  echo "*** done."
}

do_run() {
  ${PATH_REDIS_BIN} ${PATH_REDIS_CONFIG}
  ${PATH_REDIS_BIN} ${PATH_REDIS_SLAVE_1_CONFIG}
  ${PATH_REDIS_BIN} ${PATH_REDIS_SLAVE_2_CONFIG}
  ${PATH_NGINX_BIN} -c ${PATH_NGINX_CONFIG}
}

case "$1" in
  clean)
    do_clean
    ;;
  compile)
    do_compile
    ;;
  copy)
    do_copy_configs
    ;;
  run)
    do_run
    ;;
esac
