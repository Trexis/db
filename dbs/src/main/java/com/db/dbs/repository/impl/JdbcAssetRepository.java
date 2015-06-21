package com.db.dbs.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.db.dbs.enums.AssetCategory;
import com.db.dbs.enums.AssetType;
import com.db.dbs.enums.ItemType;
import com.db.dbs.model.Asset;
import com.db.dbs.model.Component;
import com.db.dbs.repository.AssetRepository;

@Repository
public class JdbcAssetRepository implements AssetRepository {

	private final JdbcTemplate jdbcTemplate;

	@Inject
	public JdbcAssetRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Transactional
	public void updateAsset(Asset asset) {
		try {
			jdbcTemplate.update(
					"insert into Assets(tenantname, appname, category, path, filename, mimetype, checksum) values (?, ?, ?, ?, ?, ?, ?);",
					asset.getTenantname(), asset.getAppname(), asset.getAssetcategory().toString(), asset.getPath(), asset.getFilename(), asset.getMimetype(), asset.getChecksum());
		} catch (DuplicateKeyException e) {
			jdbcTemplate.update(
					"update assets set mimetype=?, checksum=? where tenantname=? and appname=? and category=? and path=? and filename=?;",
					asset.getMimetype(), asset.getChecksum(), asset.getTenantname(), asset.getAppname(), asset.getAssetcategory().toString(), asset.getPath(), asset.getFilename());		
		}
	}
	
	public List<Asset> listItemAssets(String tenantName, String appName, String pageName) {
		String relativepath = "/_pages/" + pageName;
	    return this.jdbcTemplate.query("select * from Assets where tenantname = ? and appname = ? and path = ?", new Object[] { tenantName, appName, relativepath }, assetRowMapper());
	}
	
	public List<Asset> listItemAssets(String tenantName, String appName, String pageName, String componentName) {
		String relativepath = "/_components/" + componentName;
		
		List<Asset> catelogassets = this.jdbcTemplate.query("select * from Assets where tenantname = ? and appname = ? and path = ?", new Object[] { tenantName, appName, relativepath }, assetRowMapper());
		
		relativepath = "/_pages/" + pageName + "/_components/" + componentName;
		List<Asset> localizedassets = this.jdbcTemplate.query("select * from Assets where tenantname = ? and appname = ? and path = ?", new Object[] { tenantName, appName, relativepath }, assetRowMapper());

		
		List<Asset> uniquelist = new ArrayList<Asset>(catelogassets);
		
		for(Asset localasset: localizedassets){
			localasset.setLocalized(true);
			String localassetlocation = localasset.getLocation();
			localassetlocation = localassetlocation.replace("/_pages/" + pageName, "");
			for(Asset catasset: catelogassets){
				if(localassetlocation.equals(catasset.getLocation())){
					uniquelist.remove(catasset);
				}
			}
		}
		uniquelist.addAll(localizedassets);
		
	    return uniquelist;
	}

	private RowMapper<Asset> assetRowMapper(){
		return new RowMapper<Asset>() {
			public Asset mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Asset(rs.getString("tenantname"), rs.getString("appname"), AssetCategory.valueOf(rs.getString("category")), rs.getString("path"), rs.getString("filename"), rs.getString("mimetype"), rs.getString("checksum"));
			}
		};
	}

}
