package com.salesforce.casp.echo.example;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

//        final String payload = new String(new char[1024*1024]).replace('\0', '.');

@Path("/")
public class IndexResource {
    private static final String allTokensObservable = "/sfdc/casp/echo/example/all-tokens";
    private static final Set<String> tokensSet = new ConcurrentSkipListSet<String>();

    private static volatile int token = 0;

    @GET @Path("/last-token") @Produces(MediaType.TEXT_PLAIN)
    public Response getLastToken() {
        final String payload = String.valueOf(token);
        return Response.ok(payload)
                .header("Content-Length", payload.length())
                .header("Cache-Control", "max-age=86400")
                // observe "all-tokens" for when a new token is generated
                .header("Echo-Observe", allTokensObservable)
                .build();
    }

    @POST @Path("/new-token") @Produces(MediaType.TEXT_PLAIN)
    public Response getCounter() {
        final String payload = String.valueOf(++token);
        tokensSet.add(payload);
        return Response.ok(payload)
                .header("Content-Length", payload.length())
                // new token is generated, so notify the all-tokens observable
                .header("Echo-Notify", allTokensObservable)
                .build();
    }

    @GET @Path("/all-tokens") @Produces(MediaType.TEXT_PLAIN)
    public Response get() {
        final String payload = Arrays.toString(tokensSet.toArray());
        return Response.ok(payload)
                .header("Content-Length", payload.length())
                .header("Cache-Control", "max-age=86400")
                // observe all-tokens for when a new token is generated
                .header("Echo-Observe", allTokensObservable)
                .build();
    }
}


