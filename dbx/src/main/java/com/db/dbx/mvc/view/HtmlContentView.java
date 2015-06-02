package com.db.dbx.mvc.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

import com.db.dbx.utilities.Utilities;

public class HtmlContentView implements View {
	
	public String getContentType() {
		
		return "text/html";
	}

	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		try{
			String htmlcontent = (String) model.get("htmlcontent");
			
			htmlcontent = Utilities.resolveHTMLVariables(request, htmlcontent);
			
			Utilities.printHTMLToResponse(response, htmlcontent);
			response.setStatus(200);
			
		} catch(Exception ex){
			response.setStatus(500);
		}
	}

}
