package com.example.wat2eat.models

interface Review {
    val reviewId: String
    val recipeId: Int?
    val userId: String
    val username: String
    val image: String?
    val rating: Float
    fun getDetails(): String
}

class BasicReview(
    override val reviewId: String = "",
    override val recipeId: Int? = null,
    override val userId: String = "",
    override val username: String = "",
    override val image: String? = null,
    override val rating: Float
) : Review {
    override fun getDetails(): String = ""
}
abstract class ReviewDecorator(protected val review: Review) : Review by review

class ReviewWithDescription(review: Review, private val description: String) : ReviewDecorator(review) {
    override fun getDetails(): String = super.getDetails() +  description
}

