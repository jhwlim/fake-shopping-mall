package com.example.controller.dto

import com.example.common.enums.UserRole
import com.example.model.UserDto

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
    val user: User,
    val accessToken: String,
    val accessTokenType: String,
) {
    data class User(
        val id: Long,
        val name: String,
        val role: UserRole,
    ) {

        companion object {

            fun from(user: UserDto): User {
                return with(user) {
                    User(
                        id = id,
                        name = name,
                        role = role,
                    )
                }
            }

        }

    }

}
