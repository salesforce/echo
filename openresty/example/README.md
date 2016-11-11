### Sample application using Echo

To build and run:
```bash
$ mvn clean install
$ java -jar target/echo-example.jar
```

Sample run: 
  - POST to endpoint and capture returned ID
  - use the ID to GET content of previous post (cache miss)
  - GET again (cache hit)
  - PUT to the same endpoint and ID (invalidates cache)
  - GET the same (cache miss)
  - GET again (cache hit)

```bash
$ export ID=`curl -X POST -H 'Host: pteyer-ltm1.internal.salesforce.com:8786' 'http://127.0.0.1/' --data "$(uuidgen)"`
$ echo $ID
ffa74592-19be-41b0-9e1a-8802d6aabcf1
$ curl -X GET -I -H 'Host: pteyer-ltm1.internal.salesforce.com:8786' "http://127.0.0.1/$ID"
HTTP/1.1 200 OK
Server: openresty/1.11.2.1
Date: Mon, 24 Oct 2016 18:14:41 GMT
Content-Type: text/plain
Content-Length: 36
Connection: keep-alive
Echo-Observe: /sfdc/casp/echo/example/object-id-ffa74592-19be-41b0-9e1a-8802d6aabcf1
Echo-Cache-Status: MISS

$ curl -X GET -I -H 'Host: pteyer-ltm1.internal.salesforce.com:8786' "http://127.0.0.1/$ID"
HTTP/1.1 200 OK
Server: openresty/1.11.2.1
Date: Mon, 24 Oct 2016 18:14:45 GMT
Content-Type: text/plain
Content-Length: 36
Connection: keep-alive
Echo-Observe: /sfdc/casp/echo/example/object-id-ffa74592-19be-41b0-9e1a-8802d6aabcf1
Echo-Cache-Status: HIT

$ export ID=`curl -X PUT -H 'Host: pteyer-ltm1.internal.salesforce.com:8786' "http://127.0.0.1/$ID" --data "$(uuidgen)"`
$ curl -X GET -I -H 'Host: pteyer-ltm1.internal.salesforce.com:8786' "http://127.0.0.1/$ID"
HTTP/1.1 200 OK
Server: openresty/1.11.2.1
Date: Mon, 24 Oct 2016 18:15:17 GMT
Content-Type: text/plain
Content-Length: 36
Connection: keep-alive
Echo-Observe: /sfdc/casp/echo/example/object-id-ffa74592-19be-41b0-9e1a-8802d6aabcf1
Echo-Cache-Status: MISS

$ curl -X GET -I -H 'Host: pteyer-ltm1.internal.salesforce.com:8786' "http://127.0.0.1/$ID"
HTTP/1.1 200 OK
Server: openresty/1.11.2.1
Date: Mon, 24 Oct 2016 18:15:23 GMT
Content-Type: text/plain
Content-Length: 36
Connection: keep-alive
Echo-Observe: /sfdc/casp/echo/example/object-id-ffa74592-19be-41b0-9e1a-8802d6aabcf1
Echo-Cache-Status: HIT
```
