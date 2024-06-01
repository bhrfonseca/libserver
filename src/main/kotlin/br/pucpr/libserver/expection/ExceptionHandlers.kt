package br.pucpr.libserver.expection

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class ExceptionHandlers {

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbiddenException(ex: ForbiddenException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.message)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<String> =
            ex.bindingResult.allErrors
                    .joinToString("\n") { "${(it as FieldError).field}: ${it.defaultMessage}" }
                    .let { ResponseEntity.status(HttpStatus.FORBIDDEN).body(it) }
}