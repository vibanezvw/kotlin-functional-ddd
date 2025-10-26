package com.digitalhub.examples.infrastructure.rest.dto

import com.digitalhub.examples.application.command.CreateUserCommand

data class CreateUserRequest(
    val email: String,
    val name: String
) {
    fun toCommand(): CreateUserCommand = CreateUserCommand(
        email = email,
        name = name
    )
}
