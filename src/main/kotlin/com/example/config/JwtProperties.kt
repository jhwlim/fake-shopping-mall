package com.example.config

import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.security.Key

@ConstructorBinding
@ConfigurationProperties("jwt.properties")
data class JwtProperties(
    val secretKey: String,
    val expirationTime: Long,
    val key: Key = Keys.hmacShaKeyFor(secretKey.toByteArray())
)
