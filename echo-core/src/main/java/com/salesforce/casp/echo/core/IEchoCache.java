package com.salesforce.casp.echo.core;

public interface IEchoCache {

    void put(String key, byte[] value);

    void expire(String key, long ttlMillis);

    byte[] get(String key);

    void delete(String key);
}
