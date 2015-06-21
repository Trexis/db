package com.db.dbs.repository;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.db.dbs.enums.ItemType;
import com.db.dbs.model.Asset;

public interface ContentRepository {

	String findPageHTML(String tenantName, String applicationName, String pageName) throws Exception;
	String findPageAsJson(String tenantName, String applicationName, String pageName) throws Exception;

	String findComponentHTML(String tenantName, String applicationName, String pageName, String componentName) throws Exception;
	String findComponentAsJson(String tenantName, String applicationName, String pageName, String componentName) throws Exception;
	
	void updateItemAsset(String tenantName, String applicationName, String relativePath, File file) throws Exception;
	
	InputStream findContentByPath(String relativePathToContent, String fileName) throws Exception;
	String findContentAsJsonByPath(String relativePathToContent, String fileName) throws Exception;
	
	
}
