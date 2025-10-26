package com.digitalhub.examples.domain.model.user

import arrow.core.Either
import com.digitalhub.examples.domain.error.UserError

/**
 * UserRepository interface (Domain Layer).
 * Defines the contract for User persistence using Railway-Oriented Programming.
 * Implementation will be in the Infrastructure layer.
 */
interface UserRepository {
    
    /**
     * Saves a user to the repository.
     * Returns Either<UserError, User> where Left represents failure and Right represents success.
     */
    suspend fun save(user: User): Either<UserError, User>
    
    /**
     * Finds a user by ID.
     * Returns Either<UserError.NotFound, User>
     */
    suspend fun findById(id: UserId): Either<UserError.NotFound, User>
    
    /**
     * Finds a user by email.
     * Returns Either<UserError.NotFound, User>
     */
    suspend fun findByEmail(email: Email): Either<UserError.NotFound, User>
    
    /**
     * Checks if a user with the given email already exists.
     */
    suspend fun existsByEmail(email: Email): Boolean
    
    /**
     * Deletes a user by ID.
     * Returns Either<UserError, Unit>
     */
    suspend fun deleteById(id: UserId): Either<UserError, Unit>
    
    /**
     * Finds all users.
     * Returns a list of users (empty list if none found).
     */
    suspend fun findAll(): List<User>
}