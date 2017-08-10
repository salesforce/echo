package com.salesforce.casp.echo.core.cache;

import com.salesforce.casp.echo.core.HttpRequest;
import com.salesforce.casp.echo.core.HttpResponse;
import com.salesforce.casp.echo.core.IEchoCache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import static com.google.common.base.Preconditions.checkArgument;

public class EchoCacheOnEhcache extends AbstractBaseEchoCache implements IEchoCache {

    private final Cache<HttpRequest, HttpResponse> responseCache;

    public EchoCacheOnEhcache() {
        final CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();
        final MutableConfiguration<HttpRequest, HttpResponse> configuration = new MutableConfiguration<HttpRequest, HttpResponse>()
                .setTypes(HttpRequest.class, HttpResponse.class)
                .setStoreByValue(false)
                .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_DAY));
        this.responseCache = cacheManager.createCache("echo-cache", configuration);
    }

    @Override
    public HttpResponse lookup(final HttpRequest request) {
        checkArgument(request != null, "null request");
        return this.responseCache.get(request);
    }

    @Override
    public void store(final HttpRequest request, final HttpResponse response) {
        checkArgument(request != null, "null request");
        checkArgument(response != null, "null response");
        this.responseCache.put(request, response);
    }

    @Override
    public void invalidate(final HttpRequest request, final HttpResponse response) {
        checkArgument(request != null, "null request");
        checkArgument(response != null, "null response");
        this.responseCache.remove(request);
    }
}

