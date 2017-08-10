package com.salesforce.casp.echo.core.cache;

import com.google.common.base.Charsets;
import com.salesforce.casp.echo.core.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBaseEchoCache {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected String getKeyForRequest(final HttpRequest request) {
        return EchoUtil.getHash(request.getUri().getBytes(Charsets.UTF_8));
    }
}
