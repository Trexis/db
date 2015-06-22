package com.db.dbx.mvc.view;

import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.servlet.View;

import com.db.dbx.utilities.Utilities;

public class AssetView implements View {
	
	public String getContentType() {
		
		return "text/html";
	}

	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		try{
			InputStream asset = (InputStream) model.get("byteArrayInputStream");
			IOUtils.copy(asset, response.getOutputStream());
			response.setStatus(200);
			
		} catch(Exception ex){
			response.setStatus(500);
		}
	}

}
