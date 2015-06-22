package com.db.dbx.mvc.controller;

import java.io.InputStream;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.db.dbx.gateway.DBSGatewayClient;
import com.db.dbx.gateway.GatewayDispatcher;
import com.db.dbx.model.DBSApplication;
import com.db.dbx.utilities.Utilities;

@Controller
public class ComponentsController {

	@RequestMapping(value="/_components/**", method=RequestMethod.GET)
	public String componentAssets(Model model, HttpServletRequest request, HttpServletResponse response) {
		String mapping = null;
		
		String appurl = Utilities.getAppURLFromRequest(request);

		try{
			boolean ishtml = false;
			String componentasseturl = Utilities.getLinkURLFromRequest(request);
			ishtml = componentasseturl.endsWith(".html");
			componentasseturl = Utilities.getEncodedURL(componentasseturl); //we encode the url so its treated as variable by camel route in dbs
			
			DBSGatewayClient client = new DBSGatewayClient();
			InputStream asset = client.performJSONGetAsBinary("/asset/" + appurl + "/" + componentasseturl);
			if(ishtml){
				String htmlcontent = IOUtils.toString(asset, "UTF-8"); 
				model.addAttribute("htmlcontent", htmlcontent);
				mapping = "dbxhtml";
			} else {
				model.addAttribute(asset);
				mapping = "dbxasset";
			}
			
		} catch(Exception ex){
			mapping = "404";  //ToDo throw a application 404, not a system 404
		}
			
		return mapping;
	}
}