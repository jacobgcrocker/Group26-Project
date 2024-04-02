package com.example.wat2eat.models

import java.sql.Timestamp

data class Review (
    val reviewId: String? = "",
    val recipeId: Int? = null,
    val userId: String? = "",
    val username: String? = "",
    val description: String? = "",
    val image: String? = "",
    val rating: Float?
)