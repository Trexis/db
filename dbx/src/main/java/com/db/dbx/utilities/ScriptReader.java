package com.db.dbx.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

public class ScriptReader {
	public static String ReadDBXScript(HttpServletRequest request, String serverModel){
		Map<String, String> mergefields = new HashMap<String,String>();
		mergefields.put("contextPath", request.getContextPath());
		mergefields.put("serverModel", serverModel);
		
		return parseScriptFile("dbx.html", mergefields);	
	}
	
	public static String ReadPageScript(HttpServletRequest request, String serverModel){
		Map<String, String> mergefields = new HashMap<String,String>();
		mergefields.put("contextPath", request.getContextPath());
		mergefields.put("serverModel", serverModel);
		
		return parseScriptFile("page.html", mergefields);
	}
	
	private static String parseScriptFile(String fileName, Map<String, String> mergeFields){
		String response = "";
		
		try{
			response = readScriptFile(fileName);
			for(String key: mergeFields.keySet()){
				response = response.replaceAll("#" + key + "#", mergeFields.get(key));
			}
			
		} catch(Exception ex){
			response = "<!-- ERROR OCCURED LOADING SCRIPT FOR DBX Page -->";
		}
		
		return response;
	}
	
	private static String readScriptFile(String filename) throws IOException{
		InputStream in = ScriptReader.class.getClassLoader().getResourceAsStream("/scripts/" + filename);
		
		StringWriter writer = new StringWriter();
		IOUtils.copy(in, writer, "UTF-8");
		return writer.toString();			
	}
}
