package com.db.dbx.model;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

public class ApplicationJson {
	
	JsonNode applicationnode;
	
	public ApplicationJson(String applicationJSON) throws JsonProcessingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = mapper.readTree(applicationJSON);
		this.applicationnode = actualObj.findValue("application");
	}
	
	public String getTenantName(){
		JsonNode node = this.applicationnode.findValue("tenantName");
		return node.getValueAsText();
	}
	
	public String getName(){
		JsonNode node = this.applicationnode.findValue("name");
		return node.getValueAsText();
	}	
	
	public boolean isAllowAnnoymous(){
		JsonNode node = this.applicationnode.findValue("allowannoymous");
		return Boolean.parseBoolean(node.getValueAsText());
	}
	
	public String getJSON(){
		return this.applicationnode.toString();
	}
}
