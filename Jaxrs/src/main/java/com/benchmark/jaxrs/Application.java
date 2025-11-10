package com.benchmark.jaxrs;

import com.benchmark.jaxrs.config.JpaConfig;
import com.benchmark.jaxrs.resource.CategoryItemResource;
import com.benchmark.jaxrs.resource.CategoryResource;
import com.benchmark.jaxrs.resource.ItemResource;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.net.URI;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static final String BASE_URI = "http://localhost:8080/api/";

    public static void main(String[] args) throws IOException {
        logger.info("Starting JAX-RS Jersey application...");

        // Initialize Spring context
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JpaConfig.class);

        // Configure Jersey
        final ResourceConfig resourceConfig = new ResourceConfig(
            CategoryResource.class,
            ItemResource.class,
            CategoryItemResource.class
        );

        // Create and start server
        final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), resourceConfig);

        logger.info("Application started at {}", BASE_URI);
        logger.info("Press Ctrl+C to stop...");

        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down application...");
            server.shutdown();
            context.close();
        }));

        // Keep the application running
        try {
            Thread.currentThread().join();
        } catch (InterruptedException ex) {
            logger.error("Application interrupted", ex);
        }
    }
}
