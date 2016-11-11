package com.salesforce.casp.echo.core.impl;

import com.salesforce.casp.echo.core.IEchoCache;

public class EchoCacheOnEhcache extends AbstractBaseEchoCache implements IEchoCache {

    @Override
    public void put(final String key, final byte[] value) {

    }

    @Override
    public void expire(final String key, final long ttlMillis) {

    }

    @Override
    public byte[] get(final String key) {
        return new byte[0];
    }

    @Override
    public void delete(final String key) {

    }
}
