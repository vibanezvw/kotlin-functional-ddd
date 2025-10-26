package com.digitalhub.examples.application.command

/**
 * Command to create a new user.
 * Commands represent intentions to change the system state (CQRS Write side).
 */
data class CreateUserCommand(
    val email: String,
    val name: String
)
