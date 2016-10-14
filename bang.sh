#!/bin/bash

set -e

OPENRESTY_VERSION=1.11.2.1
OPENRESTY_PACKAGE=openresty-${OPENRESTY_VERSION}

REDIS_VERSION=stable
REDIS_PACKAGE=redis-${REDIS_VERSION}

PATH_ECHO=/opt/echo
PATH_OPENRESTY=${PATH_ECHO}/openresty
PATH_ARCHIVE=${PATH_ECHO}/archive

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

do_copy_archive() {
  cp -r archive ${PATH_ARCHIVE}
}

mkdir -p ${PATH_ECHO}
case "$1" in
  compile)
    do_copy_archive
    do_build_openresty
    do_build_redis
    ;;
  copy_archive)
    do_copy_archive
    ;;
  *)
    echo $"Usage: $0 {compile}"
    exit 1
esac

