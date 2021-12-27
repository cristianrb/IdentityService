package com.github.cristianrb.identityservice.rest

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorldController {

    @RequestMapping("hello")
    fun helloWorld(@RequestParam name: String) = "Hello $name"

}