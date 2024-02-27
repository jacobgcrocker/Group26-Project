package com.example.wat2eat.api

import com.example.wat2eat.BuildConfig
import com.example.wat2eat.models.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface RecipeService {
    @GET("recipes/complexSearch")
    fun searchRecipes(
        @Query("query") q: String,
        @Query("apiKey") apiKey:String = BuildConfig.API_KEY,
        @Query("number") number: Int = 10,
        @Query("instructionsRequired") instr: Boolean = true

        ): Call<RecipeResponse>
    fun getRecipeInformation(
        @Query("id") id: Int,
        @Query("apiKey") apiKey:String = BuildConfig.API_KEY
    ): Call<Recipe>
}
