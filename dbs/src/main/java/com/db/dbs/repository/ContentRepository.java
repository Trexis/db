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

	void updatePageHTML(String tenantName, String applicationName, String pageName, String content) throws Exception;
	void updateComponentHTML(String tenantName, String applicationName, String componentName, String content) throws Exception;
	void updateComponentHTML(String tenantName, String applicationName, String pageName, String componentName, String content) throws Exception;
	
	void updateItemAsset(String tenantName, String applicationName, String pageName, String relativePath, File file) throws Exception;
	void updateItemAsset(String tenantName, String applicationName, String pageName, String componentName, String relativePath, File file) throws Exception;
	
	InputStream findContentByPath(String relativePathToContent, String fileName) throws Exception;
	String findContentAsJsonByPath(String relativePathToContent, String fileName) throws Exception;
	
	
}
