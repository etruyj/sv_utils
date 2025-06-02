//===================================================================
// TestHttpFilter
//      Description:
//          This filter is for testing filter application. Its sole 
//          function is to log a string of text to prove the filters
//          are working.
//
// Created by etruyj
//===================================================================

package com.socialvagrancy.utils.http.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Provider
public class TestHttpFilter
implements ContainerResponseFilter {
    public static final Logger log = LoggerFactory.getLogger(TestHttpFilter.class);

	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {

            log.error("Filter TEST");
	}

}
