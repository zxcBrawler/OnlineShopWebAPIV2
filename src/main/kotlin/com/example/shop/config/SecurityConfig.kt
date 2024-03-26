package com.example.shop.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Autowired private val jwtAuthFilter: JwtAuthFilter,
    @Autowired private val authenticationProvider: AuthenticationProvider
) {
    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // Disable CSRF protection for this application
            .csrf { it.disable() }

            // Configure authorization rules for different endpoints
            .authorizeHttpRequests {
                it
                    // Allow unrestricted access to the login and register endpoint
                    .requestMatchers("/api/auth/login").permitAll()
                    .requestMatchers("/api/auth/register").permitAll()
                    .requestMatchers("/api/auth/changePassword").permitAll()
                    // Allow unrestricted access to the Prometheus actuator endpoint
                    .requestMatchers("/actuator/prometheus").permitAll()

                    // TODO: handle other api endpoints based on each role here

                    // Require authentication for any other request
                    .anyRequest().authenticated()
            }

            // Set session management to stateless, as JWTs are used for authentication
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

            // Set the custom authentication provider
            .authenticationProvider(authenticationProvider)

            // Add the custom JWT authentication filter before the default UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        // Build and return the configured SecurityFilterChain
        return http.build()
    }
}