package com.db.dbx.model;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

public class DBSLink {
	
	JsonNode linknode;
	
	public DBSLink(String linkJSON) throws JsonProcessingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = mapper.readTree(linkJSON);
		this.linknode = actualObj.findValue("link");
	}
	
	public String getTenantName(){
		JsonNode node = this.linknode.findValue("tenantname");
		return node.getValueAsText();
	}
	
	public String getApplicationName(){
		JsonNode node = this.linknode.findValue("appname");
		return node.getValueAsText();
	}	
	
	public String getPageName(){
		JsonNode node = this.linknode.findValue("pagename");
		return node.getValueAsText();
	}	

	public String getName(){
		JsonNode node = this.linknode.findValue("name");
		return node.getValueAsText();
	}	
}
