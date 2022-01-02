package com.github.cristianrb.identityservice.rest

import com.github.cristianrb.identityservice.model.UserDto
import com.github.cristianrb.identityservice.service.AuthService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
class HelloWorldController(private val authService: AuthService) {

    @GetMapping("hello")
    fun helloWorld(@RequestParam name: String) = "Hello $name"

    @PostMapping("createUser")
    fun createUser(@RequestBody userDto: UserDto) : Mono<UserDto> {
        return authService.createUser(userDto)
    }

}