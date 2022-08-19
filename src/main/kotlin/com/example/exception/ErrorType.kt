package com.example.exception

import org.springframework.http.HttpStatus

enum class ErrorType(
    val code: Int,
    val message: String,
    val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
) {

    INVALID_AUTH_TOKEN(1001, "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),

    UNKNOWN(9999, "알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR)

}
