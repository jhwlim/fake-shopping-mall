package com.example.security

import com.example.domain.user.User
import com.example.domain.user.UserRole

data class SecurityUser(
    val id: Long,
    val name: String,
    val role: UserRole,
) {
    companion object {
        fun from(user: User): SecurityUser {
            return with(user) {
                SecurityUser(
                    id = id,
                    name = name,
                    role = role
                )
            }
        }
    }
}
