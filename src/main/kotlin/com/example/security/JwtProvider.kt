package com.example.security

import com.example.common.Constants
import com.example.common.enums.UserRole
import com.example.config.JwtProperties
import com.example.exception.BaseException
import com.example.exception.ErrorType
import com.example.utils.toDate
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.time.LocalDateTime

private val log = KotlinLogging.logger {}

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

    fun decodeToken(token: String): SecurityUser {
        try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(jwtProperties.key)
                .build()
                .parseClaimsJws(token)
                .body
            return SecurityUser(
                id = claims.subject.toLong(),
                name = claims[Constants.AUTH_CLAIM_KEY_OF_NAME].toString(),
                role = UserRole.valueOf(claims[Constants.AUTH_CLAIM_KEY_OF_ROLE].toString()),
            )
        } catch (e: Exception) {
            log.error { e }
            throw BaseException(ErrorType.INVALID_AUTH_TOKEN)
        }
    }

}
