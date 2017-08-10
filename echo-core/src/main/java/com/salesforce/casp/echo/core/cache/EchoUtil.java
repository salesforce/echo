package com.salesforce.casp.echo.core.cache;

import com.google.common.hash.Hashing;
import com.salesforce.casp.echo.core.HttpHeader;
import com.salesforce.casp.echo.core.HttpRequest;
import com.salesforce.casp.echo.core.HttpResponse;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EchoUtil {

    public static HttpRequest toRequest(final HttpServletRequest request) {
        return new HttpRequest(request.getMethod(),
                request.getRequestURI(),
                Collections.list(request.getHeaderNames()).stream()
                        .map(h -> new HttpHeader(h, Collections.list(request.getHeaders(h))))
                        .collect(Collectors.toList()));
    }

    public static HttpRequest toRequest(final ContainerRequestContext request) {
        return new HttpRequest(request.getMethod(),
                request.getUriInfo().getRequestUri().toString(),
                request.getHeaders().keySet().stream()
                        .map(h -> new HttpHeader(h, request.getHeaders().get(h)))
                        .collect(Collectors.toList()));
    }

    public static int getTtl(final List<HttpHeader> headers) {
        int ttl = 30; // default to 30 seconds for now
        for (final HttpHeader header : headers) {
            if (header.getName().equalsIgnoreCase("Cache-Control")) {
                for (final Object v : header.getValues()) {
                    if (! (v instanceof String)) {
                        continue;
                    }
                    final String value = String.valueOf(v);
                    if (value.startsWith("max-age=")) {
                        try {
                            ttl = Integer.valueOf(value.substring(value.indexOf("max-age="), value.length()));
                        } catch (RuntimeException ignored) {}
                    }
                }
            }
        }
        return ttl;
    }

    public static String getHash(final byte[] bytes) {
        return Hashing.murmur3_128().hashBytes(bytes) + ":" + Hashing.crc32().hashBytes(bytes);
    }

    public static HttpResponse toResponse(final ContainerResponseContext response) {
        final List<HttpHeader> headers = response.getHeaders().keySet().stream()
                .map(h -> new HttpHeader(h, response.getHeaders().get(h)))
                .collect(Collectors.toList());
        final long timestamp = System.currentTimeMillis();
        return new HttpResponse(headers, response.getStatus(),
                response.getEntity(), timestamp,
                timestamp + getTtl(headers) * 1_000_000);
    }
}

