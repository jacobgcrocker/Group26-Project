package com.example.wat2eat.api

import com.example.wat2eat.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface RecipeService {
    @GET("api/recipes/v2")
    fun searchRecipes(
        @Query("type")
        type:String = "public",
        @Query("q") q: String,
        @Query("app_id") app_id:String = BuildConfig.APP_ID,
        @Query("app_key") app_key:String = BuildConfig.APP_KEY,

        ): Call<RecipeResponse>
}
