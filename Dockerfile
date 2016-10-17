FROM openresty/openresty:latest-trusty

LABEL version="1.0"

MAINTAINER Paymon Teyer <pteyer@salesforce.com>

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update
RUN apt-get install redis-server -y
RUN apt-get install monit -y

ADD conf/redis.conf /etc/redis/redis.conf
RUN chmod 0664 /etc/redis/redis.conf

ADD conf/nginx.conf /usr/local/openresty/nginx/conf/nginx.conf
RUN chmod 0664 /usr/local/openresty/nginx/conf/nginx.conf

ADD env/monitrc /etc/monit/monitrc
RUN chmod -R 700 /etc/monit/monitrc

ENTRYPOINT [ "service", "monit", "start" ]

