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

	@Transactional
	public void updatePreference(Preference preference) {
		try {
			jdbcTemplate.update(
					"insert into Preferences(tenantname, appname, itemtype, itemname, prefname, prefvalue) values (?, ?, ?, ?, ?, ?);",
					preference.getTenantname(), preference.getApplicationname(), preference.getItemtype().toString(), preference.getItemname(), preference.getName(), preference.getValue());
		} catch (DuplicateKeyException e) {
			jdbcTemplate.update(
					"update assets set prefvalue=? where tenantname=? and appname=? and itemtype=? and itemname=? and prefname=?;",
					preference.getValue(), preference.getTenantname(), preference.getApplicationname(), preference.getItemtype().toString(), preference.getItemname(), preference.getName(), preference.getValue());		
		}
	}

	
	public List<Preference> listItemPreferences(String tenantName, String appName, ItemType itemType, String itemName) {
	    return this.jdbcTemplate.query("select * from Preferences where tenantname = ? and appname = ? and itemtype = ? and itemname = ?", new Object[] { tenantName, appName, itemType.toString(), itemName }, preferenceRowMapper());
	}

	private RowMapper<Preference> preferenceRowMapper(){
		return new RowMapper<Preference>() {
			public Preference mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Preference(rs.getString("tenantname"), rs.getString("appname"), ItemType.valueOf(rs.getString("itemtype")), rs.getString("itemname"), rs.getString("prefname"), rs.getString("prefvalue"));
			}
		};
	}
}
