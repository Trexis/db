package com.db.dbs.repository;

import java.io.InputStream;

public interface ContentRepository {

	void populateInitialContent() throws Exception;
	
	InputStream findContentByPath(String relativePathToContent, String fileName) throws Exception;
	String findContentAsJsonByPath(String relativePathToContent, String fileName) throws Exception;
	
	String findPageContent(String tenantName, String applicationName, String pageName) throws Exception;
	String findPageContentAsJson(String tenantName, String applicationName, String pageName) throws Exception;

	String findComponentContent(String tenantName, String applicationName, String pageName, String componentName) throws Exception;
	
	void createPage(String tenantName, String applicationName, String pageName, String content) throws Exception;
}
