package com.digitalhub.examples.infrastructure.rest

import com.digitalhub.examples.application.command.CreateUserCommandHandler
import com.digitalhub.examples.application.command.DeleteUserCommand
import com.digitalhub.examples.application.command.DeleteUserCommandHandler
import com.digitalhub.examples.application.query.*
import com.digitalhub.examples.domain.error.UserError
import com.digitalhub.examples.infrastructure.rest.dto.CreateUserRequest
import com.digitalhub.examples.infrastructure.rest.dto.ErrorResponse
import com.digitalhub.examples.infrastructure.rest.dto.UserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuarios", description = "API para gestión de usuarios")
class UserController(
    private val createUserCommandHandler: CreateUserCommandHandler,
    private val deleteUserCommandHandler: DeleteUserCommandHandler,
    private val findUserByIdQueryHandler: FindUserByIdQueryHandler,
    private val findAllUsersQueryHandler: FindAllUsersQueryHandler,
    private val findUserByEmailQueryHandler: FindUserByEmailQueryHandler
) {

    @PostMapping
    @Operation(
        summary = "Crear un nuevo usuario",
        description = "Crea un nuevo usuario con email y nombre. El email debe ser único."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Usuario creado exitosamente",
                content = [Content(schema = Schema(implementation = UserResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Datos de entrada inválidos",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "409",
                description = "El usuario ya existe",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
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
    @Operation(
        summary = "Obtener usuario por ID",
        description = "Busca y retorna un usuario por su identificador único"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Usuario encontrado",
                content = [Content(schema = Schema(implementation = UserResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    suspend fun getUserById(
        @Parameter(description = "ID del usuario", required = true)
        @PathVariable id: String
    ): ResponseEntity<Any> =
        findUserByIdQueryHandler.handle(FindUserByIdQuery(id))
            .fold(
                ifLeft = { error -> ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.from(error, HttpStatus.NOT_FOUND))
                },
                ifRight = { user -> ResponseEntity.ok(UserResponse.from(user)) }
            )

    @GetMapping
    @Operation(
        summary = "Obtener todos los usuarios",
        description = "Retorna una lista con todos los usuarios registrados"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Lista de usuarios obtenida exitosamente",
                content = [Content(schema = Schema(implementation = UserResponse::class))]
            )
        ]
    )
    suspend fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        val users = findAllUsersQueryHandler.handle(FindAllUsersQuery())
        return ResponseEntity.ok(users.map { UserResponse.from(it) })
    }

    @GetMapping("/by-email")
    @Operation(
        summary = "Obtener usuario por email",
        description = "Busca y retorna un usuario por su dirección de correo electrónico"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Usuario encontrado",
                content = [Content(schema = Schema(implementation = UserResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Email inválido",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    suspend fun getUserByEmail(
        @Parameter(description = "Email del usuario", required = true, example = "usuario@ejemplo.com")
        @RequestParam email: String
    ): ResponseEntity<Any> =
        findUserByEmailQueryHandler.handle(FindUserByEmailQuery(email))
            .fold(
                ifLeft = { error -> ResponseEntity
                    .status(mapErrorToStatus(error))
                    .body(ErrorResponse.from(error, mapErrorToStatus(error)))
                },
                ifRight = { user -> ResponseEntity.ok(UserResponse.from(user)) }
            )

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar usuario",
        description = "Elimina un usuario por su identificador único"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "Usuario eliminado exitosamente"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    suspend fun deleteUser(
        @Parameter(description = "ID del usuario", required = true)
        @PathVariable id: String
    ): ResponseEntity<Any> =
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
