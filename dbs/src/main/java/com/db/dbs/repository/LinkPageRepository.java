package com.db.dbs.repository;

import java.util.List;

import com.db.dbs.model.Link;
import com.db.dbs.model.Page;


public interface LinkPageRepository {
	
	List<Page> listPagesByTenant(String tenantName);

	List<Link> listLinksByApplication(String tenantName, String appName, String parentLinkName);
	List<Page> listPagesByApplication(String tenantName, String appName);

	Link findLinkByUrl(String tenantName, String appName, String url);
	Page findPageByName(String tenantName, String appName, String pageName);
	
}
