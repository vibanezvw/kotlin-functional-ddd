package com.digitalhub.examples.infrastructure.rest

import com.digitalhub.examples.application.command.CreateUserCommandHandler
import com.digitalhub.examples.application.command.DeleteUserCommand
import com.digitalhub.examples.application.command.DeleteUserCommandHandler
import com.digitalhub.examples.application.query.*
import com.digitalhub.examples.domain.error.UserError
import com.digitalhub.examples.infrastructure.rest.dto.CreateUserRequest
import com.digitalhub.examples.infrastructure.rest.dto.ErrorResponse
import com.digitalhub.examples.infrastructure.rest.dto.UserResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val createUserCommandHandler: CreateUserCommandHandler,
    private val deleteUserCommandHandler: DeleteUserCommandHandler,
    private val findUserByIdQueryHandler: FindUserByIdQueryHandler,
    private val findAllUsersQueryHandler: FindAllUsersQueryHandler,
    private val findUserByEmailQueryHandler: FindUserByEmailQueryHandler
) {

    @PostMapping
    suspend fun createUser(@RequestBody request: CreateUserRequest): ResponseEntity<Any> =
        createUserCommandHandler.handle(request.toCommand())
            .fold(
                ifLeft = { error -> ResponseEntity
                    .status(mapErrorToStatus(error))
                    .body(ErrorResponse.from(error, mapErrorToStatus(error)))
                },
                ifRight = { user -> ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(UserResponse.from(user))
                }
            )

    @GetMapping("/{id}")
    suspend fun getUserById(@PathVariable id: String): ResponseEntity<Any> =
        findUserByIdQueryHandler.handle(FindUserByIdQuery(id))
            .fold(
                ifLeft = { error -> ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.from(error, HttpStatus.NOT_FOUND))
                },
                ifRight = { user -> ResponseEntity.ok(UserResponse.from(user)) }
            )

    @GetMapping
    suspend fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        val users = findAllUsersQueryHandler.handle(FindAllUsersQuery())
        return ResponseEntity.ok(users.map { UserResponse.from(it) })
    }

    @GetMapping("/by-email")
    suspend fun getUserByEmail(@RequestParam email: String): ResponseEntity<Any> =
        findUserByEmailQueryHandler.handle(FindUserByEmailQuery(email))
            .fold(
                ifLeft = { error -> ResponseEntity
                    .status(mapErrorToStatus(error))
                    .body(ErrorResponse.from(error, mapErrorToStatus(error)))
                },
                ifRight = { user -> ResponseEntity.ok(UserResponse.from(user)) }
            )

    @DeleteMapping("/{id}")
    suspend fun deleteUser(@PathVariable id: String): ResponseEntity<Any> =
        deleteUserCommandHandler.handle(DeleteUserCommand(id))
            .fold(
                ifLeft = { error -> ResponseEntity
                    .status(mapErrorToStatus(error))
                    .body(ErrorResponse.from(error, mapErrorToStatus(error)))
                },
                ifRight = { ResponseEntity.noContent().build<Any>() }
            )

    private fun mapErrorToStatus(error: UserError): HttpStatus = when (error) {
        is UserError.NotFound -> HttpStatus.NOT_FOUND
        is UserError.AlreadyExists -> HttpStatus.CONFLICT
        is UserError.InvalidEmail, is UserError.InvalidName -> HttpStatus.BAD_REQUEST
        is UserError.RepositoryError -> HttpStatus.INTERNAL_SERVER_ERROR
    }
}
