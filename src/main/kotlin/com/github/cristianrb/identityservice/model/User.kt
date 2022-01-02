package com.github.cristianrb.identityservice.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(
    @Id
    val id: String?,
    val username: String,
    val password: String,
)
