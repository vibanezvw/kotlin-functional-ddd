package com.digitalhub.examples.application.query

import arrow.core.Either
import com.digitalhub.examples.domain.error.UserError
import com.digitalhub.examples.domain.model.user.User
import com.digitalhub.examples.domain.model.user.UserId
import com.digitalhub.examples.domain.model.user.UserRepository
import org.springframework.stereotype.Service

@Service
class FindUserByIdQueryHandler(
    private val userRepository: UserRepository
) {
    suspend fun handle(query: FindUserByIdQuery): Either<UserError.NotFound, User> =
        userRepository.findById(UserId.from(query.userId))
}
