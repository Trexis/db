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

import com.db.dbs.model.Component;
import com.db.dbs.model.ModelContext;
import com.db.dbs.model.Page;
import com.db.dbs.repository.ComponentRepository;

@Repository
public class JdbcComponentRepository implements ComponentRepository {

	private final JdbcTemplate jdbcTemplate;
	
	@Inject
	private ModelContext modelContext;
	
	@Inject
	public JdbcComponentRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Transactional
	public void updateComponent(Component component) {
		try {
			jdbcTemplate.update(
					"insert into Components(tenantname, appname, pagename, name, title) values (?, ?, ?, ?, ?);",
					component.getTenantname(), component.getAppname(), component.getPagename(), component.getName(), component.getTitle());
		} catch (DuplicateKeyException e) {
			jdbcTemplate.update(
					"update components set title=? where tenantname=? and appname=? and pagename=? and name=?;",
					component.getTitle(), component.getTenantname(), component.getAppname(), component.getPagename(), component.getName());		
		}
	}


	public List<Component> listComponentsByPage(String tenantName, String appName, String pageName) {
	    return this.jdbcTemplate.query("select * from Components where tenantname = ? and appname = ? and pagename = ?", new Object[] { tenantName, appName, pageName }, componentRowMapper());
	}

	public Component findComponentByReference(String tenantName, String appName, String pageName, String componentRef) {
		return jdbcTemplate.queryForObject("select * from Components where tenantname = ? and appname = ? and pagename = ? and reference = ?",
				componentRowMapper(), tenantName, appName, pageName, componentRef);
	}

	public Component findComponentByName(String tenantName, String appName,	String pageName, String componentName) {
		return jdbcTemplate.queryForObject("select * from Components where tenantname = ? and appname = ? and pagename = ? and name = ?",
				componentRowMapper(), tenantName, appName, pageName, componentName);
	}

	private RowMapper<Component> componentRowMapper(){
		return new RowMapper<Component>() {
			public Component mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Component(modelContext, rs.getString("tenantname"), rs.getString("appname"), rs.getString("pagename"), rs.getString("name"), rs.getString("title"));
			}
		};
	}

}
