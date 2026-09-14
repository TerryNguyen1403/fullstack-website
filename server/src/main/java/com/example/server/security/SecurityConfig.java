package com.example.server.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
  }

	// BCrypt
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

  // AuthenticationManager
  @Bean
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration config
  ) throws Exception {
    return config.getAuthenticationManager();
  }

	// SecurityFilterChain
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)                       // API stateless không cần CSRF
      .sessionManagement(sm ->
        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))// không tạo session
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/**").permitAll()    // login/register công khai
        .anyRequest().authenticated())                  // còn lại cần đăng nhập
      // Cắm JwtAuthenticationFilter TRƯỚC UsernamePasswordAuthenticationFilter
      .addFilterBefore(jwtAuthenticationFilter,
        UsernamePasswordAuthenticationFilter.class);


    return http.build();
  }
}
