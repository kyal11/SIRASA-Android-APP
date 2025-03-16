package com.dev.sirasa.data.remote.response.user

data class UpdateAccount (
    val name: String? = null,
    val nim: String? = null,
    val phoneNumber: String? = null,
    val imageUrl: Any? = null,
    val password: String? = null,
    val verified: Boolean? = null,
    val role: String? = null,
    val email: String? = null
)

data class CreateUser(
    val name: String,
    val email: String,
    val password: String,
    val nim: String,
    val phoneNumber: String,
    val verified: Boolean,
    val role: String? = "user"
)