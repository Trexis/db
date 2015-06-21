package com.db.dbs.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.db.dbs.enums.AssetCategory;
import com.db.dbs.enums.AssetType;
import com.db.dbs.enums.ItemType;

public class Asset {

	private String tenantname;
	private String appname;
	private AssetCategory assetcategory;
	private String path;
	private String filename;
	private String mimetype;
	private String checksum;
	boolean localized = false;
	
	public Asset(String tenantName, String appName, AssetCategory assetCategory, String path, String fileName, String mimeType, String checksum){
		this.tenantname = tenantName;
		this.appname = appName;
		this.assetcategory = assetCategory;
		this.path = path;
		this.filename = fileName;
		this.mimetype = mimeType;
		this.checksum = checksum;
	}

	@JsonIgnore
	public String getTenantname() {
		return tenantname;
	}

	public void setTenantname(String tenantname) {
		this.tenantname = tenantname;
	}

	@JsonIgnore
	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}
	

	public boolean isLocalized() {
		return localized;
	}

	public void setLocalized(boolean localized) {
		this.localized = localized;
	}

	//Location is used to also determine localized assets in the asset repository.
	public String getLocation() {
		String location = path + "/" + filename;
		return location;
	}

	@JsonIgnore
	public AssetCategory getAssetcategory() {
		return assetcategory;
	}

	public void setAssetcategory(AssetCategory assetcategory) {
		this.assetcategory = assetcategory;
	}

	@JsonIgnore
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@JsonIgnore
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	
	
}
