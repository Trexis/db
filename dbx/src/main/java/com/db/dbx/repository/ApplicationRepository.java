package com.db.dbx.repository;

import java.util.List;

import com.db.dbx.model.Application;

public interface ApplicationRepository {
	
	List<Application> listApplicationsByTenant(String tenantName);
	Application findApplicationByName(String tenantName, String appName);
	Application findApplicationByUrl(String url);
	
}
