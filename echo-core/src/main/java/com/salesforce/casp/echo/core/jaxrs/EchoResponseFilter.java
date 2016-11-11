package com.salesforce.casp.echo.core.jaxrs;

import com.salesforce.casp.echo.core.impl.EchoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.ENTITY_CODER)
public class EchoResponseFilter extends AbstractBaseEchoRequestFilter implements ContainerResponseFilter {
    private static final Logger logger = LoggerFactory.getLogger(EchoResponseFilter.class);

    @Override
    public void filter(final ContainerRequestContext requestContext,
                       final ContainerResponseContext responseContext) throws IOException {
        if (!requestContext.getMethod().equals("GET") && !requestContext.getMethod().equals("HEAD")) {
            logger.info("non-get/head method; echo cache response ignored.");
            return;
        }

        if (responseContext.getEntity() == null || responseContext.getStatus() != Response.Status.OK.getStatusCode()) {
            logger.info("status code is: {}", responseContext.getStatus());
            return;
        }

        cache.put(EchoUtil.toRequest(requestContext), EchoUtil.toResponse(responseContext));
    }
}

