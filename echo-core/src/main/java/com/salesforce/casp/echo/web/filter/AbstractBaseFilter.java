package com.salesforce.casp.echo.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public abstract class AbstractBaseFilter implements Filter {
    /**
     * If a request attribute NO_FILTER is set, then filtering will be skipped
     */
    public static final String NO_FILTER = "NO_FILTER";

    private static final Logger logger = LoggerFactory.getLogger(AbstractBaseFilter.class);

    /**
     * The filter configuration.
     */
    protected FilterConfig filterConfig;

    /**
     * The exceptions to log differently, as a comma separated list
     */
    protected String exceptionsToLogDifferently;

    /**
     * Most {@link Throwable}s in Web applications propagate to the user. Usually they are logged where they first
     * happened. Printing the stack trace once a {@link Throwable} as propagated to the servlet is sometimes
     * just clutters the log.
     * <p/>
     * This field corresponds to an init-param of the same name. If set to true stack traces will be suppressed.
     */
    protected boolean suppressStackTraces;


    /**
     * Performs the filtering.  This method calls template method
     * {@link #doFilter(HttpServletRequest,HttpServletResponse,FilterChain) } which does the filtering.
     * This method takes care of error reporting and handling.
     * Errors are reported at warn level because http tends to produce lots of errors.
     *
     * @throws IOException if an IOException occurs during this method it will be rethrown and will not be wrapped
     */
    public final void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws ServletException, IOException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            //NO_FILTER set for RequestDispatcher forwards to avoid double gzipping
            if (filterNotDisabled(httpRequest)) {
                doFilter(httpRequest, httpResponse, chain);
            } else {
                chain.doFilter(request, response);
            }
        } catch (final Throwable throwable) {
            logThrowable(throwable, httpRequest);
        }
    }

    /**
     * Filters can be disabled programmatically by adding a {@link #NO_FILTER} parameter to the request.
     * This parameter is normally added to make RequestDispatcher include and forwards work.
     *
     * @param httpRequest the request
     * @return true if NO_FILTER is not set.
     */
    protected boolean filterNotDisabled(final HttpServletRequest httpRequest) {
        return httpRequest.getAttribute(NO_FILTER) == null;
    }

    /**
     * This method should throw IOExceptions, not wrap them.
     */
    private void logThrowable(final Throwable throwable, final HttpServletRequest httpRequest)
            throws ServletException, IOException {
        StringBuffer messageBuffer = new StringBuffer("Throwable thrown during doFilter on request with URI: ")
                .append(httpRequest.getRequestURI())
                .append(" and Query: ")
                .append(httpRequest.getQueryString());
        String message = messageBuffer.toString();
        boolean matchFound = matches(throwable);
        if (matchFound) {
            try {
                if (suppressStackTraces) {
                    logger.error(throwable.getMessage());
                } else {
                    logger.error(throwable.getMessage(), throwable);
                }
            } catch (Exception e) {
                logger.error("Could not invoke Log method", e);
            }
            if (throwable instanceof IOException) {
                throw (IOException) throwable;
            } else {
                throw new ServletException(message, throwable);
            }
        } else {

            if (suppressStackTraces) {
                logger.warn(messageBuffer.append(throwable.getMessage()).append("\nTop StackTraceElement: ")
                        .append(throwable.getStackTrace()[0].toString()).toString());
            } else {
                logger.warn(messageBuffer.append(throwable.getMessage()).toString(), throwable);
            }
            if (throwable instanceof IOException) {
                throw (IOException) throwable;
            } else {
                throw new ServletException(throwable);
            }
        }
    }

    /**
     * Checks whether a throwable, its root cause if it is a {@link ServletException}, or its cause, if it is a
     * Chained Exception matches an entry in the exceptionsToLogDifferently list
     *
     * @param throwable
     * @return true if the class name of any of the throwables is found in the exceptions to log differently
     */
    private boolean matches(Throwable throwable) {
        if (exceptionsToLogDifferently == null) {
            return false;
        }
        if (exceptionsToLogDifferently.indexOf(throwable.getClass().getName()) != -1) {
            return true;
        }
        if (throwable instanceof ServletException) {
            Throwable rootCause = (((ServletException) throwable).getRootCause());
            if (exceptionsToLogDifferently.indexOf(rootCause.getClass().getName()) != -1) {
                return true;
            }
        }
        if (throwable.getCause() != null) {
            Throwable cause = throwable.getCause();
            if (exceptionsToLogDifferently.indexOf(cause.getClass().getName()) != -1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final void init(final FilterConfig filterConfig) throws ServletException {
        try {
            this.filterConfig = filterConfig;
            processInitParams(filterConfig);

            // Attempt to initialise this filter
            doInit(filterConfig);
        } catch (final Exception e) {
            logger.error("Could not initialise servlet filter.", e);
            throw new ServletException("Could not initialise servlet filter.", e);
        }
    }

    /**
     * Processes initialisation parameters. These are configured in web.xml in accordance with the
     * Servlet specification using the following syntax:
     * <pre>
     * <filter>
     *      ...
     *      <init-param>
     *          <param-name>blah</param-name>
     *          <param-value>blahvalue</param-value>
     *      </init-param>
     *      ...
     * </filter>
     * </pre>
     * @throws ServletException
     */
    protected void processInitParams(final FilterConfig config) throws ServletException {
        String exceptions = config.getInitParameter("exceptionsToLogDifferently");
        String level = config.getInitParameter("exceptionsToLogDifferentlyLevel");
        String suppressStackTracesString = config.getInitParameter("suppressStackTraces");
        suppressStackTraces = Boolean.valueOf(suppressStackTracesString).booleanValue();
        if (logger.isDebugEnabled()) {
            logger.debug("Suppression of stack traces enabled for " + this.getClass().getName());
        }

        if (exceptions != null) {
            validateMandatoryParameters(exceptions, level);
            exceptionsToLogDifferently = exceptions;
            if (logger.isDebugEnabled()) {
                logger.debug("Different logging levels configured for " + this.getClass().getName());
            }
        }
    }

    private void validateMandatoryParameters(String exceptions, String level) throws ServletException {
        if ((exceptions != null && level == null) || (level != null && exceptions == null)) {
            throw new ServletException("Invalid init-params. Both exceptionsToLogDifferently"
                    + " and exceptionsToLogDifferentlyLevelvalue should be specified if one is"
                    + " specified.");
        }
    }

    /**
     * Destroys the filter. Calls template method {@link #doDestroy()}  to perform any filter specific
     * destruction tasks.
     */
    public final void destroy() {
        this.filterConfig = null;
        doDestroy();
    }

    /**
     * Checks if request accepts the named encoding.
     */
    protected boolean acceptsEncoding(final HttpServletRequest request, final String name) {
        final boolean accepts = headerContains(request, "Accept-Encoding", name);
        return accepts;
    }

    /**
     * Checks if request contains the header value.
     */
    private boolean headerContains(final HttpServletRequest request, final String header, final String value) {
        final Enumeration accepted = request.getHeaders(header);
        while (accepted.hasMoreElements()) {
            final String headerValue = (String) accepted.nextElement();
            if (headerValue.indexOf(value) != -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * A template method that performs any AbstractBaseFilter specific destruction tasks.
     * Called from {@link #destroy()}
     */
    protected abstract void doDestroy();


    /**
     * A template method that performs the filtering for a request.
     * Called from {@link #doFilter(ServletRequest,ServletResponse,FilterChain)}.
     */
    protected abstract void doFilter(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse,
                                     final FilterChain chain) throws Throwable;

    /**
     * A template method that performs any AbstractBaseFilter specific initialisation tasks.
     * Called from {@link #init(FilterConfig)}.
     * @param filterConfig
     */
    protected abstract void doInit(FilterConfig filterConfig) throws Exception;

    /**
     * Returns the filter config.
     */
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    /**
     * Determine whether the user agent accepts GZIP encoding. This feature is part of HTTP1.1.
     * If a browser accepts GZIP encoding it will advertise this by including in its HTTP header:
     * <p/>
     * <code>
     * Accept-Encoding: gzip
     * </code>
     * <p/>
     * Requests which do not accept GZIP encoding fall into the following categories:
     * <ul>
     * <li>Old browsers, notably IE 5 on Macintosh.
     * <li>Search robots such as yahoo. While there are quite a few bots, they only hit individual
     * pages once or twice a day. Note that Googlebot as of August 2004 now accepts GZIP.
     * <li>Internet Explorer through a proxy. By default HTTP1.1 is enabled but disabled when going
     * through a proxy. 90% of non gzip requests are caused by this.
     * <li>Site monitoring tools
     * </ul>
     * As of September 2004, about 34% of requests coming from the Internet did not accept GZIP encoding.
     *
     * @param request
     * @return true, if the User Agent request accepts GZIP encoding
     */
    protected boolean acceptsGzipEncoding(HttpServletRequest request) {
        return acceptsEncoding(request, "gzip");
    }

}

