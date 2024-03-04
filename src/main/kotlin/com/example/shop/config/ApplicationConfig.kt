package com.example.shop.config

import com.example.shop.repositories.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


/**
 * Configuration class for application-specific configurations, such as security-related beans.
 *
 * @property userRepository The repository for user entities.
 */
@Configuration
class ApplicationConfig(private val userRepository: UserRepository) {

    /**
     * Provides a custom UserDetailsService implementation.
     *
     * @return UserDetailsService Returns a UserDetailsService implementation.
     */
    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            userRepository.findByUsername(username)
                .orElseThrow { UsernameNotFoundException("User not found") }
        }
    }

    /**
     * Provides a BCryptPasswordEncoder bean for password encoding.
     *
     * @return BCryptPasswordEncoder Returns a BCryptPasswordEncoder bean.
     */
    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * Provides an AuthenticationManager bean using the provided AuthenticationConfiguration.
     *
     * @param config The AuthenticationConfiguration to configure the AuthenticationManager.
     * @return AuthenticationManager Returns an AuthenticationManager bean.
     */
    @Bean
    @Throws(Exception::class)
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    /**
     * Provides an AuthenticationProvider bean using DaoAuthenticationProvider.
     *
     * @return AuthenticationProvider Returns an AuthenticationProvider bean.
     */
    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()

        // Set the custom UserDetailsService and password encoder for authentication
        authenticationProvider.setUserDetailsService(userDetailsService())
        authenticationProvider.setPasswordEncoder(passwordEncoder())

        return authenticationProvider
    }
}