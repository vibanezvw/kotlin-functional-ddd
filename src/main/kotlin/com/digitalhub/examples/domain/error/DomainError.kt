package com.digitalhub.examples.domain.error

/**
 * Base interface for all domain errors.
 * Errors are part of the domain model and represent business failures.
 */
sealed interface DomainError {
    val message: String
    val code: String
}

/**
 * User-specific domain errors.
 * These represent all possible failures in the User domain.
 */
sealed interface UserError : DomainError {
    
    /**
     * Error when attempting to create a user with an email that already exists.
     */
    data class AlreadyExists(val email: String) : UserError {
        override val message = "User with email $email already exists"
        override val code = "USER_ALREADY_EXISTS"
    }
    
    /**
     * Error when email format is invalid.
     */
    data class InvalidEmail(val email: String) : UserError {
        override val message = "Invalid email format: $email"
        override val code = "INVALID_EMAIL"
    }
    
    /**
     * Error when user name is invalid.
     */
    data class InvalidName(override val message: String) : UserError {
        override val code = "INVALID_NAME"
    }
    
    /**
     * Error when user is not found by ID.
     */
    data class NotFound(val userId: String) : UserError {
        override val message = "User not found: $userId"
        override val code = "USER_NOT_FOUND"
    }
    
    /**
     * Generic repository error.
     */
    data class RepositoryError(override val message: String) : UserError {
        override val code = "REPOSITORY_ERROR"
    }
}