package com.example.aerodrom.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
	 @Autowired
	    @Qualifier("customUserDetailsService")
	    UserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				.antMatchers("/", "/user/**", "/styles/**", "/auth/**", "/destinacije/**", "/letovi/**", "/rezervacija/**", "/klase/**")
				.permitAll() //putanje koje su dozvoljene svima
				.antMatchers("/**") 
				.hasRole("MANAGER") // putanje dostupne manageru
//				.antMatchers("/")
//				.hasRole("USER") //putanje dostupne obicnom useru
			.and()
			.formLogin()
			.loginPage("/auth/loginPage")
			.loginProcessingUrl("/login")
			.failureUrl("/auth/loginPage?error=true")
	        .usernameParameter("username")
	        .passwordParameter("password")
			.defaultSuccessUrl("/auth/pocetna")
			.permitAll()
			.and()
			.logout()
			.logoutSuccessUrl("/")
			.and().rememberMe().key("uniqueAndSecret");
	}
}
