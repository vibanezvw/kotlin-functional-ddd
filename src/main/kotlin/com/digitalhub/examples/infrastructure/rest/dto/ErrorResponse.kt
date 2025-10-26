package com.digitalhub.examples.infrastructure.rest.dto

import com.digitalhub.examples.domain.error.UserError
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus

@Schema(description = "Respuesta de error")
data class ErrorResponse(
    @Schema(description = "Mensaje de error descriptivo", example = "El usuario no fue encontrado")
    val message: String,
    
    @Schema(description = "Código de error", example = "NotFound")
    val code: String,
    
    @Schema(description = "Código de estado HTTP", example = "404")
    val status: Int
) {
    companion object {
        fun from(error: UserError, status: HttpStatus): ErrorResponse = ErrorResponse(
            message = when (error) {
                is UserError.InvalidEmail -> error.message
                is UserError.InvalidName -> error.message
                is UserError.NotFound -> error.message
                is UserError.AlreadyExists -> error.message
                is UserError.RepositoryError -> error.message
            },
            code = error::class.simpleName ?: "UNKNOWN_ERROR",
            status = status.value()
        )
    }
}
