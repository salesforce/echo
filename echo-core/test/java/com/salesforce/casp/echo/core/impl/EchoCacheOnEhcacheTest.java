package com.salesforce.casp.echo.core.impl;

import com.salesforce.casp.echo.core.HttpRequest;
import com.salesforce.casp.echo.core.HttpResponse;
import com.salesforce.casp.echo.core.IEchoCache;
import com.salesforce.casp.echo.core.cache.EchoCacheOnEhcache;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.testng.Assert.*;

public class EchoCacheOnEhcacheTest {

    @Test
    public void testGetPut() throws Exception {
        final IEchoCache cache = new EchoCacheOnEhcache();

        for (int i = 0; i < 1000; ++i) {
            final HttpRequest request = new HttpRequest("lookup", UUID.randomUUID().toString(), Arrays.asList());
            final HttpResponse response = new HttpResponse(null, 100, UUID.randomUUID().toString(), 1000, 1000);

            cache.store(request, response);
            assertEquals(cache.lookup(request), response);
        }
    }
}
