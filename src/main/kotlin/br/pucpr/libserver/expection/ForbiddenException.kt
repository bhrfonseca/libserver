package br.pucpr.libserver.expection

import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(FORBIDDEN)
class ForbiddenException(
        message: String = "Forbidden",
        cause: Throwable? = null
) : IllegalArgumentException(message, cause)