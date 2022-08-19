package com.example.exception

import org.springframework.http.HttpStatus

enum class ErrorType(
    val code: Int,
    val message: String,
    val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
) {

    UNKNOWN(9999, "알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR)

}
