package com.db.dbx.mvc.view;

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

public class MVCViewResolver implements ViewResolver {

	@Inject
	private ApplicationContext appContext;
	
	@Inject
	private PageView dbxpageview;

	@Inject
	private ModelView dbxmodelview;

	@Inject
	private HtmlContentView dbxhtmlcontentview;
	
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		if(viewName.equals("")||viewName.equals("404")){
			return performJSPResolve("404");			
		} else if(viewName.equals("login")||viewName.equals("401")){
			return performJSPResolve("login");			
		} else if(viewName.equals("security/enroll")){
			return performJSPResolve("enroll");			
		} else if(viewName.equals("status")){
			return performJSPResolve("status");	
		} else if(viewName.equals("explorer")){
			return performJSPResolve("explorer");
		} else if(viewName.equals("model")){
			return dbxmodelview;
		} else if(viewName.equals("model_page")){
			return dbxmodelview;
		} else if(viewName.equals("model_component")){
			return dbxmodelview;
		} else if(viewName.equals("dbxpage")){
			return dbxpageview;
		} else {
			return performJSPResolve("404");			
		}
	}
	
	private View performJSPResolve(String viewName) throws Exception{
		InternalResourceViewResolver urlResolver = new InternalResourceViewResolver();
		urlResolver.setApplicationContext(appContext);
		urlResolver.setPrefix("/WEB-INF/views/");  //todo, make this configurable
        urlResolver.setSuffix(".jsp");
		return urlResolver.resolveViewName(viewName, Locale.ENGLISH);
	}

}
