package com.db.dbx.mvc.controller;

import java.io.InputStream;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class ThemeController {

	@RequestMapping(value="/_themes/**", method=RequestMethod.GET)
	public String model(Model model, HttpServletRequest request, HttpServletResponse response) {
		String mapping = null;
		
		String appurl = Utilities.getAppURLFromRequest(request);

		try{
			String themeasseturl = Utilities.getLinkURLFromRequest(request);
			themeasseturl = Utilities.getEncodedURL(themeasseturl); //we encode the url so its treated as variable by camel route in dbs
			
			DBSGatewayClient client = new DBSGatewayClient();
			InputStream asset = client.performJSONGetAsBinary("/theme/" + appurl + "/" + themeasseturl);
			model.addAttribute(asset);
			mapping = "dbxthemeasset";
		} catch(Exception ex){
			mapping = "404";  //ToDo throw a application 404, not a system 404
		}
			
		return mapping;
	}
}