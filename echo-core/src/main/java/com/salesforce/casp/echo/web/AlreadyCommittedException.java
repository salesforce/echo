package com.salesforce.casp.echo.web;

/**
 * <p/>
 * This exception is thrown if, in particular, the {@link javax.servlet.ServletResponse#isCommitted()}
 * method shows the response has been  committed. A commited response has already had its status code and headers written.
 * <p/>
 * One situation that can cause this problem is using jsp:include to include a full, cached page in another
 * page. When the JSP page is entered the response gets committed.
 * @see ResponseHeadersNotModifiableException
 * @author Greg Luck
 * @version $Id: AlreadyCommittedException.java 744 2008-08-16 20:10:49Z gregluck $
 */
public class AlreadyCommittedException extends ResponseHeadersNotModifiableException {

    /**
     * Constructor for the exception
     */
    public AlreadyCommittedException() {
        super();
    }

    /**
     * Constructs an exception with the message given
     * @param message the message
     */
    public AlreadyCommittedException(String message) {
        super(message);
    }
}
