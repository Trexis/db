package com.db.dbx.mvc;

import javax.servlet.Filter;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.db.dbx.config.MainConfig;
import com.db.dbx.config.SecurityConfig;
import com.db.dbx.config.SocialConfig;
import com.db.dbx.config.WebMvcConfig;

public class SpringMvcInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { MainConfig.class, WebMvcConfig.class, SecurityConfig.class, SocialConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
	
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		
		DelegatingFilterProxy reconnectDelegate = new DelegatingFilterProxy("apiExceptionHandler");
		
		return new Filter[] { reconnectDelegate, encodingFilter, new HiddenHttpMethodFilter() };
	}

}
