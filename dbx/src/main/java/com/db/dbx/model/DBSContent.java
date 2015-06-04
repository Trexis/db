package com.db.dbx.model;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

public class DBSContent {
	
	JsonNode contentnode;
	
	public DBSContent(String contentJSON) throws JsonProcessingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = mapper.readTree(contentJSON);
		this.contentnode = actualObj.findValue("content");
	}
	
	public String getProperty(String property){
		JsonNode propertiesnode = this.contentnode.findValue("properties");
		JsonNode node = propertiesnode.findValue(property);
		return node.getValueAsText();
	}
	
	public String getContent(){
		return getProperty("data");
	}	
	
}
