package com.digitalhub.examples.domain.model.user

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.digitalhub.examples.domain.error.UserError

/**
 * Email Value Object with validation.
 * Ensures email format is valid using Railway-Oriented Programming.
 */
@JvmInline
value class Email private constructor(val value: String) {
    companion object {
        private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        
        /**
         * Smart constructor that validates email format.
         * Returns Either<UserError.InvalidEmail, Email>
         */
        fun create(value: String): Either<UserError.InvalidEmail, Email> =
            if (value.matches(EMAIL_REGEX)) Email(value).right()
            else UserError.InvalidEmail(value).left()
    }
}