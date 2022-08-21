package com.example.model

import com.example.common.enums.UserRole
import com.example.domain.user.User
import com.example.security.SecurityUser

data class UserDto(
    val id: Long,
    val name: String,
    val role: UserRole,
) {

    companion object {

        fun from(user: User): UserDto {
            return with(user) {
                UserDto(
                    id = id,
                    name = name,
                    role = role,
                )
            }
        }

    }

    fun toSecurityUser(): SecurityUser {
        return SecurityUser(
            id = id,
            name = name,
            role = role,
        )
    }

}
