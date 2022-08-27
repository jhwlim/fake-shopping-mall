package com.example.utils

import com.example.exception.ErrorResponse
import com.example.exception.ErrorType
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import java.nio.charset.StandardCharsets
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.HttpServletResponse

val objectMapper = ObjectMapper()

fun LocalDateTime.toDate(): Date = Timestamp.valueOf(this)

fun HttpServletResponse.writeErrorResponseBody(errorType: ErrorType) {
    contentType = MediaType.APPLICATION_JSON_VALUE
    characterEncoding = StandardCharsets.UTF_8.name()
    status = errorType.httpStatus.value()
    val body = ErrorResponse.from(errorType)
    writer.print(objectMapper.writeValueAsString(body))
}
