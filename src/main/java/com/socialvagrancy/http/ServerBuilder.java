//===================================================================
// ServerBuilder.java
//      Description:
//          The purpose of this object is to build and start a server
//          object.
//
// Created by etruyj
//===================================================================

package com.socialvagrancy.utils.http;

import com.socialvagrancy.utils.http.filter.CORSAuthorizationFilter;
import com.socialvagrancy.utils.http.filter.TestHttpFilter;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import javax.net.ssl.SSLContext;

import org.glassfish.grizzly.filterchain.FilterChain;
import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerBuilder {
    private static final Logger log = LoggerFactory.getLogger(ServerBuilder.class);

    private String base_uri;
    private String[] serverPackages;
    private HashMap<String, Object> property_map;
    private StaticHttpHandler webpageHandler;;
    private HttpServer webServer;
    private HashMap<String, String> static_page_path_map;
    private ArrayList<Class<?>> filter_list;
    private boolean useHttps = true;
    private String keystorePath;
    private String keystorePassword;

    public ServerBuilder() {
        serverPackages = new String[1];
        serverPackages[0] = "com.socialvagrancy.choixrecette.util.server";
        property_map = new HashMap<String, Object>();
        webpageHandler = null;
        static_page_path_map = null;
        filter_list = null;
    }

    //===========================================
    // Setters
    //===========================================
    public ServerBuilder addCertificate(String path, String password) {
        this.keystorePath = path;
        this.keystorePassword = password;
        return this;
    }

    public ServerBuilder addHttpFilter(String name) {
        if(filter_list == null) {
            filter_list = new ArrayList<Class<?>>();
        }

        switch(name) {
            case "cors":
                filter_list.add(CORSAuthorizationFilter.class);
                break;
            case "test":
                filter_list.add(TestHttpFilter.class);
                break;
            default:
                break;
        }
    
        return this;
    }
    
    public ServerBuilder addProperty(String name, Object property) {
        property_map.put(name, property);
        return this;
    }

    public ServerBuilder setConnectionHttp() {
        this.useHttps = false;
        return this;
    }

    public ServerBuilder setPackage(String... code) {
        serverPackages = code;
        return this;
    }

    public void addStaticWebPageDirectory(String resource_dir, String web_path) {
        if(static_page_path_map == null) {
            static_page_path_map = new HashMap<String, String>();
        }

        static_page_path_map.put(web_path, resource_dir);

    }

    public ServerBuilder uri(String ip, int port, String api_prefix) {
        String scheme = useHttps ? "https" : "http";
        base_uri = scheme + "://" + ip + ":" + port + "/" + api_prefix;
        return this;
    }

    public ServerBuilder uri(String ip, int port) {
        String scheme = useHttps ? "https" : "http";
        base_uri = scheme + "://" + ip + ":" + port + "/";
        return this;   
    }

    public ServerBuilder uri(String ip, String api_prefix) {
        String scheme = useHttps ? "https" : "http";
        base_uri = scheme + "://" + ip + "/" + api_prefix;
        return this;
    }    

    public ServerBuilder uri(String ip) {
        String scheme = useHttps ? "https" : "http";
        base_uri = scheme + "://" + ip + "/";
        return this;
    }

    //===========================================
    // Functions
    //===========================================
    public Server build() {
        log.debug("Building server.");
        Server server = new Server();

        String message;

        try {
            message = "Starting web server";
            log.info(message);
            System.err.println(message);
        
            webServer = startServer();
            configureServer();
            server.setServer(webServer);

            message = "Server started and listing at " + base_uri;
            log.warn(message);
            System.err.println(message);

            System.err.println("Press [enter] to stop the server...");
            System.in.read();

            message = "Shutting down the server.";
            log.warn(message);
            System.err.println(message);

            webServer.shutdown();

            message = "Server shutdown successfully.";
            log.warn(message);
            System.err.println(message);

            return server;
        } catch(Exception e) {
            System.err.println(e.getMessage());
            log.error(e.getMessage());
            log.error("Failed to start server");
    
            return null;
        }

    }

    private void configureServer() {
        // Post start configuration.
        // Check for static web pages.
        if(static_page_path_map != null) {
            for(String key : static_page_path_map.keySet()) {
                log.info("Server HTML pages from " + static_page_path_map.get(key) + " at path " + key);

                webpageHandler = new StaticHttpHandler(static_page_path_map.get(key));
                webServer.getServerConfiguration().addHttpHandler(webpageHandler, key);
            }
        }

    }
    
public HttpServer startServer() {
    final ResourceConfig config = new ResourceConfig()
        .packages(serverPackages)
        .addProperties(property_map);

    if(filter_list != null) {
        for(Class<?> filter : filter_list) {
            config.register(filter);
        }
    }

    URI uri = URI.create(base_uri);

    if(useHttps) {
        if(keystorePath == null || keystorePassword == null) {
            throw new IllegalStateException("HTTPS enabled, but keystore not provided. Call addCertificate().");
        }

        SSLContextConfigurator sslContextConfig = new SSLContextConfigurator();
        sslContextConfig.setKeyStoreFile(keystorePath);
        sslContextConfig.setKeyStorePass(keystorePassword);

        SSLContext sslContext = sslContextConfig.createSSLContext();

        SSLEngineConfigurator sslEngineConfig = new SSLEngineConfigurator(sslContext)
            .setClientMode(false)
            .setNeedClientAuth(false);

        return GrizzlyHttpServerFactory.createHttpServer(uri, config, true, sslEngineConfig);
    } else {
        return GrizzlyHttpServerFactory.createHttpServer(uri, config);
    }
}

}
