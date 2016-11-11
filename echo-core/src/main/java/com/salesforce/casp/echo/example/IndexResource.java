package com.salesforce.casp.echo.example;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Path("/")
public class IndexResource {

    private static final String objectObservablePrefix = "/sfdc/casp/echo/example/object-id-";

    private static final Map<String, String> store = new ConcurrentHashMap<>();

    @POST
    public Response post(final String body) {
        final String id = UUID.randomUUID().toString();
        store.put(id, body);
        return Response.ok(id)
                .build();
    }

    @PUT @Path("/{id}")
    public Response put(@PathParam("id") final String id, final String body) {
        store.put(id, body);
        return Response.ok(id)
                .header("Echo-Notify", objectObservablePrefix + id)
                .build();
    }

    @GET @Path("/{id}")
    public Response getObjectById(@PathParam("id") final String id) {
        if (!store.containsKey(id)) {
            return Response.status(404).build();
        }
        final String payload = store.get(id);
        return Response.ok(payload)
                .header("Echo-Observe", objectObservablePrefix + id)
                .build();
    }
}


