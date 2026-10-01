package com.ecommerce.app.config;

import com.ecommerce.app.security.CustomAccessDeniedHandler;
import com.ecommerce.app.security.CustomAuthenticationEntryPoint;
import com.ecommerce.app.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Enable CORS with configuration from CorsConfig
            .cors(cors -> cors.configurationSource(corsConfigurationSource))

            // Disable CSRF for stateless JWT authentication
            .csrf(csrf -> csrf.disable())

            // Set session creation policy to STATELESS (no session cookies)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Configure authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public endpoints (no authentication required)
                .requestMatchers(
                    "/api/auth/login",
                    "/api/auth/register"
                ).permitAll()

                // Public product endpoints (no authentication required)
                .requestMatchers("/api/products/**").permitAll()

                // Test endpoints (public for development)
                .requestMatchers("/api/test/**").permitAll()

                // H2 Console (for development database access)
                .requestMatchers("/h2-console/**").permitAll()

                // Admin-specific auth endpoints (requires ADMIN role)
                .requestMatchers(
                    "/api/auth/unlock",
                    "/api/auth/register-admin"
                ).hasRole("ADMIN")

                // Admin endpoints - ALL /api/admin/** require ADMIN role (Defense in Depth)
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // User endpoints - ALL /api/user/** require USER or ADMIN role (Defense in Depth)
                .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")

                // Cart endpoints - require USER or ADMIN role
                .requestMatchers("/api/cart/**").hasAnyRole("USER", "ADMIN")

                // Order endpoints - require USER or ADMIN role
                .requestMatchers("/api/orders/**").hasAnyRole("USER", "ADMIN")

                // All other requests require authentication
                .anyRequest().authenticated()
            );

        // Configure exception handling
        http.exceptionHandling(exception -> exception
            .accessDeniedHandler(customAccessDeniedHandler)      // 403 Forbidden
            .authenticationEntryPoint(customAuthenticationEntryPoint)  // 401 Unauthorized
        );

        // Add JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Allow H2 console frame (development only)
        http.headers(headers -> headers
            .frameOptions(frame -> frame.disable())
        );

        return http.build();
    }

}
