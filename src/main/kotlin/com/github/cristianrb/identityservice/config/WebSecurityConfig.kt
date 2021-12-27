package com.github.cristianrb.identityservice.config

import com.github.cristianrb.identityservice.filter.JWTAuthorizationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.reactive.config.WebFluxConfigurer


@EnableWebFluxSecurity
@Configuration
class WebSecurityConfig: WebFluxConfigurer {

    companion object {
        val EXCLUDED_PATHS = arrayOf("/login")
    }

    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity
    ): SecurityWebFilterChain {
        return http.csrf().disable()
            .authorizeExchange()
            .pathMatchers(*EXCLUDED_PATHS).permitAll()
            .anyExchange().authenticated()
            .and()
            .addFilterAt(JWTAuthorizationFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
            .build()
    }

}