package com.db.dbx.mvc.view;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

import com.db.dbx.enums.StatusCode;
import com.db.dbx.model.Application;
import com.db.dbx.model.Page;
import com.db.dbx.service.JSONModel;
import com.db.dbx.utilities.ScriptReader;
import com.db.dbx.utilities.Utils;

public class ModelView implements View {
	
	@Inject JSONModel jsonModel;


	public String getContentType() {
		
		return null;
	}

	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		try{
			Application application = (Application) model.get("application");
			String serverjsonmodel = jsonModel.getApplicationJsonModel(application);
			
			String responsejson = Utils.wrapResponseWithDefaultJSON(StatusCode.Success, serverjsonmodel);
			Utils.printJSONToResponse(response, responsejson);
			response.setStatus(200);
			
		} catch(Exception ex){
			String responsejson = Utils.wrapResponseWithDefaultJSON(StatusCode.Error, Utils.convertObjectToJSON(ex));
			Utils.printJSONToResponse(response, responsejson);
			response.setStatus(500);
		}
	}

}
