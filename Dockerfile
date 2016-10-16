FROM openresty/openresty:latest-trusty

LABEL version="1.0"

MAINTAINER Paymon Teyer <pteyer@salesforce.com>

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update
RUN apt-get install redis-server -y
ADD deb/echo_0.1-1/etc/init.d/echo-proxy /etc/init.d/echo-proxy
ADD deb/echo_0.1-1/etc/redis.conf /etc/redis.conf
ADD deb/echo_0.1-1/usr/local/openresty/nginx/conf/nginx.conf /usr/local/openresty/nginx/conf/nginx.conf
RUN service redis-server start
RUN service echo-proxy start

