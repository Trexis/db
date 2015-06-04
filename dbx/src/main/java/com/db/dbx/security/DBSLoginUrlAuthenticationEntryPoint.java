package com.db.dbx.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class DBSLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

	String loginformurl;
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException
	{
       RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
       redirectStrategy.sendRedirect(request, response, this.loginformurl);
	}

	public DBSLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
		this.loginformurl = loginFormUrl;
	}

}
