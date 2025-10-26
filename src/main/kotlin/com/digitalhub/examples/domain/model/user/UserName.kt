package com.digitalhub.examples.domain.model.user

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.digitalhub.examples.domain.error.UserError

/**
 * UserName Value Object with validation.
 * Ensures user name meets business rules using Railway-Oriented Programming.
 */
@JvmInline
value class UserName private constructor(val value: String) {
    companion object {
        private const val MIN_LENGTH = 2
        private const val MAX_LENGTH = 100
        
        /**
         * Smart constructor that validates user name.
         * Returns Either<UserError.InvalidName, UserName>
         */
        fun create(value: String): Either<UserError.InvalidName, UserName> =
            when {
                value.isBlank() -> UserError.InvalidName("Name cannot be blank").left()
                value.length < MIN_LENGTH -> UserError.InvalidName("Name must have at least $MIN_LENGTH characters").left()
                value.length > MAX_LENGTH -> UserError.InvalidName("Name cannot exceed $MAX_LENGTH characters").left()
                else -> UserName(value).right()
            }
    }
}