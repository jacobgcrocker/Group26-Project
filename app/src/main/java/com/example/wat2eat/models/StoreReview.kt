package com.example.wat2eat.models

data class StoreReview(
    val reviewId: Int? = null,
    val recipeId: Int? = null,
    val username: String? = null,
    val userId: String? = null,
    val rating: Float,
    val content: String? = null
)