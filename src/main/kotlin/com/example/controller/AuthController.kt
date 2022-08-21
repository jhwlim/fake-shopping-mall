package com.example.controller

import com.example.common.Constants
import com.example.controller.dto.LoginRequest
import com.example.controller.dto.LoginResponse
import com.example.service.AuthService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

private val log = KotlinLogging.logger {}

@RestController
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): LoginResponse {
        log.info { "[POST, /login] request : $request" }
        val user = authService.login(request.name, request.password)
        val accessToken = authService.createAccessToken(user)
        return LoginResponse(
            user = LoginResponse.User.from(user),
            accessToken = accessToken,
            accessTokenType = Constants.AUTH_ACCESS_TOKEN_TYPE,
        )
    }

}
