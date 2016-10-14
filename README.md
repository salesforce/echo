# Echo - HTTP Reverse Proxy Cache

### Overview

Full specification can be found here [Echo - HTTP Reverse Proxy Cache](https://sfdc.co/echo).

Echo is an HTTP Reverse Proxy Cache that reduces request latencies and down stream resource usage by providing a HTTP request/response cache for previously executed remote HTTP calls.  Echo intercepts requests from clients, performs response lookup in the Echo cache, and if found, returns the cached response to the caller.  On a cache miss, the request is forwarded to the specified passthrough service that processes the request. Echo will cache the corresponding response and then return the response to the caller.  On subsequent requests the response contents are served directly from the cache, eliminating the call to the backend service. Cached content can be static or dynamic as Echo provides invalidation capabilities that allow backend services to notify Echo when the cached entry is stale.

Object invalidation matching is done through patterns (regular expressions) allowing bulk invalidations for a org/user/etc. to be possible.  An invalidation API over HTTP is exposed to clients that need to invalidate cached entries.

Cached entries will be stored in Redis, which is set-up in a Master-Slave configuration.  This configuration, allows for Redis to be scaled-out horizontally as more processing capacity is needed.  Also, cached entries as well as invalidations, need to occur on the master node, and the corresponding changes will be pushed by master to all Redis slaves, simplifying the invalidation logic.

There are various ways Echo could be deployed.  It could sit in front of a service and with cache lookup happening for every request to a service, or it can sit between services, to handle a subset.

For example, Echo could live on the same host as core and UI Tier, servicing only requests that go from UI tier to core.  In the future Echo could be a superpod level service, and ultimately would live on the same host or same data center as the UI Tier when it’s collocated with the country customers are in.

### How to use?

Download and install Debian package:

```bash
$ git clone git@git.soma.salesforce.com:CASP/echo.git
$ cd echo/dist
$ dpkg -i echo_0.1-1.deb
```

To start Echo:

```bash
$ service echo-cache start
$ service echo-proxy start
```

To stop Echo:

```bash
$ service echo-cache stop
$ service echo-proxy stop
```

To uninstall Echo:

```bash
$ apt-get remove echo -y
```

To use Echo, add `Host` header parameter pointing at the upstream hostname; e.g.,

```bash
$ curl -i -v -H 'Host: www.vim.org' 'http://127.0.0.1/'
```

The response from the above call should contain header parameter `Echo-Cache-Status` with value `HIT` or `MISS`.

#### Key naming

By default, objects are cached in Redis with this key pattern: `object:escape_uri(${RequestURI})`; for exmaple, the following call:

```bash
$ curl -H 'Host: gs0.salesforce.com' 'http://127.0.0.1/home/home.jsp'
```

results in the followin object in Redis:

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

By default, objects in cache are expired respectful of Cache-Control header.  However, it is possible to overwrite the cache expiry via the header parameter `Echo-Expire`; for example, adding `Echo-Expire: 30` to response header results in the cached object to expire (deleted from Redis) after 30 seconds.

#### Explicit invalidation

It is possible for backend services to explicitly invalidate cached objects matching a pattern.  This can be achieved by adding `Echo-Invalidate` header parameter to response.  For example, adding `Echo-Invalidate: *` results in all cached objects to be removed from Echo (Redis); or adding `Echo-Invalidate: gs0/home/*` results in all keys prefixed with `gs0/home/` to be removed.

#### Observables

Echo provides functionality required to construct and invalidate _object dependency graphs_.  The dependency graph is constructed by adding a header parameter to reponse: `Echo-Observe`; for example, adding `Echo-Observe: /sfdc/casp/foo/bar` results in constructing an observable object graph in Echo called `/sfdc/casp/foo/bar`.  It is then possible for another response to invalidate the observers by adding the header parameter: `Echo-Notify`; for example: `Echo-Notify: /sfdc/casp/foo/bar` results in invalidation of all keys observing the given name.

### Development
OpenSSL and PCRE libraries are required.

Mac OS:
```bash
$ brew install openssl pcre
```

Debian/Ubuntu:
```bash
$ apt-get install libssl-dev libpcre3-dev
```

To build (compile and install OpenResty + Redis):
```bash
$ ./bang.sh compile
```

To run:
```bash
$ ./bang.sh run
```
