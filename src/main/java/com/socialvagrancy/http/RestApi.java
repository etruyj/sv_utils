package com.socialvagrancy.utils.http;

import java.io.BufferedReader;
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

import org.apache.hc.client5.http.HttpResponseException;

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

	public String authenticate(String httpRequest, String body) throws Exception
	{
	    return request("POST", httpRequest, "NO_AUTH", null, body);
	}
	
	public String delete(String httpRequest, String token) throws Exception
	{
	    return request("DELETE", httpRequest, "Authorization", token, null);
	}

	public String get(String httpRequest) throws Exception
	{
	    return request("GET", httpRequest, "NO_AUTH", null, null);
    }
	
	public String get(String httpRequest, String token) throws Exception
	{
	    return request("GET", httpRequest, "Authorization", token, null);
    }
	
	public String get(String httpRequest, String token, String auth_header) throws Exception
	{
        return request("GET", httpRequest, auth_header, token, null);
	}
	
	public String post(String httpRequest, String token, String auth_header, String body) throws Exception
	{
	    return request("POST", httpRequest, auth_header, token, body);
    }
	
	public String post(String httpRequest, String token, String body) throws Exception
	{
	    return request("POST", httpRequest, "Authorization", token, body);
    }
	
	public String put(String httpRequest, String token, String body) throws Exception
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
	
    private String request(String method, String httpRequest, String auth_type, String token, String body)
	{
		StringBuilder response = new StringBuilder();
		
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
			
		    // Read response	
		    BufferedReader br = new BufferedReader(new InputStreamReader(cxn.getInputStream(), "utf-8"));

		    String responseLine = null;

		    while((responseLine = br.readLine()) != null)
		    {
			    response.append(responseLine);
		    }
        } catch(HttpResponseException e) {
            System.err.println("[" + e.getStatusCode() + "] " + e.getMessage());
        } catch(IOException e) {
            System.err.println("[IOException] " + e.getMessage());
        }

		return response.toString();
	}
}
