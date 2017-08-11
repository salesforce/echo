package com.salesforce.casp.echo.servlet;

import com.salesforce.casp.echo.core.EchoFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

abstract class AbstractBaseEchoFilter implements EchoFilter, Filter {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        logger.info("{} init called.", getClass());
    }

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        // pre-handle
        filterChain.doFilter(servletRequest, servletResponse);
        // post-handle
    }

    @Override
    public void destroy() {
        logger.info("{} destroy called.", getClass());
    }
}
