package com.db.dbs.repository;

import java.util.List;

import com.db.dbs.enums.ItemType;
import com.db.dbs.model.Asset;

public interface AssetRepository {
	
	List<Asset> listItemAssets(String tenantName, String appName, String pageName);
	List<Asset> listItemAssets(String tenantName, String appName, String pageName, String componentName);
	
	void updateAsset(Asset asset);
}
