package com.digitalhub.examples.infrastructure.rest.dto

import com.digitalhub.examples.domain.error.UserError
import org.springframework.http.HttpStatus

data class ErrorResponse(
    val message: String,
    val code: String,
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
