package br.pucpr.libserver.security

data class LoginRequest(
        val name : String?,
        val password: String?
)
