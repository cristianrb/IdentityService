package com.github.cristianrb.identityservice.filter

import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder

class JWTAuthorizationFilter : WebFilter {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val authHeader = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION) ?: return chain.filter(exchange)

        if (!authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange)
        }

        try {
            val claims = decodeToken(exchange)
            val authorities = claims["authorities"] as List<String>
            val auth = UsernamePasswordAuthenticationToken(claims.subject, null, authorities.map {
                SimpleGrantedAuthority(it)
            })
            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))
        } catch (e: Exception) {
            logger.error("JWT exception", e)
        }

        return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.clearContext())
    }

    private fun decodeToken(exchange: ServerWebExchange): Claims {
        val token = exchange.request.headers[HttpHeaders.AUTHORIZATION]!![0].replace("Bearer ", "")
        return Jwts.parser().setSigningKey("mySecretKey".toByteArray()).parseClaimsJws(token).body
    }
}