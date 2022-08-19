package com.example.exception

import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val log = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): ResponseEntity<ErrorResponse> {
        log.error { e.message }
        return ResponseEntity.status(e.errorType.httpStatus)
            .body(ErrorResponse.from(e.errorType))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        log.error { e.message }
        return ResponseEntity.internalServerError()
            .body(ErrorResponse.from(ErrorType.UNKNOWN))
    }

}
