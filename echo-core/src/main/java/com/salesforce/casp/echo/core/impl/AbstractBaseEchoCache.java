package com.salesforce.casp.echo.core.impl;

import com.google.common.base.Charsets;
import com.salesforce.casp.echo.Echo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBaseEchoCache {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected String getKeyForRequest(final Echo.HttpRequest request) {
        return EchoUtil.getHash(request.getUri().getBytes(Charsets.UTF_8));
    }

    protected boolean isValidMethod(final String method) {
        return method.toUpperCase().matches("HEAD|GET|PUT|POST");
    }

}
