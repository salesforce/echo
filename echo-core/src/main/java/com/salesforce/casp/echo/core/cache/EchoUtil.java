package com.salesforce.casp.echo.core.cache;

import com.google.common.hash.Hashing;
import com.salesforce.casp.echo.core.HttpHeader;
import com.salesforce.casp.echo.core.HttpRequest;
import com.salesforce.casp.echo.core.HttpResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EchoUtil {

    public static HttpRequest toRequest(final HttpServletRequest request) {
        final StringBuffer requestURL = request.getRequestURL();
        if (request.getQueryString() != null) {
            requestURL.append("?").append(request.getQueryString());
        }
        final String url = requestURL.toString();
        return new HttpRequest(request.getMethod(),
                url,
                Collections.list(request.getHeaderNames()).stream()
                        .map(h -> new HttpHeader(h, Collections.list(request.getHeaders(h))))
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

    static String getHash(final byte[] bytes) {
        return Hashing.murmur3_128().hashBytes(bytes) + ":" + Hashing.crc32().hashBytes(bytes);
    }

    public static HttpResponse toResponse(final HttpServletResponse response) {
        final List<HttpHeader> headers = getHeaders(response);
        final long timestamp = System.currentTimeMillis();
        return new HttpResponse(headers, response.getStatus(),
                "test".getBytes(), timestamp,
                timestamp + getTtl(headers) * 1_000_000);
    }

    public static List<HttpHeader> getHeaders(final HttpServletRequest request) {
        return Collections.list(request.getHeaderNames()).stream()
                .map(h -> new HttpHeader(h, Collections.list(request.getHeaders(h))))
                .collect(Collectors.toList());
    }

    public static List<HttpHeader> getHeaders(final HttpServletResponse response) {
        final List<HttpHeader> headers = new ArrayList<>();
        for (final String headerName : response.getHeaderNames()) {
            headers.add(new HttpHeader(headerName, (List) response.getHeaders(headerName)));
        }
        return headers;
    }
}

