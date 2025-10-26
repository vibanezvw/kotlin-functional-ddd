package com.digitalhub.examples.infrastructure.rest.dto

import com.digitalhub.examples.application.command.CreateUserCommand
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Datos requeridos para crear un nuevo usuario")
data class CreateUserRequest(
    @Schema(
        description = "Dirección de correo electrónico del usuario",
        example = "usuario@ejemplo.com",
        required = true
    )
    val email: String,
    
    @Schema(
        description = "Nombre completo del usuario",
        example = "Juan Pérez",
        required = true
    )
    val name: String
) {
    fun toCommand(): CreateUserCommand = CreateUserCommand(
        email = email,
        name = name
    )
}
