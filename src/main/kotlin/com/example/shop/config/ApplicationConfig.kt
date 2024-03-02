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


@Configuration
class ApplicationConfig (private val userRepository : UserRepository) {

    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            userRepository.findByUsername(username)
                .orElseThrow { UsernameNotFoundException("User not found") }
        }
    }
    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
    @Bean
    @Throws(Exception::class)
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailsService())
        authenticationProvider.setPasswordEncoder(passwordEncoder())
        return authenticationProvider
    }
}