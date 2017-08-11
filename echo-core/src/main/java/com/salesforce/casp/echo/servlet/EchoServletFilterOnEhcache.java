package com.salesforce.casp.echo.servlet;

import com.salesforce.casp.echo.core.HttpRequest;
import com.salesforce.casp.echo.core.HttpResponse;
import com.salesforce.casp.echo.core.IEchoCache;
import com.salesforce.casp.echo.core.cache.EchoCacheOnEhcache;

public class EchoServletFilterOnEhcache extends AbstractBaseEchoFilter {

    protected static final IEchoCache cache = new EchoCacheOnEhcache();

    @Override
    public HttpResponse lookup(final HttpRequest request) {
        return cache.lookup(request);
    }

    @Override
    public void store(final HttpRequest request, final HttpResponse response) {
        cache.store(request, response);
    }

    @Override
    public void invalidate(final HttpRequest request, final HttpResponse cachedResponse) {
        cache.invalidate(request, cachedResponse);
    }
}
