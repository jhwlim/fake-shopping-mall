package com.example.exception

class BaseException(
    val errorType: ErrorType,
) : RuntimeException(errorType.message)
