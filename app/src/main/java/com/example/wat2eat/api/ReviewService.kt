package com.example.wat2eat.api

import com.example.wat2eat.models.Review
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
interface ReviewService {
    @GET("/review")
    fun getReviewsByRecipeId(@Query("recipeId") recipeId: String): List<Review>
}