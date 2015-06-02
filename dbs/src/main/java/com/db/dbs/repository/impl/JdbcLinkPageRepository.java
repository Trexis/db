package com.db.dbs.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.db.dbs.model.Link;
import com.db.dbs.model.ModelContext;
import com.db.dbs.model.Page;
import com.db.dbs.repository.LinkPageRepository;

@Repository
public class JdbcLinkPageRepository implements LinkPageRepository {

	private final JdbcTemplate jdbcTemplate;
	
	@Inject
	private ModelContext modelContext;
	
	@Inject
	public JdbcLinkPageRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Link findLinkByUrl(String tenantName, String appName, String url) {
		try{
			return jdbcTemplate.queryForObject("select * from Links where tenantname = ? and appname = ? and url = ?",
				linkRowMapper(), tenantName, appName, url);
		} catch(Exception ex){
			return null;
		}
	}

	public Page findPageByName(String tenantName, String appName, String pageName) {
		try{
			return jdbcTemplate.queryForObject("select * from Pages where tenantname = ? and appname = ? and name = ?",
				pageRowMapper(), tenantName, appName, pageName);
		} catch(Exception ex){
			return null;
		}
	}

	public List<Page> listPagesByTenant(String tenantName) {
	    return this.jdbcTemplate.query("select * from Pages where tenantname = ? and appname = ''", new Object[] { tenantName }, pageRowMapper());
	}

	public List<Link> listLinksByApplication(String tenantName, String appName) {
	    return this.jdbcTemplate.query("select * from Links where tenantname = ? and appname = ?", new Object[] { tenantName, appName }, linkRowMapper());
	}

	public List<Page> listPagesByApplication(String tenantName, String appName) {
	    return this.jdbcTemplate.query("select * from Pages as P where P.tenantname = ? and P.appname = ? and P.isapplicationpage=1", new Object[] { tenantName, appName }, pageRowMapper());
	}
	
	
	private RowMapper<Link> linkRowMapper(){
		return new RowMapper<Link>() {
			public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Link(modelContext, rs.getString("tenantname"), rs.getString("appname"), rs.getString("name"), rs.getString("url"), rs.getString("pagename"));
			}
		};
	}
	
	private RowMapper<Page> pageRowMapper(){
		return new RowMapper<Page>() {
			public Page mapRow(ResultSet rs, int rowNum) throws SQLException {
				boolean isapppage = rs.getInt("isapplicationpage")==1;
				return new Page(modelContext, rs.getString("tenantname"), rs.getString("appname"), rs.getString("name"), isapppage);
			}
		};
	}


}
