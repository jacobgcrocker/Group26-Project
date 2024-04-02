package com.example.wat2eat.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val RECIPE_BASE_URL = "https://api.spoonacular.com/"
    private const val BACKEND_BASE_URL = "http://10.0.2.2:3000/"

    val recipeServiceInstance: RecipeService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(RECIPE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(RecipeService::class.java)
    }

    val userServiceInstance: UserService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BACKEND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(UserService::class.java)
    }

    val reviewServiceInstance: ReviewService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BACKEND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ReviewService::class.java)
    }
}
