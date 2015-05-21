package com.db.dbx.repository;

import java.util.List;

import com.db.dbx.model.Application;
import com.db.dbx.model.Component;

public interface ComponentRepository {
	
	List<Component> listComponentsByPage(String tenantName, String appName, String pageName);
	Component findComponentByReference(String tenantName, String appName, String pageName, String componentRef);
	
}
