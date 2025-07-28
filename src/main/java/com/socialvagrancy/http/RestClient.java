//=================================================================
// RestClient.java
//	Description:
//		This class handles RESTful requests to the web using
//		the Apache HttpClient
//
// Created by Sean Snyder
//=================================================================

package com.socialvagrancy.utils.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.StringBuilder;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class RestClient
{
    private HttpClient http_client;

	public RestClient(boolean ignore_ssl)
	{
        if(ignore_ssl) {
		    // Turn off SSL certification since the SSL certificate on
		    // these libraries is always flagged.
		    TrustManager[] trustAllCerts = new TrustManager[] { 
			    new X509TrustManager() {
				    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
				    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
				    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
			    }
		    };
		
            // Install the all-trusting trust manager

		    try
		    {
                SSLContext sc = SSLContext.getInstance("TLS");
			    sc.init(null, trustAllCerts, new java.security.SecureRandom());
		
		        http_client = HttpClients.custom()
                                .setSSLSocketFactory(new SSLConnectionSocketFactory(sc, NoopHostnameVerifier.INSTANCE))
                                .build();
        
            }
		    catch(Exception e)
		    {
			    System.out.println(e.getMessage());
		    }
        } else {
            http_client = HttpClients.createDefault();
        }
	}

    //===========================================
    // API Calls
    //===========================================
    public HttpResponse delete(String httpRequest, String token) throws IOException {
        return delete(httpRequest, "Authorization", token);
    }
    

    public HttpResponse delete(String httpRequest, String auth, String token) throws IOException {
        HttpDelete api_call = new HttpDelete(httpRequest);
        
        api_call.setHeader("Content-Type", "application/json");
        api_call.setHeader("Accepts", "application/json");
        api_call.setHeader(auth, token);
        
        HttpResponse response = http_client.execute(api_call);
        
        return response;
    }

    public HttpResponse get(String httpRequest) throws IOException {
        return get(httpRequest, null, null); // Get without authentication
    }

    public HttpResponse get(String httpRequest, String token) throws IOException {
        return get(httpRequest, "Authorization", token);
    }

    public HttpResponse get(String httpRequest, String auth, String token) throws IOException {
        HttpGet api_call = new HttpGet(httpRequest);
        
        api_call.setHeader("Content-Type", "application/json");
        api_call.setHeader("Accepts", "application/json");
        if(auth != null) api_call.setHeader(auth, token); // only set the authorization header if it's not null.

        HttpResponse response = http_client.execute(api_call);

        return response;
    }

	public HttpResponse patch(String httpRequest, String token, String body) throws IOException {
        return patch(httpRequest, "Authorization", token, body);
    }

	public HttpResponse patch(String httpRequest, String auth, String token, String body) throws IOException {
		// Open connection		
		HttpResponse response = null;
        
        HttpPatch request = new HttpPatch(httpRequest);
        request.setHeader("Content-Type", "application/merge-patch+json");
        request.setHeader("Accepts", "application/json");
        request.setHeader(auth, token);
        request.setEntity(new StringEntity(body));

        response = http_client.execute(request);
		    
        return response;
	}
	
    public HttpResponse post(String httpRequest, String body) throws IOException {
        // API call without a token, i.e. login in requests
        return post(httpRequest, "Authorization", null, body);
    }

    public HttpResponse post(String httpRequest, String token, String body) throws IOException {
        // API call without a token, i.e. login in requests
        return post(httpRequest, "Authorization", token, body);
    }

    public HttpResponse post(String httpRequest, String auth, String token, String body) throws IOException {
        HttpPost api_call = new HttpPost(httpRequest);

        api_call.setHeader("Content-Type", "application/json");
        api_call.setHeader("Accepts", "application/json");
        
        if(token != null) {
            api_call.setHeader(auth, token);
        }

        if(body != null && body != "") {
            api_call.setEntity(new StringEntity(body));
        }

        HttpResponse response = http_client.execute(api_call);

        return response;
    }
	
    public HttpResponse put(String httpRequest, String token, String body) throws IOException {
        return put(httpRequest, "Authorization", token, body);
    }

    public HttpResponse put(String httpRequest, String auth, String token, String body) throws IOException {
        HttpPut api_call = new HttpPut(httpRequest);

        api_call.setHeader("Content-Type", "application/json");
        api_call.setHeader("Accept", "application/json");
        api_call.setHeader(auth, token);
        api_call.setEntity(new StringEntity(body));

        HttpResponse response = http_client.execute(api_call);

        return response;
    }
}
