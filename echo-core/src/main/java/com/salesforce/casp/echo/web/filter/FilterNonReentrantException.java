package com.salesforce.casp.echo.web.filter;

import net.sf.ehcache.CacheException;

/**
 * Thrown when it is detected that a caching filter's {@link CachingFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
 * method is reentered by the same thread.
 * <p/>
 * Reentrant calls will block indefinitely because the first request has not yet
 * unblocked the cache.
 * <p/>
 * This condition usually happens declaratively when the same filter is specified twice in a filter chain
 * or programmatically when a {@link javax.servlet.RequestDispatcher} includes or forwards back to the same URL,
 * either directly or indirectly.
 * @author Greg Luck
 * @version $Id: FilterNonReentrantException.java 744 2008-08-16 20:10:49Z gregluck $
 */
public class FilterNonReentrantException extends CacheException {

    /**
     * Constructor for the exception
     */
    public FilterNonReentrantException() {
        super();
    }

    /**
     * Constructs an exception with the message given
     *
     * @param message the message
     */
    public FilterNonReentrantException(String message) {
        super(message);
    }
}
