package com.example.wat2eat.models

import java.sql.Timestamp

data class Review (
    val reviewId: String? = "",
    val userId: String? = "",
    val username: String? = "",
    val description: String? = "",
    val image: String? = "",
    val timestamp: Long? = null,
    val rating: Float?
)