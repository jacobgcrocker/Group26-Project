package com.example.wat2eat.api

import com.example.wat2eat.models.Review
import com.example.wat2eat.models.StoreReview
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.Body
interface ReviewService {
    @GET("/review")
    suspend fun getReviewsByRecipeId(@Query("recipeId") recipeId: String): List<StoreReview>
    @POST("/review")
    suspend fun postReview(@Body newReview: StoreReview): Response<StoreReview>
}