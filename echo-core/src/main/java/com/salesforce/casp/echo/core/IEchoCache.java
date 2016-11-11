package com.salesforce.casp.echo.core;

import com.salesforce.casp.echo.Echo;

public interface IEchoCache {

    // lookup response for this request
    Echo.HttpResponse get(Echo.HttpRequest request);

    // cache response for this request
    void put(Echo.HttpRequest request, Echo.HttpResponse response);
}
