package com.example.exception

import org.springframework.http.HttpStatus

enum class ErrorType(
    val code: Int,
    val message: String,
    val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
) {

    // 인증, 인가
    INVALID_AUTH_TOKEN(1001, "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    USER_NAME_NOT_FOUND(1002, "해당 이름의 사용자 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
    PASSWORD_NOT_MATCHES(1003, "패스워드 정보가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_AUTHORIZATION_HEADER_FORMAT(1004, "유효하지 않은 Authorization Header 형식입니다.", HttpStatus.UNAUTHORIZED),
    AUTHENTICATION_REQUIRED(1005, "인증이 필요한 요청입니다.", HttpStatus.FORBIDDEN),
    ACCESS_DENIED(1006, "접근 권한이 없는 사용자입니다.", HttpStatus.FORBIDDEN),

    INVALID_REQUEST(9001, "유효하지 않은 요청입니다."),
    UNKNOWN(9999, "알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR)

}
