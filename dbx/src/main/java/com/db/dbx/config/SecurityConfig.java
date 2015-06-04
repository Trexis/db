package com.db.dbx.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.db.dbx.security.DBSAuthenticationProvider;
import com.db.dbx.security.DBSLoginUrlAuthenticationEntryPoint;
import com.db.dbx.security.DBXAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private ApplicationContext context;
		
	@Autowired
	public void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(new DBSAuthenticationProvider());
		/*auth.jdbcAuthentication()
				.dataSource(dataSource)
				.usersByUsernameQuery("select username, password, true from Users where username = ?")
				.authoritiesByUsernameQuery("select username, role from Users where username = ?")
				.passwordEncoder(passwordEncoder());*/
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
				.antMatchers("/resources/**");
	}
	
	//org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			//.httpBasic()
			//	.authenticationEntryPoint(new DBSLoginUrlAuthenticationEntryPoint("/security/login"))
			//.and()
				.formLogin()
					.loginPage("/security/login")
					.loginProcessingUrl("/security/authenticate")
					.failureUrl("/security/login?error=bad_credentials")
					//.failureHandler(new DBXAuthenticationFailureHandler())
			.and()
				.logout()
					.logoutUrl("/security/logout")
					.deleteCookies("JSESSIONID")
			.and()
				.authorizeRequests()
					.antMatchers("/explorer**").access("hasRole('ROLE_ADMIN')")
					.antMatchers("/**").permitAll()
			.and()
				.rememberMe();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public TextEncryptor textEncryptor() {
		return Encryptors.noOpText();
	}

}
