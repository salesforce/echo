package com.salesforce.casp.echo.core;

import com.google.common.base.MoreObjects;

import java.util.List;
import java.util.Objects;

public class HttpResponse {
    private final List<HttpHeader> headers;
    private final int statusCode;
    private final Object entity;
    private final long updateTimestampMillis;
    private final long expireTimestampMillis;

    public HttpResponse(final List<HttpHeader> headers,
                        int statusCode,
                        final Object entity,
                        long update_timestamp_millis,
                        long expire_timestamp_millis) {
        this.headers = headers;
        this.statusCode = statusCode;
        this.entity = entity;
        this.updateTimestampMillis = update_timestamp_millis;
        this.expireTimestampMillis = expire_timestamp_millis;
    }

    public List<HttpHeader> getHeaders() {
        return headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Object getEntity() {
        return entity;
    }

    public long getUpdateTimestampMillis() {
        return updateTimestampMillis;
    }

    public long getExpireTimestampMillis() {
        return expireTimestampMillis;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        final HttpResponse other = (HttpResponse) o;
        return Objects.equals(getHeaders(), other.getHeaders())
                && Objects.equals(getStatusCode(), other.getStatusCode())
                && Objects.equals(getEntity(), other.getEntity())
                && Objects.equals(getUpdateTimestampMillis(), other.getUpdateTimestampMillis())
                && Objects.equals(getExpireTimestampMillis(), other.getExpireTimestampMillis());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHeaders(),
                getStatusCode(),
                getEntity(),
                getUpdateTimestampMillis(),
                getExpireTimestampMillis());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("status_code", getStatusCode())
                .add("headers", getHeaders())
                .add("entity", getEntity())
                .add("update_timestamp_millis", getUpdateTimestampMillis())
                .add("expire_timestamp_millis", getExpireTimestampMillis())
                .toString();
    }
}
