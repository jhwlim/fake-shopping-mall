package com.example.security

import com.example.common.enums.UserRole

data class SecurityUser(
    val id: Long,
    val name: String,
    val role: UserRole,
)
