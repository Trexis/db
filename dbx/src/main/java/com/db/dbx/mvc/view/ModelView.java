package com.db.dbx.mvc.view;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

import com.db.dbx.enums.StatusCode;
import com.db.dbx.gateway.GatewayDispatcher;
import com.db.dbx.utilities.Utilities;

public class ModelView implements View {
	
	public String getContentType() {
		
		return "application/json";
	}

	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		try{
			String appurl = Utilities.getAppURLFromRequest(request);
			String viewname = model.get("viewname").toString();
			
			GatewayDispatcher gateway = new GatewayDispatcher(request, response);
			
			String jsonmodel = "{}";
			if(viewname.equals("model")){
				jsonmodel = gateway.DispatchDBS("/model/" + appurl);
			}
			if(viewname.equals("model_component")){
				jsonmodel = gateway.DispatchDBS("/model/" + appurl + "/page/" + model.get("pageName") + "/component/" + model.get("componentName"));
			}
			if(viewname.equals("model_page")){
				jsonmodel = gateway.DispatchDBS("/model/" + appurl + "/page/" + model.get("pageName"));
			}
			
			Utilities.printJSONToResponse(response, jsonmodel);
			if(Utilities.validateDBSResponseJSON(jsonmodel)){
				response.setStatus(200);
			} else {
				response.setStatus(500);
			}
			
		} catch(Exception ex){
			String responsejson = Utilities.wrapResponseWithDefaultJSON(StatusCode.Error, Utilities.convertObjectToJSON(ex));
			Utilities.printJSONToResponse(response, responsejson);
			response.setStatus(500);
		}
	}

}
