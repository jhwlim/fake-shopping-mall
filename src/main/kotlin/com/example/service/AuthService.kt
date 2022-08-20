package com.example.service

import com.example.domain.user.UserRepository
import com.example.exception.BaseException
import com.example.exception.ErrorType
import com.example.model.UserDto
import com.example.security.JwtProvider
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
) {

    fun login(name: String, password: String): UserDto {
        val user = userRepository.findByName(name) ?: throw BaseException(ErrorType.USER_NAME_NOT_FOUND)
        if (user.password != password) {
            throw BaseException(ErrorType.PASSWORD_NOT_MATCHES)
        }
        return UserDto.from(user)
    }

    fun createAccessToken(user: UserDto): String {
        return jwtProvider.createToken(user.toSecurityUser(), LocalDateTime.now())
    }

}
