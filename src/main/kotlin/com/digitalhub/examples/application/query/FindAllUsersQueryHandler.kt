package com.digitalhub.examples.application.query

import com.digitalhub.examples.domain.model.user.User
import com.digitalhub.examples.domain.model.user.UserRepository
import org.springframework.stereotype.Service

@Service
class FindAllUsersQueryHandler(
    private val userRepository: UserRepository
) {
    suspend fun handle(query: FindAllUsersQuery): List<User> =
        userRepository.findAll()
}
