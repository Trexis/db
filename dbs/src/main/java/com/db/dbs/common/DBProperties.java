package com.db.dbs.common;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.naming.InitialContext;

public class DBProperties extends Properties {

	public DBProperties() throws Exception{
		try{
			InitialContext initialContext = new InitialContext();  
			String filelocation = (String) initialContext.lookup("java:comp/env/db/config");
			InputStreamReader input = new InputStreamReader(new FileInputStream(filelocation), "UTF8");
			
			this.load(input);
						
		} catch (Exception ex){
			ex.printStackTrace();
			throw new Exception("Unable to load db.properties file. Configure a environment variable called db/config and point it to the db.properties file.", ex);
		}
	}
}
