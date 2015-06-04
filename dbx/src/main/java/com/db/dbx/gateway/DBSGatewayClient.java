package com.db.dbx.gateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import com.db.dbx.common.DBProperties;


public class DBSGatewayClient extends HttpClientBuilder {

	CloseableHttpClient httpclient;
	DBProperties dbproperties;
	String dbsurl = "";
	String dbscontext = "";
	String dbstoken = "";
	
	public DBSGatewayClient() throws Exception {
		super();
		httpclient = this.build();
		dbproperties = new DBProperties();
		dbsurl = dbproperties.getProperty("db.gateway.url");
		dbscontext = dbproperties.getProperty("db.gateway.dbs.context");
		dbstoken = dbproperties.getProperty("db.gateway.dbs.token");

	}
	
	
	//GET
	public String performJSONGetAsString(String relativeURL) throws Exception {
        
        Header[] headers = {	
        	new BasicHeader("Authorization", "Bearer " + dbstoken)
        };
        
		return performJSONGetAsString(relativeURL, headers);
	}
	
	public String performJSONGetAsString(String relativeURL, Header[] headers) throws Exception {
		String url = dbsurl + dbscontext + relativeURL;
		
		HttpGet request = new HttpGet(url);
		if(headers!=null) request.setHeaders(headers);

		return getStringResponse(request);
	}
	
	
	//POST
	public String performJSONPostAsString(String relativeURL, String jsonPostData) throws Exception {
		StringEntity entity = new StringEntity(jsonPostData, "UTF-8");
        entity.setContentType("application/json;charset=UTF-8"); //text/plain;charset=UTF-8
        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
        
        Header[] headers = {	
        	new BasicHeader("Authorization", "Bearer " + dbstoken)
        };
        
		return performJSONPostAsString(relativeURL, entity, headers);
	}
	
	public String performJSONPostAsString(String relativeURL, StringEntity entity, Header[] headers) throws Exception {

		String url = dbsurl + dbscontext + relativeURL;
		
		HttpPost request = new HttpPost(url);
		if(headers!=null) request.setHeaders(headers);
        request.setEntity(entity); 

		return getStringResponse(request);
	}
	
	
	/*
	 * PRIVATE METHODS
	 */
	
	private byte[] getBinaryResponse(HttpUriRequest request) throws Exception{
		byte[] response = null;

		try {
			HttpResponse httpresponse = httpclient.execute(request);
			int statusCode = httpresponse.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw new Exception(httpresponse.getStatusLine().getReasonPhrase());
			}
			
			if(statusCode != HttpStatus.SC_NO_CONTENT) {
				InputStream is = httpresponse.getEntity().getContent();
				response = IOUtils.toByteArray(is);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Fatal transport error: " + e.getMessage(), e);
		} finally {
			request.abort();
		}  
		
		return response;
	}
	
	private String getStringResponse(HttpUriRequest request) throws Exception{
		String response = "";

		try {
			HttpResponse httpresponse = httpclient.execute(request);
			int statusCode = httpresponse.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw new Exception(httpresponse.getStatusLine().getReasonPhrase());
			}
			
			if(statusCode != HttpStatus.SC_NO_CONTENT) {
				response = readResponse(httpresponse);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Fatal transport error: " + e.getMessage(), e);
		} finally {
			request.abort();
		}  
		
		return response;
	}


	/****************************
	 * UTILITIES  
	 ****************************/
	
	private String readResponse(HttpResponse httpResponse) throws IllegalStateException, IOException{
		String htmlresponse = "";
		BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
		String line = "";
		while ((line = rd.readLine()) != null) {
			htmlresponse += line;
		}		
		return htmlresponse;
	}

}
