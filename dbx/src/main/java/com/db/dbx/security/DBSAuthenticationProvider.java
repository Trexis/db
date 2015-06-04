package com.db.dbx.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.db.dbx.gateway.DBSGatewayClient;
import com.db.dbx.model.DBSUser;
import com.db.dbx.utilities.Utilities;

public class DBSAuthenticationProvider implements AuthenticationProvider {

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try{
			String username = authentication.getName();
			String credentials = (String) authentication.getCredentials();
			
			DBSGatewayClient client = new DBSGatewayClient();
			String userjsonmodel = client.performJSONGetAsString("/model/user/" + username);
			
			if(Utilities.validateDBSResponseJSON(userjsonmodel)){
				DBSUser user = new DBSUser(userjsonmodel);
				if(user.getPassword().equals(credentials)){
					GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
					return new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), authoritiesMapper.mapAuthorities(user.getAuthorities()));
				} else {
					throw new BadCredentialsException("Incorrect password provided.");
				}
			} else {
				throw new Exception("Unable to get user information from DBS");
			}
		} catch(Exception ex){
			throw new UsernameNotFoundException(ex.getLocalizedMessage());
		}

	}

	public boolean supports(Class<?> authentication) {
		return true;
	}

}
