//====================================================================
// CORSAuthorizationFilter
//      Description
//          A Jakarta filter to add CORS headers to HTTP responses,
//          allowing cross-origin resource sharing for specified domains.
//          This implementation is suitable for use with applications
//          requiring token-based authorization.
//
// Created by etruyj
//====================================================================

package com.socialvagrancy.utils.http.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;

import java.util.Arrays;
import java.util.List;

public class CORSAuthorizationFilter implements ContainerResponseFilter {
    @Context
    Configuration config; 
    
    
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();

        List<String> origins = requestContext.getHeaders().get("Origin");
        String origin = null;

        // Grab the last origin as CORS only allows 1 origin to be returned.
        if(origins != null) {
            for(String o : origins) {
                origin = o;
            }
        }

        if(((List<String>) config.getProperty("allowed_origins")).contains(origin)) {
            headers.add("Access-Control-Allow-Origin", origin);
            headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
            headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
            headers.add("Access-Control-Allow-Credentials", "true");
        }
    }
}
