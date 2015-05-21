package com.db.dbx.utilities;

import javax.servlet.http.HttpServletRequest;

public class Utils {

	
	/*
	 * URL METHODS
	 */
	//http://bank1.db.com:8080/dbx/sitea/
	//getSchema: http
	//getServerName: bank1.db.com
	//getRequestURI: /dbx/sitea/
	//getServerPort:  8080
	//getContextPath: /dbx
	//getServletPath: /sitea/
	
	public static String getAppURLFromRequest(HttpServletRequest request){
		return request.getServerName();
	}
	public static String getLinkURLFromRequest(HttpServletRequest request){
		return request.getServletPath();
	}

}
