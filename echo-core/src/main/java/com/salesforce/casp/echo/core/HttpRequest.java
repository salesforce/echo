package com.salesforce.casp.echo.core;

import com.google.common.base.MoreObjects;

import java.util.List;
import java.util.Objects;

public class HttpRequest {
    private final String method;
    private final String uri;
    private final List<HttpHeader> headers;

    public HttpRequest(String method, String uri, List<HttpHeader> headers) {
        this.method = method;
        this.uri = uri;
        this.headers = headers;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public List<HttpHeader> getHeaders() {
        return headers;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        final HttpRequest other = (HttpRequest) o;
        return Objects.equals(getUri(), other.getUri())
                && Objects.equals(getMethod(), other.getMethod())
                && Objects.equals(getHeaders(), other.getHeaders())
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMethod(), getUri(), getHeaders());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("method", getMethod())
                .add("uri", getUri())
                .add("headers", getHeaders())
                .toString();
    }
}
