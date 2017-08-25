package com.salesforce.casp.echo.web.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A custom {@link ServletOutputStream} for use by our filters
 *
 * @version $Id: FilterServletOutputStream.java 744 2008-08-16 20:10:49Z gregluck $
 * @author <a href="mailto:gluck@thoughtworks.com">Greg Luck</a>
 */
public class FilterServletOutputStream extends ServletOutputStream {

    private OutputStream stream;

    /**
     * Creates a FilterServletOutputStream.
     */
    public FilterServletOutputStream(final OutputStream stream) {
        this.stream = stream;
    }

    /**
     * Writes to the stream.
     */
    public void write(final int b) throws IOException {
        stream.write(b);
    }

    /**
     * Writes to the stream.
     */
    public void write(final byte[] b) throws IOException {
        stream.write(b);
    }

    /**
     * Writes to the stream.
     */
    public void write(final byte[] b, final int off, final int len) throws IOException {
        stream.write(b, off, len);
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        // no-op
    }
}

