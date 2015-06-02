package com.db.dbx.gateway;

import java.util.Enumeration;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpRequest;
import org.mitre.dsmiley.httpproxy.ProxyServlet;

import com.db.dbx.common.DBProperties;

public class GatewayServlet extends ProxyServlet{

	
	@Override
	protected String getConfigParam(String key)
	{
		if(key.equals("targetUri")){
			try {
				DBProperties dbProperties = new DBProperties();
				return dbProperties.getProperty("db.gateway.url");
			} catch (Exception e) {
				return null;
			}
		} else {
			return getServletConfig().getInitParameter(key);
		}
	}	
}
