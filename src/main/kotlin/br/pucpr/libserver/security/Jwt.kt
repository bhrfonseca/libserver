package br.pucpr.libserver.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import io.jsonwebtoken.jackson.io.JacksonSerializer
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

@Component
class Jwt {
    fun createToken(user: User) : String =
        UserToken(user).let {
            Jwts.builder()
                    .signWith(Keys.hmacShaKeyFor(SECRET.toByteArray()))
                    .json(JacksonSerializer())
                    .issuedAt(utcNow().toDate())
                    .expiration(utcNow().plusHours(EXPIRE_HOURS).toDate())
                    .issuer(ISSUER)
                    .subject(user.id.toString())
                    .claim(USER_FIELD, it)
                    .compact()
        }

    fun extract(req: HttpServletRequest): Authentication? {
        try {
            val header = req.getHeader(HttpHeaders.AUTHORIZATION)
            if (header == null || !header.startsWith("Bearer")) return null
            val token = header.replace("Bearer", "").trim()

            val claims = Jwts
                    .parser().verifyWith(Keys.hmacShaKeyFor(SECRET.toByteArray()))
                    .json(JacksonDeserializer(mapOf(USER_FIELD to UserToken::class.java)))
                    .build()
                    .parseSignedClaims(token)
                    .payload

            if (claims.issuer != ISSUER) return null

            return claims.get("user", UserToken::class.java).toAuthentication()

        } catch (e: Throwable) {
            log.debug("Token rejected", e)
            return null
        }
    }

    companion object {

        val log = LoggerFactory.getLogger(Jwt::class.java)
        const val SECRET = "e6f34622e103297f5c4f606aa29ad62648e2134f"
        const val EXPIRE_HOURS = 1L
        const val ISSUER = "LIBSERVER"
        const val USER_FIELD = "user"

        private fun utcNow() = ZonedDateTime.now(ZoneOffset.UTC)

        private fun ZonedDateTime.toDate(): Date = Date.from(this.toInstant())

        private fun UserToken.toAuthentication(): Authentication {
            val authorities = roles.map { SimpleGrantedAuthority("ROLE_$it") }
            return UsernamePasswordAuthenticationToken.authenticated(this, id, authorities)
        }

    }
}