package com.salesforce.casp.echo.core.impl;

import com.google.protobuf.ByteString;
import com.salesforce.casp.echo.Echo;
import com.salesforce.casp.echo.core.IEchoCache;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.Assert.*;

public class EchoCacheOnEhcacheTest {

    @Test
    public void testGetPut() throws Exception {
        final IEchoCache cache = new EchoCacheOnEhcache();

        for (int i = 0; i < 1000; ++i) {
            final Echo.HttpRequest request = Echo.HttpRequest.newBuilder()
                    .setMethod(Echo.HttpMethod.GET)
                    .setUri(UUID.randomUUID().toString())
                    .build();

            final Echo.HttpResponse response = Echo.HttpResponse.newBuilder()
                    .addHeaders(Echo.HttpHeader.newBuilder().setName(UUID.randomUUID().toString()).addValues(UUID.randomUUID().toString()))
                    .setStatusCode(200)
                    .setBody(ByteString.copyFrom(UUID.randomUUID().toString().getBytes()))
                    .build();

            cache.put(request, response);
            assertEquals(cache.get(request), response);
        }
    }
}
