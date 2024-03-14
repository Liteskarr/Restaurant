package com.liteskarr.restaurant.configs

import com.liteskarr.restaurant.adapters.UserDetailsServiceAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {
    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsServiceAdapter()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf { obj: AbstractHttpConfigurer<*, *> -> obj.disable() }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/test").permitAll()
                auth.requestMatchers("/register").permitAll()
                auth.requestMatchers("/templates/guest-register.html").permitAll()
                auth.requestMatchers("/api/**").hasAuthority("ADMIN")
                auth.requestMatchers("/swagger-ui/**").hasAuthority("ADMIN")
                auth.requestMatchers("/**").authenticated()
            }
            .formLogin { obj: AbstractAuthenticationFilterConfigurer<*, *, *> -> obj.permitAll() }
            .build()
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(userDetailsService())
        provider.setPasswordEncoder(passwordEncoder())
        return provider
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}