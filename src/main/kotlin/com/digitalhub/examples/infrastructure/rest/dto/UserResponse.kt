package com.digitalhub.examples.infrastructure.rest.dto

import com.digitalhub.examples.domain.model.user.User
import java.time.LocalDateTime

data class UserResponse(
    val id: String,
    val email: String,
    val name: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(user: User): UserResponse = UserResponse(
            id = user.id,
            email = user.email,
            name = user.name,
            createdAt = user.createdAt
        )
    }
}
