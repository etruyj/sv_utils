package com.socialvagrancy.utils.http;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.StringBuilder;
import java.net.URL;
import java.net.HttpURLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class RestApi
{
	public RestApi(boolean ignore_ssl)
	{
		if(ignore_ssl)
		{
			// Ignore invalid SSL Certificates
			// 	Create and install a TrustManager that 
			// 	accepts all SSL certificates.
			TrustManager[] trustAllCerts = new TrustManager[] { 
				new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
					public void checkClientTrusted(X509Certificate[] certs, String authType) {}
					public void checkServerTrusted(X509Certificate[] certs, String authType) {}
				}
			};

			// Install the all-trusting trust manager
			try
			{
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		
				// Create all-trusting host name verifier
				HostnameVerifier allHostsValid = new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) { return true; }
				};

				// Install the all-trustng host verifier
				HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
	}

	public HttpResponse authenticate(String httpRequest, String body) 
	{
	    return request("POST", httpRequest, "NO_AUTH", null, body);
	}
	
	public HttpResponse delete(String httpRequest, String token) 
	{
	    return request("DELETE", httpRequest, "Authorization", token, null);
	}

	public HttpResponse get(String httpRequest) 
	{
	    return request("GET", httpRequest, "NO_AUTH", null, null);
    }
	
	public HttpResponse get(String httpRequest, String token) 
	{
	    return request("GET", httpRequest, "Authorization", token, null);
    }
	
	public HttpResponse get(String httpRequest, String token, String auth_header) 
	{
        return request("GET", httpRequest, auth_header, token, null);
	}
	
	public HttpResponse post(String httpRequest, String token, String auth_header, String body) 
	{
	    return request("POST", httpRequest, auth_header, token, body);
    }
	
	public HttpResponse post(String httpRequest, String token, String body) 
	{
	    return request("POST", httpRequest, "Authorization", token, body);
    }
	
	public HttpResponse put(String httpRequest, String token, String body)
	{
	    return request("PUT", httpRequest, "Authorization", token, body);
	}

	//=======================================
	// Private Functions
	//=======================================
	
	private String parseUrl(String url)
	{
		return url.replace(" ", "%20");
	}
	
    private HttpResponse request(String method, String httpRequest, String auth_type, String token, String body)
	{
		StringBuilder response = new StringBuilder();
		int code = 0;
        String message = "";

		// Open connection		
		// Remove spaces from httpRequest
		httpRequest = parseUrl(httpRequest);
		
        try {    
		    URL url = new URL(httpRequest);
		    HttpURLConnection cxn = (HttpURLConnection) url.openConnection();
		
		    // Configuration the connection
		    cxn.setRequestMethod(method);
		    cxn.setDoOutput(true);
		    cxn.setRequestProperty("Content-Type", "application/json");
		    cxn.setRequestProperty("Accept", "application/json");
		   
            if(!auth_type.equals("NO_AUTH")) {
                cxn.setRequestProperty(auth_type, token);
            }

		    if(body != null && body.length() > 0)
		    {
		    	OutputStream output = cxn.getOutputStream();
			    byte[] input = body.getBytes("utf-8");
			    output.write(input, 0, input.length);
		    }
	
            code = cxn.getResponseCode();
            message = cxn.getResponseMessage();

		    // Read response	
		    BufferedReader br = null;
            if(100<=code && code<=399) {
                br = new BufferedReader(new InputStreamReader(cxn.getInputStream(), "utf-8"));
            }
            else
            {
                br = new BufferedReader(new InputStreamReader(cxn.getErrorStream(), "utf-8"));
            }

		    String responseLine = null;

		    while((responseLine = br.readLine()) != null)
		    {
			    response.append(responseLine);
		    }
        } catch(FileNotFoundException e) {
            System.err.println("[" + code + "] " + e.getMessage());
        } catch(IOException e) {
            System.err.println("[" + code + "][IOException] " + e.getMessage());
        }

		HttpResponse http_response = new HttpResponse();
        
        http_response.setStatusCode(code);
        http_response.setResponseMessage(message);
        http_response.setBody(response.toString());

        return http_response;
	}
}
