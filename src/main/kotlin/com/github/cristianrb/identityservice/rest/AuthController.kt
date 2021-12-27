package com.github.cristianrb.identityservice.rest

import com.github.cristianrb.identityservice.model.AuthUser
import com.github.cristianrb.identityservice.model.User
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.core.authority.AuthorityUtils
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority
import org.springframework.web.bind.annotation.PostMapping
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.*

@RestController
class AuthController {

    companion object {
        private const val SECRET_KEY = "mySecretKey"
        private const val AUTH_ID = "cristianrbJWT"
    }

    @PostMapping("login")
    fun login(@RequestBody user: User): Mono<AuthUser> {
        val token = getJWTToken(user)
        return Mono.just(AuthUser(user.username, token))
    }

    private fun getJWTToken(user: User): String {

        val grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER")

        val token = Jwts
            .builder()
            .setId(AUTH_ID)
            .setSubject(user.username)
            .claim("authorities", grantedAuthorities.map(GrantedAuthority::getAuthority))
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 3600*1000))
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY.toByteArray())
            .compact()

        return "Bearer $token"
    }


}