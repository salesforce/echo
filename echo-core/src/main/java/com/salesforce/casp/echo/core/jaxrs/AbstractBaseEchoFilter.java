package com.salesforce.casp.echo.core.jaxrs;

import com.salesforce.casp.echo.core.Constants;
import com.salesforce.casp.echo.core.HttpHeader;
import com.salesforce.casp.echo.core.HttpRequest;
import com.salesforce.casp.echo.core.HttpResponse;
import com.salesforce.casp.echo.core.cache.EchoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

public abstract class AbstractBaseEchoFilter implements ContainerRequestFilter, ContainerResponseFilter {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractBaseEchoFilter.class);

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        if (! isRequestCacheable(requestContext)) {
            logger.info("{} method; echo cache request ignored.", requestContext.getMethod());
            return;
        }

        final HttpRequest request = EchoUtil.toRequest(requestContext);
        final HttpResponse cachedResponse = lookup(request);
        if (cachedResponse == null) {
            logger.info("cache-miss for request {}", request.getUri());
            return;
        }
        logger.info("cache-hit for request {}", request.getUri());
        if (cachedResponse.getExpireTimestampMillis() < System.currentTimeMillis()) {
            logger.info("cache-hit with stale content for request: {}", request.getUri());
            invalidate(request, cachedResponse);
        }

        final Response.ResponseBuilder responseBuilder = Response.ok()
                .header(Constants.ECHO_CACHE, "Hit")
                .status(cachedResponse.getStatusCode());
        // attach header parameters
        for (final HttpHeader header : cachedResponse.getHeaders()) {
            for (final Object value : header.getValues()) {
                responseBuilder.header(header.getName(), value);
            }
        }
        // add entity body
        if (cachedResponse.getEntity() != null) {
            responseBuilder.entity(cachedResponse.getEntity());
        }
        requestContext.abortWith(responseBuilder.build());
    }

    @Override
    public void filter(final ContainerRequestContext requestContext,
                       final ContainerResponseContext responseContext) throws IOException {
        if (! isResponseCacheable(requestContext, responseContext)) {
            logger.info("non-cacheable response; echo cache response ignored.");
            return;
        }

        final HttpRequest request = EchoUtil.toRequest(requestContext);
        logger.info("storing response for request: {}", request.getUri());
        store(request, EchoUtil.toResponse(responseContext));
    }

    protected boolean isRequestCacheable(final ContainerRequestContext request) {
        return request.getMethod().toLowerCase().matches("get|head");
    }

    protected boolean isResponseCacheable(final ContainerRequestContext request,
                                          final ContainerResponseContext response) {

        if (! request.getMethod().toLowerCase().matches("get|head")) {
            return false;
        }
        if (response.getEntity() == null || response.getStatus() != Response.Status.OK.getStatusCode()) {
            return false;
        }

        return true;
        // TODO check for all headers
    }

    protected abstract HttpResponse lookup(final HttpRequest request);

    protected abstract void store(final HttpRequest request, final HttpResponse response);

    protected abstract void invalidate(final HttpRequest request, final HttpResponse cachedResponse);
}
