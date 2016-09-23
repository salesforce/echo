# Echo - HTTP Reverse Proxy Cache

### Overview

Full specification can be found here [Echo - HTTP Reverse Proxy Cache](https://sfdc.co/echo).

Echo is an HTTP Reverse Proxy Cache that reduces request latencies and down stream resource usage by providing a HTTP request/response cache for previously executed remote HTTP calls.  Echo intercepts requests from clients, performs response lookup in the Echo cache, and if found, returns the cached response to the caller.  On a cache miss, the request is forwarded to the specified passthrough service that processes the request. Echo will cache the corresponding response and then return the response to the caller.  On subsequent requests the response contents are served directly from the cache, eliminating the call to the backend service. Cached content can be static or dynamic as Echo provides invalidation capabilities that allow backend services to notify Echo when the cached entry is stale.

Object invalidation matching is done through patterns (regular expressions) allowing bulk invalidations for a org/user/etc. to be possible.  An invalidation API over HTTP is exposed to clients that need to invalidate cached entries.

Cached entries will be stored in Redis, which is set-up in a Master-Slave configuration.  This configuration, allows for Redis to be scaled-out horizontally as more processing capacity is needed.  Also, cached entries as well as invalidations, need to occur on the master node, and the corresponding changes will be pushed by master to all Redis slaves, simplifying the invalidation logic.

There are various ways Echo could be deployed.  It could sit in front of a service and with cache lookup happening for every request to a service, or it can sit between services, to handle a subset.

For example, Echo could live on the same host as core and UI Tier, servicing only requests that go from UI tier to core.  In the future Echo could be a superpod level service, and ultimately would live on the same host or same data center as the UI Tier when it’s collocated with the country customers are in.

### How to install?

To build and install in `/opt/echo`:

```bash
$ ./bang.sh --prefix=/opt/echo
```

