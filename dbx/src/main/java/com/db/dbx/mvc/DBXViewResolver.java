package com.db.dbx.mvc;

import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

public class DBXViewResolver implements ViewResolver {

	@Inject
	private ApplicationContext appContext;
	
	@Inject
	private PageView view;
	
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		if(!viewName.equals("")){
			return view;
		} else {
			InternalResourceViewResolver urlResolver = new InternalResourceViewResolver();
			urlResolver.setApplicationContext(appContext);
			urlResolver.setPrefix("/WEB-INF/views/");  //todo, make this configurable
            urlResolver.setSuffix(".jsp");
			return urlResolver.resolveViewName("404", Locale.ENGLISH);
		}
	}

}
