package com.db.dbs.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.db.dbs.enums.AssetType;
import com.db.dbs.enums.ItemType;
import com.db.dbs.model.Asset;
import com.db.dbs.repository.AssetRepository;

@Repository
public class JdbcAssetRepository implements AssetRepository {

	private final JdbcTemplate jdbcTemplate;

	@Inject
	public JdbcAssetRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Asset> listItemAssets(String tenantName, String appName, ItemType itemType, String itemName) {
	    return this.jdbcTemplate.query("select * from Assets where tenantname = ? and appname = ? and itemtype = ? and itemname = ?", new Object[] { tenantName, appName, itemType.toString(), itemName }, assetRowMapper());
	}

	private RowMapper<Asset> assetRowMapper(){
		return new RowMapper<Asset>() {
			public Asset mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Asset(AssetType.valueOf(rs.getString("assettype")), rs.getString("assetlocation"), rs.getString("assetchecksum"));
			}
		};
	}

}
