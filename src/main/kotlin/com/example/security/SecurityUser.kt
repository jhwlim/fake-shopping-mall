package com.example.security

import com.example.domain.user.UserRole

class SecurityUser(
    val id: Long,
    val name: String,
    val role: UserRole,
)
