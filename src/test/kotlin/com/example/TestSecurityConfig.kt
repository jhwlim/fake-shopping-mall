package com.example

import com.example.config.JwtProperties
import com.example.config.MethodSecurityConfig
import com.example.config.SecurityConfig
import com.example.security.JwtProvider
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import

@TestConfiguration
@EnableConfigurationProperties(
    JwtProperties::class,
)
@Import(
    SecurityConfig::class,
    JwtProvider::class,
    MethodSecurityConfig::class,
)
class TestSecurityConfig
