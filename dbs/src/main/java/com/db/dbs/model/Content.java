package com.db.dbs.model;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.db.dbs.utilities.Utilities;

public class Content {

	Node contentnode;
	String content;
	
	public Content(Node jcrContentNode){
		this.contentnode = jcrContentNode;
		
	}

	public Map<String, String> getProperties() throws ValueFormatException, RepositoryException{
		Map<String, String> properties = new HashMap<String, String>();
		Iterable<Property> propertiesarry = JcrUtils.getProperties(this.contentnode);
		for(Property property: propertiesarry){
			String propvalue = "";
			if(property.getType()==PropertyType.BINARY){
				try {
					Binary binary = property.getBinary();
					propvalue = IOUtils.toString(binary.getStream());
					properties.put("size", String.valueOf(binary.getSize()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if(property.getType()==PropertyType.STRING) {
				propvalue = property.getString();
			} else if(property.getType()==PropertyType.DATE) {
				propvalue = property.getDate().getTime().toString();
			} else if(property.getType()==PropertyType.BOOLEAN) {
				propvalue = String.valueOf(property.getBoolean());
			} else if(property.getType()==PropertyType.DOUBLE) {
				propvalue = String.valueOf(property.getDouble());
			} else if(property.getType()==PropertyType.NAME) {
				propvalue = property.getName();
			}
			
			properties.put(property.getName().replace("jcr:", ""), 	propvalue);
		}
		return properties;
	}
	
	@JsonIgnore
	public String toJson(){
		String jsonstring = Utilities.convertObjectToJSON(this);
		return "{\"content\": " + jsonstring + "}";
	}
	
}
