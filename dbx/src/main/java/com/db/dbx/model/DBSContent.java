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
		String nodevalue = null;
		JsonNode propertiesnode = this.contentnode.findValue("properties");
		if(propertiesnode!=null){
			JsonNode node = propertiesnode.findValue(property);
			nodevalue = node.getValueAsText();
		}
		return nodevalue;
	}
	
	public String getContent(){
		return getProperty("data");
	}	
	
}
