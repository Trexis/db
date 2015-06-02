package com.db.dbx.gateway;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.db.dbx.common.DBProperties;


public class GatewayDispatcher {
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private DBProperties dbproperties;
	
	public GatewayDispatcher(HttpServletRequest request, HttpServletResponse response) throws Exception{
		this.request = request;
		this.response = response;
		this.dbproperties = new DBProperties();
	}
	
	public String DispatchDBS(String URI) throws ServletException, IOException{
		String dbscontext = dbproperties.getProperty("db.gateway.dbs.context");
		String dbstoken = dbproperties.getProperty("db.gateway.dbs.token");
		
		response = new DispatchServletResponseWrapper(response);
		request = new AuthorizeServletRequestWrapper(request, dbstoken);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/services/" + dbscontext + URI);
		dispatcher.forward(request, response);

		String output = response.getOutputStream().toString();
		//reset the request, since we will build it up from the response later
		response.reset();

		return output;
	}
}
