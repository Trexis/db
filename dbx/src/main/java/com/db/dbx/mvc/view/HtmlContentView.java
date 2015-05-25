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

public class HtmlContentView implements View {
	
	public String getContentType() {
		
		return "text/html";
	}

	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		try{
			Application application = (Application) model.get("application");
			String htmlcontent = (String) model.get("htmlcontent");
			
			Utils.printHTMLToResponse(response, htmlcontent);
			response.setStatus(200);
			
		} catch(Exception ex){
			response.setStatus(500);
		}
	}

}
