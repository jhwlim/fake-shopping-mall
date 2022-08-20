package com.example.service

import com.example.domain.user.UserRepository
import com.example.exception.BaseException
import com.example.exception.ErrorType
import com.example.security.JwtProvider
import com.example.security.SecurityUser
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
) {

    fun createAccessToken(name: String, password: String): String {
        val user = userRepository.findByName(name) ?: throw BaseException(ErrorType.USER_NOT_FOUND)
        if (user.password != password) {
            throw BaseException(ErrorType.PASSWORD_NOT_MATCHES)
        }

        return jwtProvider.createToken(SecurityUser.from(user), LocalDateTime.now())
    }

}
