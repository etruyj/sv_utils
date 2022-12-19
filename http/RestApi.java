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

		// Query library without a header (authorization)
		// mostly just for logging in.

		StringBuilder response = new StringBuilder();
		
		// Open connection		
		try
		{
			URL url = new URL(httpRequest);
			HttpURLConnection cxn = (HttpURLConnection) url.openConnection();
		
			// Configuration the connection
			cxn.setRequestMethod("POST");
			cxn.setDoOutput(true);
			cxn.setRequestProperty("Content-Type", "application/json");
			cxn.setRequestProperty("Accept", "application/json");
	
			OutputStream output = cxn.getOutputStream();
			byte[] input = body.getBytes("utf-8");
			output.write(input, 0, input.length);
			
			
			// Read response	
			BufferedReader br = new BufferedReader(new InputStreamReader(cxn.getInputStream(), "utf-8"));

			String responseLine = null;

			while((responseLine = br.readLine()) != null)
			{
				response.append(responseLine);
			}
		}
		catch(Exception e)
		{	
			throw new Exception(e.getMessage());
		}

		return response.toString();
	}
	
/*	public String basicAuth(String httpRequest, String username, char[] password) throws Exception
	{

		// Query library without a header (authorization)
		// mostly just for logging in.

		StringBuilder response = new StringBuilder();
		
		// Open connection		
		try
		{
			URL url = new URL(httpRequest);
			HttpURLConnection cxn = (HttpURLConnection) url.openConnection();
		
			// Configuration the connection
			cxn.setRequestMethod("POST");
			cxn.setDoOutput(true);
			cxn.setRequestProperty("Content-Type", "application/json; utf-8");
			cxn.setRequestProperty("Accept", "application/json");
	
			OutputStream output = cxn.getOutputStream();
			byte[] input = body.getBytes("utf-8");
			output.write(input, 0, input.length);
			
			
			// Read response	
			BufferedReader br = new BufferedReader(new InputStreamReader(cxn.getInputStream(), "utf-8"));

			String responseLine = null;

			while((responseLine = br.readLine()) != null)
			{
				response.append(responseLine);
			}
		}
		catch(Exception e)
		{	
			throw new Exception(e.getMessage());
		}

		return response.toString();
	}
*/	
	public String delete(String httpRequest, String token) throws Exception
	{
		// Send DELETE command to the server.

		StringBuilder response = new StringBuilder();
		int response_code = 0;

		// Open connection		
		try
		{
			URL url = new URL(httpRequest);
			HttpURLConnection cxn = (HttpURLConnection) url.openConnection();
		
			// Configuration the connection
			cxn.setRequestMethod("DELETE");
			cxn.setDoOutput(true);
			cxn.setRequestProperty("Content-Type", "application/json");
			cxn.setRequestProperty("Accept", "application/json");
			cxn.setRequestProperty("Authorization", token);

			// GET response code.
			response_code = cxn.getResponseCode();

			// Read response	
			BufferedReader br = new BufferedReader(new InputStreamReader(cxn.getInputStream(), "utf-8"));

			String responseLine = null;

			while((responseLine = br.readLine()) != null)
			{
				response.append(responseLine);
			}
		}
		catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}

		// Check to see if there is a response.
		// If not, return the response code instead.
		if(response.length()==0)
		{
			throw new Exception("[" + response_code + "] Failed to delete.");
		}
		else
		{
			return response.toString();
		}
	}

	public String get(String httpRequest) throws Exception
	{
		// Query library with a header (authorization)
		// mostly just for logging in.
		StringBuilder response = new StringBuilder();
		int response_code = 0;

		// Open connection		
		try
		{
			URL url = new URL(httpRequest);
			HttpURLConnection cxn = (HttpURLConnection) url.openConnection();
		
			// Configuration the connection
			cxn.setRequestMethod("GET");
			cxn.setDoOutput(true);
			cxn.setRequestProperty("Content-Type", "application/json");
			cxn.setRequestProperty("Accept", "application/json");
			
			// GET response code.
			response_code = cxn.getResponseCode();
			
			// Read response	
			BufferedReader br = new BufferedReader(new InputStreamReader(cxn.getInputStream(), "utf-8"));

			String responseLine = null;

			while((responseLine = br.readLine()) != null)
			{
				response.append(responseLine);
			}
		}
		catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}

		return response.toString();
	}
	
	public String get(String httpRequest, String token) throws Exception
	{
		// Query library with a header (authorization)
		// mostly just for logging in.

		StringBuilder response = new StringBuilder();
		int response_code = 0;

		// Open connection		
		try
		{
			URL url = new URL(httpRequest);
			HttpURLConnection cxn = (HttpURLConnection) url.openConnection();
		
			// Configuration the connection
			cxn.setRequestMethod("GET");
			cxn.setDoOutput(true);
			cxn.setRequestProperty("Content-Type", "application/json");
			cxn.setRequestProperty("Accept", "application/json");
			cxn.setRequestProperty("Authorization", token);
			
			// GET response code.
			response_code = cxn.getResponseCode();

			// Read response	
			BufferedReader br = new BufferedReader(new InputStreamReader(cxn.getInputStream(), "utf-8"));

			String responseLine = null;

			while((responseLine = br.readLine()) != null)
			{
				response.append(responseLine);
			}
		}
		catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}

		// Check to see if there is a response.
		// If not, return the response code instead.
		if(token.equals("code"))
		{
			throw new Exception("[" + response_code + "] Unable to complete GET request.");
		}
		else
		{
			return response.toString();
		}
	}
	
	public String post(String httpRequest, String token, String body) throws Exception
	{
		// Query library without a header (authorization)
		// mostly just for logging in.

		StringBuilder response = new StringBuilder();
		
		// Open connection		
		try
		{
			URL url = new URL(httpRequest);
			HttpURLConnection cxn = (HttpURLConnection) url.openConnection();
		
			// Configuration the connection
			cxn.setRequestMethod("POST");
			cxn.setDoOutput(true);
			cxn.setRequestProperty("Content-Type", "application/json");
			cxn.setRequestProperty("Accept", "application/json");
			cxn.setRequestProperty("Authorization", token);

			if(body.length() > 0)
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
		}
		catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}

		return response.toString();
	}
	
	public String put(String httpRequest, String token, String body) throws Exception
	{
		// Query library without a header (authorization)
		// mostly just for logging in.

		StringBuilder response = new StringBuilder();
		
		// Open connection		
		try
		{
			URL url = new URL(httpRequest);
			HttpURLConnection cxn = (HttpURLConnection) url.openConnection();
		
			// Configuration the connection
			cxn.setRequestMethod("PUT");
			cxn.setDoOutput(true);
			cxn.setRequestProperty("Content-Type", "application/json");
			cxn.setRequestProperty("Accept", "application/json");
			cxn.setRequestProperty("Authorization", token);

			if(body.length() > 0)
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
		}
		catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}

		return response.toString();
	}
	
}
