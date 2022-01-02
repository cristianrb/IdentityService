package com.github.cristianrb.identityservice.service.impl

import com.github.cristianrb.identityservice.exceptions.UsetNotFoundException
import com.github.cristianrb.identityservice.model.AuthUser
import com.github.cristianrb.identityservice.model.User
import com.github.cristianrb.identityservice.model.UserDto
import com.github.cristianrb.identityservice.repository.AuthRepository
import com.github.cristianrb.identityservice.service.AuthService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.util.*

@Service
class AuthServiceImpl(private val authRepository: AuthRepository) : AuthService {

    companion object {
        private const val SECRET_KEY = "mySecretKey"
        private const val AUTH_ID = "cristianrbJWT"
    }

    override fun authenticateUser(userDto: UserDto): Mono<AuthUser> {
        return authRepository.findByUsernameAndPassword(userDto.username, userDto.password)
            .flatMap {
                val token = getJWTToken(userDto)
                Mono.just(AuthUser(userDto.username, token))
            }
            .switchIfEmpty { Mono.error(UsetNotFoundException("Invalid credentials.")) }
    }

    override fun createUser(userDto: UserDto) : Mono<UserDto> {
        val user = User(null, userDto.username, userDto.password)
        return authRepository.save(user).map {
            UserDto(it.username, it.password)
        }
    }
    private fun getJWTToken(userDto: UserDto): String {

        val grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER")

        val token = Jwts
            .builder()
            .setId(AUTH_ID)
            .setSubject(userDto.username)
            .claim("authorities", grantedAuthorities.map(GrantedAuthority::getAuthority))
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 3600*1000))
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY.toByteArray())
            .compact()

        return "Bearer $token"
    }
}