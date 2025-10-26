package com.digitalhub.examples.application.command

import arrow.core.Either
import com.digitalhub.examples.domain.error.UserError
import com.digitalhub.examples.domain.model.user.UserId
import com.digitalhub.examples.domain.model.user.UserRepository
import org.springframework.stereotype.Service

@Service
class DeleteUserCommandHandler(
    private val userRepository: UserRepository
) {
    suspend fun handle(command: DeleteUserCommand): Either<UserError, Unit> =
        userRepository.deleteById(UserId.from(command.userId))
}
