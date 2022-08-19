package com.example.security

import com.example.common.Constants
import com.example.config.JwtProperties
import com.example.utils.toDate
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class JwtProvider(
    private val jwtProperties: JwtProperties,
) {

    fun createToken(user: SecurityUser, dateTime: LocalDateTime): String {
        return Jwts.builder()
            .setSubject(user.id.toString())
            .claim(Constants.AUTH_CLAIM_KEY_OF_NAME, user.name)
            .claim(Constants.AUTH_CLAIM_KEY_OF_ROLE, user.role)
            .signWith(jwtProperties.key, SignatureAlgorithm.HS512)
            .setExpiration(dateTime.plusSeconds(jwtProperties.expirationTime).toDate())
            .compact()
    }

}
