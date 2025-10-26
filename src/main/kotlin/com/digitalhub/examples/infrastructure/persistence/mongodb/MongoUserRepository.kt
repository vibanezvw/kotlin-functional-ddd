package com.digitalhub.examples.infrastructure.persistence.mongodb

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface MongoUserRepository : ReactiveMongoRepository<UserDocument, String> {
    fun findByEmail(email: String): Mono<UserDocument>
    fun existsByEmail(email: String): Mono<Boolean>
}
