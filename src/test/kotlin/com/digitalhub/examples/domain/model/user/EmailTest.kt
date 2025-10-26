package com.digitalhub.examples.domain.model.user

import arrow.core.Either
import com.digitalhub.examples.domain.error.UserError
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class EmailTest : StringSpec({

    "should create valid email" {
        val result = Email.create("test@example.com")

        result.shouldBeInstanceOf<Either.Right<Email>>()
        result.getOrNull()?.value shouldBe "test@example.com"
    }

    "should reject invalid email format" {
        val result = Email.create("invalid-email")

        result.shouldBeInstanceOf<Either.Left<UserError.InvalidEmail>>()
    }

    "should reject blank email" {
        val result = Email.create("")

        result.shouldBeInstanceOf<Either.Left<UserError.InvalidEmail>>()
    }
})
