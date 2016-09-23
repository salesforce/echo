#!/bin/sh

set -e

OPENRESTY_VERSION=1.11.2.1
OPENRESTY_PACKAGE=openresty-${OPENRESTY_VERSION}

REDIS_VERSION=stable
REDIS_PACKAGE=redis-${REDIS_VERSION}

PATH_TMP=/tmp/.echo

PATH_NGINX_CONFIG=conf/nginx.conf

do_build_openresty() {
  echo "*** building openresty..."
  pushd .
  rm -rf ${PATH_TMP}
  mkdir -p ${PATH_TMP}
  cd ${PATH_TMP}
  rm -rf ${OPENRESTY_PACKAGE}
  wget "https://openresty.org/download/${OPENRESTY_PACKAGE}.tar.gz"
  tar xfzv ${OPENRESTY_PACKAGE}.tar.gz
  cd ${OPENRESTY_PACKAGE}
  if [[ `uname` == 'Darwin' ]]; then
    ./configure --prefix=${PATH_TMP} \
             --with-cc-opt="-I /usr/local/include" \
             --with-ld-opt="-L /usr/local/lib"
  elif [[ `uname` == 'linux' ]]; then
    ./configure --prefix=${PATH_TMP} 
  fi
  make
  popd
  echo "*** openresty build done."
}

do_build_redis() {
  echo "*** building redis..."
  pushd .
  mkdir -p ${PATH_TMP}
  cd ${PATH_TMP}
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

do_clean() {
  rm -rf ${PATH_TMP}
}

do_run() {
  sudo ${PATH_OPENRESTY}/build/nginx-1.11.2/objs/nginx -c ${PATH_NGINX_CONFIG}
}

case "$1" in
  clean)
    do_clean
    ;;
  compile)
    do_compile
    ;;
  run)
    do_run
    ;;
esac
