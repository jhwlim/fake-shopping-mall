package com.example.exception

import org.springframework.http.HttpStatus

enum class ErrorType(
    val code: Int,
    val message: String,
    val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
) {

    INVALID_AUTH_TOKEN(1001, "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(1002, "사용자 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
    PASSWORD_NOT_MATCHES(1003, "패스워드 정보가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),

    UNKNOWN(9999, "알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR)

}
