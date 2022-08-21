package com.example.domain.user

import com.example.common.enums.UserRole
import com.example.domain.BaseEntity
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val name: String,
    val password: String,
    @Enumerated(EnumType.STRING)
    val role: UserRole,
) : BaseEntity()
