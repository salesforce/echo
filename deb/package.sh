#!/bin/bash

rm -rf echo_0.1-1/etc/ echo_0.1-1/usr/ 
mkdir -p echo_0.1-1/etc/
cp ../conf/redis.conf echo_0.1-1/etc/redis.conf
mkdir -p echo_0.1-1/etc/init.d/
cp ../scripts/echo-proxy echo_0.1-1/etc/init.d/echo-proxy
mkdir -p echo_0.1-1/usr/local/openresty/nginx/conf/
cp ../conf/nginx.conf echo_0.1-1/usr/local/openresty/nginx/conf/nginx.conf

dpkg-deb --build echo_*
