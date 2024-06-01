package br.pucpr.libserver.security

data class LoginResponse (
        val token: String,
        val user: UserResponse
        )
