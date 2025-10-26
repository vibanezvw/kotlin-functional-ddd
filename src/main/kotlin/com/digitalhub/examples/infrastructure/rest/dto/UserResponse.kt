package com.digitalhub.examples.infrastructure.rest.dto

import com.digitalhub.examples.domain.model.user.User
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Respuesta con datos del usuario")
data class UserResponse(
    @Schema(description = "Identificador único del usuario", example = "507f1f77bcf86cd799439011")
    val id: String,
    
    @Schema(description = "Dirección de correo electrónico del usuario", example = "usuario@ejemplo.com")
    val email: String,
    
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    val name: String,
    
    @Schema(description = "Fecha y hora de creación del usuario", example = "2024-01-15T10:30:00")
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
