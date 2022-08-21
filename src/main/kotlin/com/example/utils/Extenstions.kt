package com.example.utils

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

fun LocalDateTime.toDate(): Date = Timestamp.valueOf(this)
