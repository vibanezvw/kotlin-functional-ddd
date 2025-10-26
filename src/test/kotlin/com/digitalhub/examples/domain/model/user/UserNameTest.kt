package com.digitalhub.examples.domain.model.user

import arrow.core.Either
import com.digitalhub.examples.domain.error.UserError
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class UserNameTest : StringSpec({

    "should create valid user name" {
        val result = UserName.create("John Doe")

        result.shouldBeInstanceOf<Either.Right<UserName>>()
        result.getOrNull()?.value shouldBe "John Doe"
    }

    "should reject blank name" {
        val result = UserName.create("")

        result.shouldBeInstanceOf<Either.Left<UserError.InvalidName>>()
    }

    "should reject name too short" {
        val result = UserName.create("A")

        result.shouldBeInstanceOf<Either.Left<UserError.InvalidName>>()
    }

    "should reject name too long" {
        val result = UserName.create("A".repeat(101))

        result.shouldBeInstanceOf<Either.Left<UserError.InvalidName>>()
    }
})
