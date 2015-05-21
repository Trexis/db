package com.db.dbx.service;

import java.io.IOException;

import javax.inject.Inject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import com.db.dbx.controller.ModelContext;
import com.db.dbx.model.Application;

@Service
public class JSONModel {

	@Inject
	private ModelContext modelContext;
	
	public JSONModel(){
		
	}
	
	public String getApplicationJsonModel(Application application){
		ObjectMapper objectmapper = new ObjectMapper();
		String jsonstring = "{}";
		try {
			jsonstring = objectmapper.writeValueAsString(application);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "{\"application\": " + jsonstring + "}";
	}
}
