package com.db.dbs.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.db.dbs.model.ModelContext;
import com.db.dbs.model.Tenant;
import com.db.dbs.repository.TenantRepository;

@Repository
public class JdbcTenantRepository implements TenantRepository {

	private final JdbcTemplate jdbcTemplate;

	@Inject
	private ModelContext modelContext;
	
	@Inject
	public JdbcTenantRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	public Tenant findTenantByUrl(String url) {
		return jdbcTemplate.queryForObject("select Tenants.* from Tenants, Apps where Apps.tenantname = Tenants.name and Apps.url = ?",
				rowMapper(), url);
	}

	public Tenant findTenantByName(String name) {
		return jdbcTemplate.queryForObject("select Tenants.* from Tenants where Tenants.name = ?",
				rowMapper(), name);
	}
	
	private RowMapper<Tenant> rowMapper(){
		return new RowMapper<Tenant>() {
			public Tenant mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Tenant(modelContext, rs.getString("name"), rs.getString("description"), rs.getString("defaultappname"));
			}
		};
	}
}
