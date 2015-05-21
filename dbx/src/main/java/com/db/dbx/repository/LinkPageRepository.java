package com.db.dbx.repository;

import java.util.List;

import com.db.dbx.model.Link;
import com.db.dbx.model.Page;


public interface LinkPageRepository {
	
	List<Page> listPagesByTenant(String tenantName);

	List<Link> listLinksByApplication(String tenantName, String appName);
	List<Page> listPagesByApplication(String tenantName, String appName);

	Link findLinkByUrl(String tenantName, String appName, String url);
	Page findPageByName(String tenantName, String appName, String pageName);
	
}
