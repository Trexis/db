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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.db.dbx.common.DBProperties;
import com.db.dbx.mvc.view.HtmlContentView;
import com.db.dbx.mvc.view.ModelView;
import com.db.dbx.mvc.view.PageView;

@Configuration
@ComponentScan(basePackages = "com.db.dbx", excludeFilters = { @Filter(Configuration.class) })
@EnableTransactionManagement
public class MainConfig {

	@Bean
	public DBProperties dbProperties() throws Exception{
		return new DBProperties();
	}
	
	@Bean
	public PageView pageView(){
		return new PageView();
	}

	@Bean
	public ModelView modelView(){
		return new ModelView();
	}

	@Bean
	public HtmlContentView htmlContentView(){
		return new HtmlContentView();
	}
	
	
	@Bean
	public PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
}
