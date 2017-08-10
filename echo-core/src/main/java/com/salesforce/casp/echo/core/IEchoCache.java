package com.salesforce.casp.echo.core;

public interface IEchoCache {

    // lookup response for this request
    HttpResponse lookup(HttpRequest request);

    // cache response for this request
    void store(HttpRequest request, HttpResponse response);

    // invalidate cached response for the given request
    void invalidate(HttpRequest request, HttpResponse response);
}
