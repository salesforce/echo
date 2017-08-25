package com.salesforce.casp.echo.servlet;

import com.salesforce.casp.echo.web.filter.SimplePageCachingFilter;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;

public class EchoServletCacheFilter extends SimplePageCachingFilter {
    @Override
    protected CacheManager getCacheManager() {
        final Configuration configuration = new Configuration();
        configuration.addCache(new CacheConfiguration("SimplePageCachingFilter", 1000));
        return CacheManager.create(configuration);
    }
}
