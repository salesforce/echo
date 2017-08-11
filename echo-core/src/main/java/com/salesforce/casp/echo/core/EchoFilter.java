package com.salesforce.casp.echo.core;

public interface EchoFilter {

    HttpResponse lookup(final HttpRequest request);

    void store(final HttpRequest request, final HttpResponse response);

    void invalidate(final HttpRequest request, final HttpResponse response);
}
