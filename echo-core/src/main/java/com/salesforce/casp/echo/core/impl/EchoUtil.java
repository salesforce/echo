package com.salesforce.casp.echo.core.impl;

import com.google.common.hash.Hashing;
import com.salesforce.casp.echo.Echo;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EchoUtil {

    public static Echo.HttpRequest toRequest(final HttpServletRequest request) {
        return Echo.HttpRequest.newBuilder()
                .setUri(request.getRequestURI())
                .setMethod(Echo.HttpMethod.valueOf(request.getMethod()))
                .addAllHeaders(Collections.list(request.getHeaderNames()).stream()
                        .map(h -> Echo.HttpHeader.newBuilder().setName(h).addAllValues(Collections.list(request.getHeaders(h))).build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static Echo.HttpRequest toRequest(final ContainerRequestContext request) {
        return Echo.HttpRequest.newBuilder()
                .setUri(request.getUriInfo().getRequestUri().toString())
                .setMethod(Echo.HttpMethod.valueOf(request.getMethod()))
                .addAllHeaders(request.getHeaders().keySet().stream()
                        .map(h -> Echo.HttpHeader.newBuilder().setName(h).addAllValues(request.getHeaders().get(h)).build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static boolean isCacheable(final Echo.HttpRequest request) {
        // head or get only
        return request.getMethod() == Echo.HttpMethod.GET || request.getMethod() == Echo.HttpMethod.HEAD;
    }

    public static int getTtl(final List<Echo.HttpHeader> headers) {
        int ttl = 0;
        for (final Echo.HttpHeader header : headers) {
            if (header.getName().equalsIgnoreCase("Cache-Control")) {
                for (final String value : header.getValuesList()) {
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

    public static Echo.HttpResponse toResponse(final ContainerResponseContext response) {
        final List<Echo.HttpHeader> headers = response.getHeaders().keySet().stream()
                .map(h -> Echo.HttpHeader.newBuilder().setName(h).addAllValues((List) response.getHeaders().get(h)).build())
                .collect(Collectors.toList());
        return Echo.HttpResponse.newBuilder()
                .addAllHeaders(headers)
                .setStatusCode(response.getStatus())
                .setUpdateTimestamp(System.currentTimeMillis())
                .setExpireTimestamp(System.currentTimeMillis() + getTtl(headers))
                .build();
    }
}

