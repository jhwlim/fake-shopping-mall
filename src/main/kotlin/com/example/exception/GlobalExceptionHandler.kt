package com.example.exception

import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val log = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): ResponseEntity<ErrorResponse> {
        log.error { e.message }
        return createErrorResponse(e.errorType)
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException::class)
    fun handleAccessDeniedException(e: org.springframework.security.access.AccessDeniedException): ResponseEntity<ErrorResponse> {
        log.error { e.message }
        return createErrorResponse(ErrorType.ACCESS_DENIED)
    }

    @ExceptionHandler
    fun handleAuthenticationException(e: AuthenticationException): ResponseEntity<ErrorResponse> {
        log.error { e.message }
        return createErrorResponse(ErrorType.AUTHENTICATION_REQUIRED)
    }

    @ExceptionHandler(IllegalArgumentException::class, HttpMessageNotReadableException::class)
    fun handleInvalidRequest(e: Exception): ResponseEntity<ErrorResponse> {
        log.error { e.message }
        return createErrorResponse(ErrorType.INVALID_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        e.printStackTrace()
        return createErrorResponse(ErrorType.UNKNOWN)
    }

    private fun createErrorResponse(errorType: ErrorType): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(errorType.httpStatus)
            .body(ErrorResponse.from(errorType))
    }

}
