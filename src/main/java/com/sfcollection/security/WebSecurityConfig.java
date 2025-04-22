package com.sfcollection.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Use explicit AntPathRequestMatcher for all patterns to avoid MvcRequestMatcher issues
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> 
                auth
                    // OpenAPI endpoints
                    .requestMatchers(
                        new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/swagger-ui/**"),
                        new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api-docs/**"),
                        new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/v3/api-docs/**")
                    ).permitAll()
                    // Authentication endpoints
                    .requestMatchers(
                        new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/v1/auth/**")
                    ).permitAll()
                    // Health check and metrics endpoints
                    .requestMatchers(
                        new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/actuator/**")
                    ).permitAll()
                    // H2 console
                    .requestMatchers(
                        new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/h2-console/**")
                    ).permitAll()
                    // GET requests
                    .requestMatchers(
                        new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/v1/books/**", HttpMethod.GET.name()),
                        new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/v1/authors/**", HttpMethod.GET.name()),
                        new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/v1/collections/**", HttpMethod.GET.name())
                    ).permitAll()
                    // All other requests need authentication
                    .anyRequest().authenticated()
            );
        
        // Add JWT filter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        
        // Allow frames for H2 console (development only)
        http.headers(headers -> headers.frameOptions(frameOption -> frameOption.sameOrigin()));
        
        return http.build();
    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // In production, restrict to specific domains
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}