package com.github.cristianrb.identityservice.service

import com.github.cristianrb.identityservice.model.AuthUser
import com.github.cristianrb.identityservice.model.UserDto
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
interface AuthService {

    fun authenticateUser(user: UserDto) : Mono<AuthUser>

    fun createUser(userDto: UserDto) : Mono<UserDto>
}