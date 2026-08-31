package com.auth.config;

import com.auth.security.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	// private final CustomUserDetailsService userDetailsService;

	// public SecurityConfig(CustomUserDetailsService userDetailsService) {
	// this.userDetailsService = userDetailsService;
	// }

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*
	 * @Bean DaoAuthenticationProvider authenticationProvider() {
	 * 
	 * DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	 * 
	 * provider.setUserDetailsService(userDetailsService);
	 * provider.setPasswordEncoder(passwordEncoder());
	 * 
	 * return provider; }
	 */

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

		return configuration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())

				// .authenticationProvider(authenticationProvider())

				.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/register", "/auth/login").permitAll()
						.anyRequest().authenticated())

				.formLogin(form -> form.disable());

		return http.build();
	}
}