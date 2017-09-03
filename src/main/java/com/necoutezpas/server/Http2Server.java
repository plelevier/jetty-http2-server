package com.necoutezpas.server;

import org.eclipse.jetty.alpn.ALPN;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import static org.eclipse.jetty.util.resource.Resource.newClassPathResource;

public class Http2Server {

    // In order to run this, you need the alpn-boot-XXX.jar in the bootstrap classpath.
    public static void main(String... args) throws Exception {
        Server server = new Server(8080);

        final HandlerList handlers = new HandlerList();

        handlers.addHandler(getResourceContext("/watch", "watch-ui"));

        ResourceConfig config = new ResourceConfig()
                .register(JacksonFeature.class)
                .register(SseFeature.class);
        config.packages("com.necoutezpas.resource");
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        context.addServlet(servlet, "/*");
        handlers.addHandler(context);

        server.setHandler(handlers);

        // HTTP Configuration
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(8443);

        // SSL Context Factory for HTTPS and HTTP/2
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStoreResource(newClassPathResource("keystore"));
        sslContextFactory.setKeyStorePassword("OBF:1vny1zlo1x8e1vnw1vn61x8g1zlu1vn4");
        sslContextFactory.setKeyManagerPassword("OBF:1u2u1wml1z7s1z7a1wnl1u2g");
        sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);

        // HTTPS Configuration
        HttpConfiguration https_config = new HttpConfiguration(http_config);
        https_config.addCustomizer(new SecureRequestCustomizer());

        // HTTP/2 Connection Factory
        HTTP2ServerConnectionFactory h2 = new HTTP2ServerConnectionFactory(https_config);
        NegotiatingServerConnectionFactory.checkProtocolNegotiationAvailable();
        ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
        alpn.setDefaultProtocol("h2");

        // SSL Connection Factory
        SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory,alpn.getProtocol());

        // HTTP/2 Connector
        ServerConnector http2Connector =
                new ServerConnector(server,ssl,alpn,h2,new HttpConnectionFactory(https_config));
        http2Connector.setPort(8443);
        server.addConnector(http2Connector);

        ALPN.debug=false;


        server.start();
        server.join();
    }

    private static ContextHandler getResourceContext(final String contextPath, final String resourcePAth) throws Exception
    {
        final ResourceHandler handler = new ResourceHandler();
        handler.setResourceBase(Http2Server.class.getClassLoader().getResource(resourcePAth).toURI().toString() );
        final ContextHandler context = new ContextHandler();
        context.setContextPath(contextPath);
        context.setHandler(handler);

        return context;
    }
}
