package com.db.dbs.repository;

import java.util.List;

import com.db.dbs.model.Application;

public interface ApplicationRepository {
	
	List<Application> listApplicationsByTenant(String tenantName);
	Application findApplicationByName(String tenantName, String appName);
	Application findApplicationByUrl(String url);
	
	void updateApplication(Application application);
	
}
