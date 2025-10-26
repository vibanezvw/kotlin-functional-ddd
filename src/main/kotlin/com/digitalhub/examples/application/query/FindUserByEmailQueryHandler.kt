package com.digitalhub.examples.application.query

import arrow.core.Either
import arrow.core.flatMap
import com.digitalhub.examples.domain.error.UserError
import com.digitalhub.examples.domain.model.user.Email
import com.digitalhub.examples.domain.model.user.User
import com.digitalhub.examples.domain.model.user.UserRepository
import org.springframework.stereotype.Service

@Service
class FindUserByEmailQueryHandler(
    private val userRepository: UserRepository
) {
    suspend fun handle(query: FindUserByEmailQuery): Either<UserError, User> =
        Email.create(query.email)
            .flatMap { email -> userRepository.findByEmail(email) }
}
