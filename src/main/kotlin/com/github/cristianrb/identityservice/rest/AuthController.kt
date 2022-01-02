package com.github.cristianrb.identityservice.rest

import com.github.cristianrb.identityservice.model.AuthUser
import com.github.cristianrb.identityservice.model.UserDto
import com.github.cristianrb.identityservice.service.AuthService
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.core.authority.AuthorityUtils
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority
import org.springframework.web.bind.annotation.PostMapping
import reactor.core.publisher.Mono
import java.util.*

@RestController
class AuthController(private val authService: AuthService) {

    @PostMapping("login")
    fun login(@RequestBody userDto: UserDto): Mono<AuthUser> {
        return authService.authenticateUser(userDto)
    }

}