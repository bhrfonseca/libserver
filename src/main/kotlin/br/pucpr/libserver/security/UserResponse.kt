package br.pucpr.libserver.security

data class UserResponse(
        val id: Long,
        val name: String,
) {
    constructor(u: User): this(
            id= u.id!!,
            name= u.name,
    )
}
