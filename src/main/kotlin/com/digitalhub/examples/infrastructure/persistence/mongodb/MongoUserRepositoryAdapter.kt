package com.digitalhub.examples.infrastructure.persistence.mongodb

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.digitalhub.examples.domain.error.UserError
import com.digitalhub.examples.domain.model.user.Email
import com.digitalhub.examples.domain.model.user.User
import com.digitalhub.examples.domain.model.user.UserId
import com.digitalhub.examples.domain.model.user.UserRepository
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Component

@Component
class MongoUserRepositoryAdapter(
    private val mongoRepository: MongoUserRepository
) : UserRepository {

    override suspend fun save(user: User): Either<UserError, User> =
        try {
            val document = UserDocument.from(user)
            mongoRepository.save(document)
                .awaitSingle()
                .toDomain()
                .right()
        } catch (e: Exception) {
            UserError.RepositoryError("Failed to save user: ${e.message}").left()
        }

    override suspend fun findById(id: UserId): Either<UserError.NotFound, User> =
        mongoRepository.findById(id.value)
            .awaitSingleOrNull()
            ?.toDomain()
            ?.right()
            ?: UserError.NotFound(id.value).left()

    override suspend fun findByEmail(email: Email): Either<UserError.NotFound, User> =
        mongoRepository.findByEmail(email.value)
            .awaitSingleOrNull()
            ?.toDomain()
            ?.right()
            ?: UserError.NotFound("User with email ${email.value}").left()

    override suspend fun existsByEmail(email: Email): Boolean =
        mongoRepository.existsByEmail(email.value).awaitSingle()

    override suspend fun deleteById(id: UserId): Either<UserError, Unit> =
        try {
            mongoRepository.deleteById(id.value).awaitSingleOrNull()
            Unit.right()
        } catch (e: Exception) {
            UserError.RepositoryError("Failed to delete user: ${e.message}").left()
        }

    override suspend fun findAll(): List<User> =
        mongoRepository.findAll()
            .collectList()
            .awaitSingle()
            .map { it.toDomain() }
}
