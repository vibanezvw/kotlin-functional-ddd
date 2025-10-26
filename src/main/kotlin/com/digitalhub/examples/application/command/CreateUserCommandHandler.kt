package com.digitalhub.examples.application.command

import arrow.core.Either
import arrow.core.flatMap
import com.digitalhub.examples.domain.error.UserError
import com.digitalhub.examples.domain.model.user.*
import org.springframework.stereotype.Service

@Service
class CreateUserCommandHandler(
    private val userRepository: UserRepository
) {
    suspend fun handle(command: CreateUserCommand): Either<UserError, User> =
        Email.create(command.email)
            .flatMap { email -> 
                UserName.create(command.name).map { name -> email to name }
            }
            .flatMap { (email, name) ->
                checkEmailNotExists(email).map { email to name }
            }
            .flatMap { (email, name) ->
                val user = User.create(
                    id = UserId.generate(),
                    email = email,
                    name = name
                )
                userRepository.save(user)
            }
    
    private suspend fun checkEmailNotExists(email: Email): Either<UserError.AlreadyExists, Email> =
        if (userRepository.existsByEmail(email)) {
            Either.Left(UserError.AlreadyExists(email.value))
        } else {
            Either.Right(email)
        }
}