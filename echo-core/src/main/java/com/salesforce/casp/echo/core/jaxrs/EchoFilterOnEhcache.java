package com.salesforce.casp.echo.core.jaxrs;

import com.salesforce.casp.echo.core.HttpRequest;
import com.salesforce.casp.echo.core.HttpResponse;
import com.salesforce.casp.echo.core.IEchoCache;
import com.salesforce.casp.echo.core.cache.EchoCacheOnEhcache;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.ENTITY_CODER)
public class EchoFilterOnEhcache extends AbstractBaseEchoFilter {
    protected static final IEchoCache cache = new EchoCacheOnEhcache();

    public EchoFilterOnEhcache() {
        LoggerFactory.getLogger(getClass()).info("echo cache filter initialized...");
    }

    @Override
    protected HttpResponse lookup(final HttpRequest request) {
        return cache.lookup(request);
    }

    @Override
    protected void store(final HttpRequest request, final HttpResponse response) {
        cache.store(request, response);
    }

    @Override
    protected void invalidate(final HttpRequest request, final HttpResponse cachedResponse) {
        cache.invalidate(request, cachedResponse);
    }
}

