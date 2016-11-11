package com.salesforce.casp.echo.core.jaxrs;

import com.salesforce.casp.echo.Echo;
import com.salesforce.casp.echo.core.Constants;
import com.salesforce.casp.echo.core.impl.EchoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.ENTITY_CODER)
public class EchoRequestFilter extends AbstractBaseEchoRequestFilter implements ContainerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(EchoRequestFilter.class);

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        if (!requestContext.getMethod().equals("GET") && !requestContext.getMethod().equals("HEAD")) {
            logger.info("non-get/head method; echo cache request ignored.");
            return;
        }

        final Echo.HttpRequest request = EchoUtil.toRequest(requestContext);
        final Echo.HttpResponse response = cache.get(request);
        if (response == null) {
            logger.info("cache-miss for request {}", request);
            return;
        }
        logger.info("cache-hit for request {}", request);

        if (request.getMethod() == Echo.HttpMethod.HEAD) {
            final Response.ResponseBuilder responseBuilder = Response.ok()
                    .header(Constants.ECHO_CACHE, "Hit")
                    .status(response.getStatusCode());
            if (response.hasBody()) {
                responseBuilder.entity(response.getBody().toByteArray());
            }
            requestContext.abortWith(responseBuilder.build());
        }
    }
}

