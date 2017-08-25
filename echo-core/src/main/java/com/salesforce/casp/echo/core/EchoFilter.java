package com.salesforce.casp.echo.core;

public interface EchoFilter {

    /**
     * Lookup a request and return the cached response if found; null otherwise.
     * @param request
     * @return
     */
    HttpResponse lookup(final HttpRequest request);

    /**
     * Store response for the given request in cache.
     * @param request
     * @param response
     */
    void store(final HttpRequest request, final HttpResponse response);

    /**
     * Invalidate a cached response for the given request.
     * @param request
     * @param response
     */
    void invalidate(final HttpRequest request, final HttpResponse response);
}
