# Echo - Invalidatable HTTP Reverse Proxy Cache

### Overview

Full specification can be found here [Echo - HTTP Reverse Proxy Cache](https://sfdc.co/echo).

Echo is an HTTP Reverse Proxy Cache that reduces request latencies and down stream resource usage by providing a HTTP request/response cache for previously executed remote HTTP calls.  Echo intercepts requests from clients, performs response lookup in the Echo cache, and if found, returns the cached response to the caller.  On a cache miss, the request is forwarded to the specified passthrough service that processes the request.  Echo will intercept the response and depending on a set of custom header parameters, caches the corresponding response and possibly invalidates older responses.  Then it will return the response to the caller.  Note that all operations are handled asynchronously.  On subsequent requests the response contents are served directly from the cache, eliminating the call to the backend service. 

Cached content can be static or dynamic as Echo provides invalidation capabilities that allow backend services to notify Echo when the cached entry is stale.  Object invalidation matching is done through patterns (regular expressions) or observables (representing a composite object dependency graph) allowing bulk invalidations for an org/user/etc. and object graph invalidation to be possible.  

Cached entries will be stored in Redis, which can be set-up in a Master-Slave configuration.  This configuration, allows for Redis to be scaled-out horizontally as more processing capacity is needed.  Also, cached entries as well as invalidations, need to occur on the master node, and the corresponding changes will be pushed by master to all Redis slaves, simplifying the invalidation logic.

### How to use?

Clone repository and build docker:
```bash
$ git clone git@git.soma.salesforce.com:CASP/echo.git
$ cd echo/
$ sudo docker build -t sfdc/echo -f Dockerfile .
```

To start Echo and expose port 80 for HTTP proxy, 2812 for [monitoring](http://127.0.0.1:2812/), 
and 7379 for [Webdis](http://webd.is/) interface to [Redis](http://127.0.0.1:7379/INFO/):
```bash
$ sudo docker run -d --name echo --publish=80:80 --publish=2812:2812 --publish=7379:7379 sfdc/echo
```



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

However, it is possible to overwrite the _key pattern_ used to store cached objects in Redis via the header parameter `Echo-Key`; for exmaple, adding `Echo-Key: gs0/home/home.jsp` to response header results in the above _curl_ call to be cached in Redis like this:
```bash
$ redis-cli keys '*'
1) "object:gs0/home/home.jsp"
```

#### Object expiry

By default, objects in cache are expired respectful of Cache-Control header.  However, it is possible to overwrite the cache expiry via the header parameter `Echo-Expire`; for example, adding `Echo-Expire: 30` to response header results in the cached objects to expire (deleted from Redis) after 30 seconds.

#### Invalidation

It is possible for backend services to explicitly invalidate cached objects matching a pattern.  This can be achieved by adding `Echo-Invalidate` header parameter to response.  For example, adding `Echo-Invalidate: *` results in all cached objects to be removed from Echo (Redis); or adding `Echo-Invalidate: gs0/home/*` results in all keys prefixed with `gs0/home/` to be removed.

#### Observables

Echo provides functionality required to construct and invalidate _object dependency graphs_.  The dependency graph is constructed by adding a header parameter to reponse: `Echo-Observe`; for example, adding `Echo-Observe: /sfdc/casp/foo/bar` results in constructing an observable object graph in Echo called `/sfdc/casp/foo/bar`.  It is then possible for another response to invalidate the observers by adding the header parameter: `Echo-Notify`; for example: `Echo-Notify: /sfdc/casp/foo/bar` results in invalidation of all keys observing the given name.
