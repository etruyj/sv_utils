//===================================================================
// Server.java
//      Description:
//          The web server application.         
//===================================================================

package com.socialvagrancy.utils.http;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);
    private HttpServer server;
    private HashMap<String, Object> property_map;

    public Server() {
        property_map = new HashMap<String, Object>();
    }

    public Server(String ip, String port, String api_prefix, Map<String, Object> property_map) {
        String BASE_URI = "http://" + ip + ":" + port + "/" + api_prefix + "/";

        try {
            log.info("Starting server and listening on " + BASE_URI);
            
            server = startServer(BASE_URI, property_map);

            System.err.println("Server started and listening at " + BASE_URI);
            log.info("Server started successfully.");

            System.err.println("Press [enter] to stop the server...");
            System.in.read();

            String message = "Shutting down task server.";
            log.warn(message);
            System.err.println(message);

            server.shutdown();
            
            message = "Server shutdown successfully.";
            log.warn(message);
            System.err.println(message);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            log.error(e.getMessage());
            log.error("Failed to start server.");
        }
    }

    public Server(String ip, String port, String api_prefix, HashMap<String, Object> property_map) {
        // This constructor starts the server with sl4j logger and a property map passed to it instead
        // of forcing the script to use classes that are part of this package.        
        String BASE_URI = "http://" + ip + ":" + port + "/" + api_prefix + "/";

        try {
            log.info("Starting server and listening on " + BASE_URI);

            server = startServer(BASE_URI, property_map);
            

            log.info("Server started successfully.");
            System.err.println("Choix Recette server started and listening at " + BASE_URI);

            System.err.println("Press [enter] to stop the server...");
            System.in.read();

            String message = "Shutting down task server.";
            log.warn(message);
            System.err.println(message);

            server.shutdown();

            message = "Server shutdown successfully.";
            log.warn(message);
            System.err.println(message);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            log.error(e.getMessage());
            log.error("Failed to start server.");
        }
    }

    public Server(String ip, String port, String resource_dir, String url_path) {
        // This constructor handles creating a server that loads static webpages.
        String BASE_URI = "http://" + ip + ":" + port + "/";

        try {
            log.info("Starting server and listening on " + BASE_URI);

            server = startServer(BASE_URI, null); // starting server without a property_map.
            
            addStaticFileHandler(resource_dir, url_path);

            log.info("Server started successfully.");
            System.err.println("Choix Recette server started and listening at " + BASE_URI);

            System.err.println("Press [enter] to stop the server...");
            System.in.read();

            String message = "Shutting down task server.";
            log.warn(message);
            System.err.println(message);

            server.shutdown();

            message = "Server shutdown successfully.";
            log.warn(message);
            System.err.println(message);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            log.error(e.getMessage());
            log.error("Failed to start server.");
        }
    }

    public void addStaticFileHandler(String resource_dir, String web_path) {
        log.info("Serving HTML pages from " + resource_dir);
        
        StaticHttpHandler staticHttpHandler = new StaticHttpHandler(resource_dir);
        server.getServerConfiguration().addHttpHandler(staticHttpHandler, web_path);
    }

    public static HttpServer startServer(String BASE_URI, Map<String, Object> property_map) {
        final ResourceConfig config = new ResourceConfig()
            .packages("com.socialvagrancy.choixrecette.util.server")
            .addProperties(property_map);

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
    }

    //===========================================
    // Getters
    //===========================================
    //===========================================
    // Setters
    //===========================================
    public void addProperty(String name, Object property) { property_map.put(name, property); }
    public void setPropertyMap(HashMap<String, Object> property_map) { this.property_map = property_map; }
    public void setServer(HttpServer server) { this.server = server; }
}
