package com.digitalhub.examples.infrastructure.persistence.mongodb

import com.digitalhub.examples.domain.model.user.User
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "users")
data class UserDocument(
    @Id
    val id: String,
    val email: String,
    val name: String,
    val createdAt: LocalDateTime
) {
    fun toDomain(): User = User(
        id = id,
        email = email,
        name = name,
        createdAt = createdAt
    )

    companion object {
        fun from(user: User): UserDocument = UserDocument(
            id = user.id,
            email = user.email,
            name = user.name,
            createdAt = user.createdAt
        )
    }
}
