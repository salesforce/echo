package com.salesforce.casp.echo.example;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Path("/")
public class IndexResource {

    private static final String allObjectsObservable = "/sfdc/casp/echo/example/allobjects";
    private static final String objectObservablePrefix = "/sfdc/casp/echo/example/object-id-";
    private static final Map<String, String> store = new ConcurrentHashMap<String, String>();
    private static volatile String lastId = "";

    @POST @Produces(MediaType.TEXT_PLAIN)
    public Response post(final String body) {
        final String id = UUID.randomUUID().toString();
        store.put(id, body);
        lastId = id;
        return Response.ok(id)
                .header("Echo-Notify", allObjectsObservable)
                .build();
    }

    @POST @Path("/{id}") @Produces(MediaType.TEXT_PLAIN)
    public Response put(@PathParam("id") final String id, final String body) {
        store.put(id, body);
        lastId = id;
        return Response.ok(id)
                .header("Echo-Notify", objectObservablePrefix + id)
                .build();
    }

    @GET @Path("/{id}") @Produces(MediaType.TEXT_PLAIN)
    public Response getObjectById(@PathParam("id") final String id) {
        final String payload = store.getOrDefault(id, "");
        return Response.ok(payload)
                .header("Echo-Observe", objectObservablePrefix + id)
                .build();
    }

    @GET @Path("/lastid") @Produces(MediaType.TEXT_PLAIN)
    public Response getLastId() {
        return Response.ok(lastId)
                .header("Echo-Observe", allObjectsObservable)
                .build();
    }

    @GET @Path("/all") @Produces(MediaType.TEXT_PLAIN)
    public Response get() {
        final String payload = Arrays.toString(store.entrySet().toArray());
        return Response.ok(payload)
                // observe all-tokens for when a new token is generated
                .header("Echo-Observe", allObjectsObservable)
                .build();
    }
}


