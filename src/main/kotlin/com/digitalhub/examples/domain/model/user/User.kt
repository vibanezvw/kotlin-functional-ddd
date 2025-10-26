package com.digitalhub.examples.domain.model.user

import java.time.LocalDateTime

data class User(
    val id: String,
    val email: String,
    val name: String,
    val createdAt: LocalDateTime
) {

    companion object {
        fun create(id: String, email: String, name: String): User {
            return User(id, email, name, LocalDateTime.now())
        }
    }
}