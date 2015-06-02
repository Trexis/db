package com.db.dbx.gateway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
 
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
 
public class AuthorizeServletRequestWrapper extends HttpServletRequestWrapper {

	private String authtoken;
    public AuthorizeServletRequestWrapper(HttpServletRequest request, String authenticaitonToken) {
    	super(request);
        this.authtoken = authenticaitonToken;
    }
     
    public String getHeader(String name) {
        //get the request object and cast it
        HttpServletRequest request = (HttpServletRequest)getRequest();

        if(name.toLowerCase().equals("authorization"))
        {
        	return getAuthorizationHeaderValue();
        } else {
            //otherwise fall through to wrapped request object
            return request.getHeader(name);
        }
    }
     
    public Enumeration getHeaderNames() {
        //create an enumeration of the request headers
         
        //create a list
        List list = new ArrayList();
         
        //loop over request headers from wrapped request object
        HttpServletRequest request = (HttpServletRequest)getRequest();
        Enumeration e = request.getHeaderNames();
        while(e.hasMoreElements()) {
            //add the names of the request headers into the list
            String n = (String)e.nextElement();
            list.add(n);
        }
         
        list.add("Authorization");
         
        //create an enumeration from the list and return
        Enumeration en = Collections.enumeration(list);
        return en;
    }

    public Enumeration getHeaders(String headerName) {
        //create an enumeration of the request headers
         
        //create a list
        List list = new ArrayList();
         
        //loop over request headers from wrapped request object
        HttpServletRequest request = (HttpServletRequest)getRequest();
        Enumeration e = request.getHeaders(headerName);
        while(e.hasMoreElements()) {
            //add the names of the request headers into the list
            String n = (String)e.nextElement();
            list.add(n);
        }
         
        if(headerName.equals("Authorization")){
            list.add(getAuthorizationHeaderValue());
        }
         
        //create an enumeration from the list and return
        Enumeration en = Collections.enumeration(list);
        return en;
    }

    private String getAuthorizationHeaderValue(){
    	return "Bearer " + authtoken;
    }
}