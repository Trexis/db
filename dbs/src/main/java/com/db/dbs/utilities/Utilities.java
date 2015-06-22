package com.db.dbs.utilities;

import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.db.dbs.enums.StatusCode;

public class Utilities {

	public static String getEncodedURL(String url){
		byte[] encodedBytes = Base64.encodeBase64(url.getBytes());
		return new String(encodedBytes);
	}

	public static String getDecodedURL(String encodedUrl){
		byte[] decodedBytes = Base64.decodeBase64(encodedUrl.getBytes());
		return new String(decodedBytes);
	}
	
	public static String appendValueToURL(String url, String value){
		String responseurl = url;
		if(!responseurl.endsWith("/")) responseurl += "/";
		responseurl += value;
		return responseurl;
	}
	
	public static void setJSONOutput(Exchange exchange, StatusCode statusCode, String json){
		
		String responsejson = wrapResponseWithDefaultJSON(statusCode, json);

		Message message = exchange.getOut();
		message.setHeader("Content-type", "application/json; charset=utf-8");
		message.setBody(responsejson);
		message.setFault(statusCode.equals(StatusCode.Error));
	}
	
	//should we throw a exception here maybe?
	public static String convertObjectToJSON(Object object){
		ObjectMapper objectmapper = new ObjectMapper();
		String jsonstring = "{}";
		try {
			jsonstring = objectmapper.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonstring;
	}
	
	public static String wrapResponseWithDefaultJSON(StatusCode statusCode, String outputJson){
		String response = "{ \"status\": \"" + statusCode.toString().toLowerCase() + "\", ";
		response += "\"data\": " + outputJson;
		response += "}";
		return response;
	}
	
	public static String getInHeader(Exchange exchange, String headerName){
		if(exchange.getIn().getHeaders().containsKey(headerName)){
			return exchange.getIn().getHeader(headerName).toString();
		} else {
			return null;
		}
	}
}
