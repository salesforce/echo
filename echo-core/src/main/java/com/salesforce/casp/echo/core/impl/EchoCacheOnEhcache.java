package com.salesforce.casp.echo.core.impl;

import com.salesforce.casp.echo.Echo;
import com.salesforce.casp.echo.core.IEchoCache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import static com.google.common.base.Preconditions.checkArgument;

public class EchoCacheOnEhcache extends AbstractBaseEchoCache implements IEchoCache {

    private final Cache<String, Echo.HttpResponse> responseCache;

    public EchoCacheOnEhcache() {
        final CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();
        final MutableConfiguration<String, Echo.HttpResponse> configuration = new MutableConfiguration<String, Echo.HttpResponse>()
                        .setTypes(String.class, Echo.HttpResponse.class)
                        .setStoreByValue(false)
                        .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));
        this.responseCache = cacheManager.createCache("echo-cache", configuration);
    }

    @Override
    public Echo.HttpResponse get(final Echo.HttpRequest request) {
        checkArgument(request != null, "null request");
        return this.responseCache.get(getKeyForRequest(request));
    }

    @Override
    public void put(final Echo.HttpRequest request, final Echo.HttpResponse response) {
        checkArgument(request != null, "null request");
        checkArgument(response != null, "null response");
        this.responseCache.put(getKeyForRequest(request), response);
    }
}
