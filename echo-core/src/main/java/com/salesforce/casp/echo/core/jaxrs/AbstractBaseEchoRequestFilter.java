package com.salesforce.casp.echo.core.jaxrs;

import com.salesforce.casp.echo.core.IEchoCache;
import com.salesforce.casp.echo.core.impl.EchoCacheOnEhcache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBaseEchoRequestFilter {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractBaseEchoRequestFilter.class);
    protected static final IEchoCache cache = new EchoCacheOnEhcache();
}
