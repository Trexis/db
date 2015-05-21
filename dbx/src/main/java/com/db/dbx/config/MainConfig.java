/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.db.dbx.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.db.dbx.controller.ModelContext;
import com.db.dbx.mvc.PageView;
import com.db.dbx.repository.TenantRepository;
import com.db.dbx.repository.impl.JdbcLinkPageRepository;
import com.db.dbx.repository.impl.JdbcTenantRepository;
import com.db.dbx.repository.impl.JdbcUserRepository;
import com.db.dbx.service.JSONModel;

@Configuration
@ComponentScan(basePackages = "com.db.dbx", excludeFilters = { @Filter(Configuration.class) })
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
public class MainConfig {

	
	@Bean(destroyMethod = "shutdown")
	public DataSource dataSource() {
		EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
		factory.setDatabaseName("dbx");
		factory.setDatabaseType(EmbeddedDatabaseType.H2);
		factory.setDatabasePopulator(databasePopulator());
		return factory.getDatabase();
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean
	public ModelContext modelContext(){
		return new ModelContext();
	}
	
	@Bean
	public JSONModel jsonModel(){
		return new JSONModel();
	}
	
	@Bean
	public PageView pageView(){
		return new PageView();
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}
	
	@Bean
	public PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	// internal helpers

	private DatabasePopulator databasePopulator() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("users.sql", JdbcUserRepository.class));
		populator.addScript(new ClassPathResource("tenants.sql", JdbcTenantRepository.class));
		populator.addScript(new ClassPathResource("apps.sql", JdbcLinkPageRepository.class));
		populator.addScript(new ClassPathResource("pages.sql", JdbcLinkPageRepository.class));
		populator.addScript(new ClassPathResource("components.sql", JdbcLinkPageRepository.class));
		populator.addScript(new ClassPathResource("links.sql", JdbcLinkPageRepository.class));
		populator.addScript(new ClassPathResource("initialdata.sql", JdbcUserRepository.class));
		return populator;
	}
}
