package com.github.cristianrb.identityservice.repository

import com.github.cristianrb.identityservice.model.User
import org.springframework.stereotype.Repository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono

@Repository
interface AuthRepository : ReactiveMongoRepository<User, String> {

    fun findByUsernameAndPassword(username: String, password: String): Mono<User>
}