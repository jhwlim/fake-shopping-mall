package com.example.config

import com.example.security.JwtAuthenticationFilter
import com.example.security.JwtProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtProvider: JwtProvider,
    private val authenticationConfiguration: AuthenticationConfiguration,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()

            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .formLogin().disable()
            .logout().disable()

            .addFilterAt(jwtAuthenticationFilter(), BasicAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(
            jwtProvider = jwtProvider,
            authManager = authenticationConfiguration.authenticationManager,
        )
    }

}
