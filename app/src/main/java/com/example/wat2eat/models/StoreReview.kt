package com.example.wat2eat.models

data class StoreReview(
    val reviewId: String? = null,
    val userId: String? = null,
    val rating: Float,
    val content: String? = null
)