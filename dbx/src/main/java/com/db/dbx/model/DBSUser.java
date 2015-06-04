package com.db.dbx.model;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class DBSUser implements Principal {
	
	JsonNode usernode;
	
	public DBSUser(String userJSON) throws JsonProcessingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = mapper.readTree(userJSON);
		this.usernode = actualObj.findValue("user");
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
       final Set<GrantedAuthority> _grntdAuths = new HashSet<GrantedAuthority>();
		JsonNode node = this.usernode.findValue("role");
       _grntdAuths.add(new SimpleGrantedAuthority(node.getValueAsText()));
		return _grntdAuths;
	}

	public String getPassword() {
		JsonNode node = this.usernode.findValue("password");
		return node.getValueAsText();
	}

	public String getUsername() {
		JsonNode node = this.usernode.findValue("username");
		return node.getValueAsText();
	}

	public String getName() {
		return getUsername();
	}	
	
}
