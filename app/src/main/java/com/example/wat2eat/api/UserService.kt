package com.example.wat2eat.api

import com.example.wat2eat.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @POST("userData")
    fun appendUser(
        @Body jsonBody: HashMap<String, String>,
    ): Call<User>

    @GET("userData")
    fun retrieveUser(
        @Query("uid") userId: String,
        ): Call<User>
}