FROM openresty/openresty:latest-trusty

LABEL version="1.0"

MAINTAINER Paymon Teyer <pteyer@salesforce.com>

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update
RUN apt-get install redis-server -y
ADD scripts/echo-proxy /etc/init.d/echo-proxy
ADD conf/redis.conf /etc/redis/redis.conf
ADD conf/nginx.conf /usr/local/openresty/nginx/conf/nginx.conf
CMD service redis-server start
CMD service echo-proxy start

