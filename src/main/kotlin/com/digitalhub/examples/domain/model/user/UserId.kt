package com.digitalhub.examples.domain.model.user

import java.util.UUID

/**
 * UserId Value Object.
 * Represents a unique identifier for a User aggregate.
 */
@JvmInline
value class UserId(val value: String) {
    companion object {
        /**
         * Generates a new unique UserId.
         */
        fun generate(): UserId = UserId(UUID.randomUUID().toString())
        
        /**
         * Creates a UserId from an existing string value.
         */
        fun from(value: String): UserId = UserId(value)
    }
}