package br.pucpr.libserver.security

import com.fasterxml.jackson.annotation.JsonIgnore

data class UserToken(
        val id: Long,
        val name: String,
        val roles: List<String>
) {
    constructor(): this(0, "", emptyList())
    constructor(user: User): this(
            id = user.id!!,
            name = user.name,
            roles = listOf(user.role)
    )

    @get:JsonIgnore
    val isAdmin: Boolean get() = "ADMIN" in roles
}
