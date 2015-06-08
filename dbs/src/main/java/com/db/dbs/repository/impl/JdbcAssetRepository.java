package com.db.dbs.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
					"insert into Assets(tenantname, appname, itemtype, itemname, assetlocation, assettype, assetchecksum) values (?, ?, ?, ?, ?, ?,?);",
					asset.getTenantname(), asset.getAppname(), asset.getItemtype().toString(), asset.getItemname(), asset.getLocation(), asset.getAssettype().toString(), asset.getChecksum());
		} catch (DuplicateKeyException e) {
			jdbcTemplate.update(
					"update assets set assetchecksum=? and assettype=? where tenantname=? and appname=? and itemtype=? and itemname=? and assetlocation=?;",
					asset.getChecksum(), asset.getAssettype().toString(), asset.getTenantname(), asset.getAppname(), asset.getItemtype().toString(), asset.getItemname(), asset.getLocation());		
		}
	}
	
	public List<Asset> listItemAssets(String tenantName, String appName, ItemType itemType, String itemName) {
	    return this.jdbcTemplate.query("select * from Assets where tenantname = ? and appname = ? and itemtype = ? and itemname = ?", new Object[] { tenantName, appName, itemType.toString(), itemName }, assetRowMapper());
	}

	private RowMapper<Asset> assetRowMapper(){
		return new RowMapper<Asset>() {
			public Asset mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Asset(rs.getString("tenantname"), rs.getString("appname"), ItemType.valueOf(rs.getString("itemtype")), rs.getString("itemname"), AssetType.valueOf(rs.getString("assettype")), rs.getString("assetlocation"), rs.getString("assetchecksum"));
			}
		};
	}

}
