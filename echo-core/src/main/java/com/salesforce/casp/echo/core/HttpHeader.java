package com.salesforce.casp.echo.core;

import com.google.common.base.MoreObjects;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HttpHeader {
    private final String name;
    private final List values;

    public HttpHeader(String name, List values) {
        this.name = name;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public List getValues() {
        return values;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        final HttpHeader other = (HttpHeader) o;
        return Objects.equals(getName(), other.getName())
                && Objects.equals(getValues(), other.getValues());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getValues());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", getName())
                .add("values", getValues())
                .toString();
    }
}
