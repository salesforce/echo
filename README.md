# Echo - Invalidatable HTTP Reverse Proxy Cache

### Overview

Echo is a PoC of an Invalidatable HTTP Reverse Proxy Cache; it is implemented as a simple Lua script on top of Nginx and Redis.

Similar to other reverse proxy caches, Nginx intercepts requests from clients, performs response lookup in the cache (i.e., Redis), 
and if found returns the cached response to the caller; on a cache miss the request is forwarded to the specified upstream service 
that processes the request and returns a response which is subsequently forwarded back to the client, and also stored in the cache 
(if cachable). 

Echo adds an additional step here: the response from the upstream service can potentially contain one or more Echo-specific header 
parameters; these parameters are only understandable by Echo, and are meant to allow response objects to directly interact with Nginx; 
making it possible for server side applications to execute custom operations on the HTTP caching layer.

For example, response header parameters can be used to instruct Nginx to invalidate and remove cached objects matching a regular
expression from the cache store. They can also be used to construct an object dependency graph, effectively allowing services to 
invalidate all cached responses that are dependent on a certain entity/resource.

### How to use?

Clone repository and build docker:
```bash
$ git clone git@github.com:salesforce/echo.git
$ cd echo/
$ sudo docker build -t sfdc/echo -f Dockerfile .
```

To start Echo and expose port 80 for HTTP proxy, 2812 for [monitoring](http://127.0.0.1:2812/):
```bash
$ sudo docker run -d --name echo --publish=80:80 --publish=2812:2812 sfdc/echo
```

A simple Java example and the sample run results can be found [here](https://github.com/salesforce/echo/tree/master/example). 

To attach to the docker and run shell or tail access/error logs:
```bash
$ sudo docker exec -it echo /bin/bash
$ sudo docker exec -it echo tail -f /var/log/nginx-access.log
$ sudo docker exec -it echo tail -f /var/log/nginx-error.log
```

To stop/kill/restart/remove Echo's docker:
```bash
$ sudo docker stop echo
$ sudo docker kill echo
$ sudo docker restart echo
$ sudo docker rm echo
```

To use Echo, add `Host` header parameter pointing at the upstream hostname; e.g.,
```bash
$ curl -i -v -H 'Host: www.vim.org' 'http://127.0.0.1/'
> GET / HTTP/1.1
> User-Agent: curl/7.35.0
> Accept: */*
> Host: www.vim.org
>
HTTP/1.1 200 OK
* Server openresty/1.11.2.1 is not blacklisted
Server: openresty/1.11.2.1
Date: Mon, 17 Oct 2016 20:46:52 GMT
Content-Type: text/html
Transfer-Encoding: chunked
Connection: keep-alive
Vary: Host
Cache-Control: max-age=172800
Expires: Wed, 19 Oct 2016 20:46:52 GMT
Echo-Cache-Status: MISS
...
```

The response from the above call should contain header parameter `Echo-Cache-Status` with value `HIT` or `MISS`.

#### Naming

By default, objects are cached in Redis with this key pattern: `object:escape_uri(${RequestURI})`; for exmaple, the following call:
```bash
$ curl -H 'Host: gs0.salesforce.com' 'http://127.0.0.1/home/home.jsp'
```
results in the following object in Redis:
```bash
$ redis-cli keys '*'
1) "object:gs0.salesforce.com%2Fhome%2Fhome.jsp"
```

However, it is possible to overwrite the _key pattern_ used to store cached objects in Redis via the header 
parameter `Echo-Key`; for exmaple, adding `Echo-Key: gs0/home/home.jsp` to response header results in the above 
_curl_ call to be cached in Redis like this:
```bash
$ redis-cli keys '*'
1) "object:gs0/home/home.jsp"
```

#### Object expiry

By default, objects in cache are expired respectful of Cache-Control header.  However, it is possible to overwrite 
the cache expiry via the header parameter `Echo-Expire`; for example, adding `Echo-Expire: 30` to response header 
results in the cached objects to expire (deleted from Redis) after 30 seconds.

#### Invalidation

It is possible for backend services to explicitly invalidate cached objects matching a pattern.  This can be achieved 
by adding `Echo-Invalidate` header parameter to response.  For example, adding `Echo-Invalidate: *` results in all cached 
objects to be removed from Echo (Redis); or adding `Echo-Invalidate: gs0/home/*` results in all keys prefixed with `gs0/home/` 
to be removed.

#### Observables

Echo provides functionality required to construct and invalidate _object dependency graphs_.  The dependency graph is 
constructed by adding a header parameter to reponse: `Echo-Observe`; for example, adding `Echo-Observe: /sfdc/casp/foo/bar` 
results in constructing an observable object graph in Echo called `/sfdc/casp/foo/bar`.  It is then possible for another 
response to invalidate the observers by adding the header parameter: `Echo-Notify`; 
for example: `Echo-Notify: /sfdc/casp/foo/bar` results in invalidation of all keys observing the given name.

