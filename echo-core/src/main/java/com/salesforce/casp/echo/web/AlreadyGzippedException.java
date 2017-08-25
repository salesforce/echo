package com.salesforce.casp.echo.web;

import net.sf.ehcache.CacheException;

/**
 * The web package performs gzipping operations. One cause of problems on web browsers
 * is getting content that is double or triple gzipped. They will either get gobblydeegook
 * or a blank page. This exception is thrown when a gzip is attempted on already gzipped content.
 * <p/>
 * This exception should be logged and the causes investigated, which are likely to be going through
 * a caching filter more than once. This exception is not normally recoverable from.
 * @author Greg Luck
 * @version $Id: AlreadyGzippedException.java 744 2008-08-16 20:10:49Z gregluck $
 */
public class AlreadyGzippedException extends CacheException {

    /**
     * Constructor for the exception
     */
    public AlreadyGzippedException() {
        super();
    }

    /**
     * Constructs an exception with the message given
     * @param message the message
     */
    public AlreadyGzippedException(String message) {
        super(message);
    }
}
