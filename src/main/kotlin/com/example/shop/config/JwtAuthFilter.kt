package com.example.shop.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


/**
 * Custom JWT authentication filter for processing JWT tokens and setting up the Spring Security context.
 *
 * @param jwtService The service for handling JWT-related operations.
 * @param userDetailsService The service for loading user-specific data.
 */
@Component
class JwtAuthFilter(
    @Autowired private val jwtService: JwtService,
    @Autowired private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    /**
     * Filters incoming requests to process JWT tokens and set up Spring Security context.
     *
     * @param request The incoming HTTP request.
     * @param response The HTTP response.
     * @param filterChain The filter chain for processing subsequent filters.
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Extract the Authorization header
        val authHeader: String? = request.getHeader("Authorization")
        val userEmail: String

        // Check if the Authorization header is present and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        // Extract the JWT token from the Authorization header
        val jwtToken: String = authHeader.substring(7)
        // Extract username from token that was generated
        userEmail = jwtService.extractUsername(jwtToken)

        // Check if the Spring Security context has no authentication
        if (SecurityContextHolder.getContext().authentication == null) {
            // Load user details from the UserDetailsService based on the extracted user email
            val userDetails: UserDetails = userDetailsService.loadUserByUsername(userEmail)

            // Validate the JWT token and create an authentication token if valid
            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                val authenticationToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )

                // Set details for the authentication token
                authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                // Set the authentication token in the Spring Security context
                SecurityContextHolder.getContext().authentication = authenticationToken
            }
        }

        // Continue processing subsequent filters in the filter chain
        filterChain.doFilter(request, response)
    }
}