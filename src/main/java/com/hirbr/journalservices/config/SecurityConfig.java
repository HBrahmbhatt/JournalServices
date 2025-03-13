package com.hirbr.journalservices.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.hirbr.journalservices.services.CustomUserDetailsServiceImpl;

@Configuration
public class SecurityConfig {

	@Autowired
	CustomUserDetailsServiceImpl customUserDetailsService;

	/*
	 * Version of security post 3 has a SecurityFilterChain added. It decides... 1.
	 * Which requests need authentication and allows/denies access on basis of
	 * roles/permissions. 2. can manage csrf related settings. 3. configure session
	 * policies (stateless for REST APIs, session-based for web apps). 4. It applies
	 * authentication & authorization filters. 5. It integrates with login/logout
	 * mechanisms.
	 * 
	 */
	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/journaling-services/**", "/user/**").authenticated()
				.requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().permitAll())
				.httpBasic(Customizer.withDefaults());
		// instead of just disabling csrf token, you can make the services stateless as
		// of now
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable();
		;
		return http.build();
	}

	// Exposing AuthenticationManager as a Bean
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		// Start by initializing the authentication manager builder
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);

		// Configuring AuthenticationManager with UserDetailsService and PasswordEncoder
		authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());

		return authenticationManagerBuilder.build(); // Build and return AuthenticationManager
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
