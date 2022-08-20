package com.example.controller.dto

data class LoginRequest(
    val name: String,
    val password: String,
) {
    init {
        require(name.isNotBlank())
        require(password.isNotBlank())
    }
}

data class LoginResponse(
    val accessToken: String,
)
