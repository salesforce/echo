package com.salesforce.casp.echo.example;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.servlet.ServletContainer;

import java.net.URI;

public class HttpServer {
    private final String bind;

    public HttpServer(final String bind) {
        this.bind = bind;
    }

    public void start() throws Exception {

        final ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(RolesAllowedDynamicFeature.class);
        resourceConfig.packages("com.salesforce.casp.echo.example");

        final Server server = JettyHttpContainerFactory.createServer(
                new URI(this.bind), false  // do not start
        );

        final ServletHolder servletHolder = new ServletHolder(new ServletContainer(resourceConfig));
        final ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.addServlet(servletHolder, "/*");

        final ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        contextHandlerCollection.setHandlers(new Handler[]{servletContextHandler});
        final HandlerCollection handlerCollection = new HandlerCollection();
        handlerCollection.setHandlers(new Handler[]{contextHandlerCollection});

        server.setHandler(handlerCollection);
        server.setDumpAfterStart(false);
        server.setDumpBeforeStop(false);
        server.setStopAtShutdown(true);

        server.start();
        server.join();
    }
}
