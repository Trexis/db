package com.db.dbx.utilities;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.web.csrf.CsrfToken;

import com.db.dbx.enums.StatusCode;

public class Utilities {

	/*
	 *  RESPONSE FUNCTIONS
	 */
	
	public static Boolean validateDBSResponseJSON(String responseJSON){
		return !responseJSON.contains("\"status\": \"error\"");
	}
	
	public static String wrapResponseWithDefaultJSON(StatusCode statusCode, String outputJson){
		String response = "{ \"status\": \"" + statusCode.toString().toLowerCase() + "\", ";
		response += "\"data\": " + outputJson;
		response += "}";
		return response;
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

	public static String resolveHTMLVariables(HttpServletRequest request, String outputHTML){
		String response = outputHTML;
		CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
		if(token!=null){
			response = response.replaceAll("[$][\\{]_csrf.token}", token.getToken());
		}
		response = response.replaceAll("[$][\\{]contextPath}", request.getContextPath());
		return response;
	}

	
	public static void printJSONToResponse(HttpServletResponse response, String jsonContent) throws IOException{
		response.setContentType("application/json");
		response.getOutputStream().print(jsonContent);
	}

	public static void printHTMLToResponse(HttpServletResponse response, String htmlContent) throws IOException{
		response.setContentType("text/html");
		response.getOutputStream().print(htmlContent);
	}
	
	/*
	 * URL METHODS
	 */
	//http://bank1.db.com:8080/dbx/sitea/
	//getSchema: http
	//getServerName: bank1.db.com
	//getRequestURI: /dbx/sitea/
	//getServerPort:  8080
	//getContextPath: /dbx
	//getServletPath: /sitea/
	
	public static String getAppURLFromRequest(HttpServletRequest request){
		return request.getServerName();
	}
	public static String getLinkURLFromRequest(HttpServletRequest request){
		return request.getServletPath();
	}

	public static String getEncodedURL(String url){
		byte[] encodedBytes = Base64.encodeBase64(url.getBytes());
		return new String(encodedBytes);
	}

	public static String getDecodedURL(String encodedUrl){
		byte[] decodedBytes = Base64.decodeBase64(encodedUrl.getBytes());
		return new String(decodedBytes);
	}

}
