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
import com.db.dbs.model.Preference;
import com.db.dbs.repository.AssetRepository;
import com.db.dbs.repository.PreferenceRepository;

@Repository
public class JdbcPreferenceRepository implements PreferenceRepository {

	private final JdbcTemplate jdbcTemplate;

	@Inject
	public JdbcPreferenceRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Preference> listItemPreferences(String tenantName, String appName, ItemType itemType, String itemName) {
	    return this.jdbcTemplate.query("select * from Preferences where tenantname = ? and appname = ? and itemtype = ? and itemname = ?", new Object[] { tenantName, appName, itemType.toString(), itemName }, preferenceRowMapper());
	}

	private RowMapper<Preference> preferenceRowMapper(){
		return new RowMapper<Preference>() {
			public Preference mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Preference(rs.getString("name"), rs.getString("value"));
			}
		};
	}
}
