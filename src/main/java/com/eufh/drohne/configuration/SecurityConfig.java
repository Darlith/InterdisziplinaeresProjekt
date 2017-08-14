package com.eufh.drohne.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.eufh.drohne.security.Securityhandler;

@Configuration
@EnableWebSecurity
@ComponentScan("com.eufh.drohne.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	Securityhandler successHandler;
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("Robert").password("password").roles("MANAGER");
        auth.inMemoryAuthentication().withUser("Peter").password("password").roles("ARBEITER");
        auth.inMemoryAuthentication().withUser("admin").password("password").roles("ADMIN");
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/assets/**").permitAll()//.anyRequest().permitAll()//
				// Rechtesystem der Rollen
				.antMatchers("/dashboard").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
				.antMatchers("/bepacken").access("hasRole('ROLE_ARBEITER') or hasRole('ROLE_ADMIN')")
				.anyRequest().authenticated().and() // den Rest authorisieren
				.formLogin().usernameParameter("arbeitsanteil").passwordParameter("passwort")//
				// Authentifizierung gegen persoenliche login-Page
				.loginPage("/login").permitAll().successHandler(successHandler)//
				.and()//
				.logout()//
				// Logout Weiterleitung
				.logoutUrl("/logout").permitAll(); // POST only
				
	}
}