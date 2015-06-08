package com.db.dbs.repository;

import java.util.List;

import com.db.dbs.model.Application;
import com.db.dbs.model.Component;

public interface ComponentRepository {
	
	List<Component> listComponentsByPage(String tenantName, String appName, String pageName);
	Component findComponentByName(String tenantName, String appName, String pageName, String componentName);
	Component findComponentByReference(String tenantName, String appName, String pageName, String componentRef);
	
	void updateComponent(Component component);
}
