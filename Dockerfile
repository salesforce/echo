FROM openresty/openresty:latest-trusty

LABEL version="1.0"

MAINTAINER Paymon Teyer <pteyer@salesforce.com>

ENV DEBIAN_FRONTEND noninteractive

ADD env/echo-proxy /etc/init.d/echo-proxy
ADD conf/nginx.conf /usr/local/openresty/nginx/conf/nginx.conf

RUN apt-get update

RUN apt-get install redis-server -y
ADD conf/redis.conf /etc/redis/redis.conf

RUN apt-get install monit -y
ADD env/monitrc /etc/monit/monitrc
RUN chmod -R 700 /etc/monit/monitrc

CMD service monit start

