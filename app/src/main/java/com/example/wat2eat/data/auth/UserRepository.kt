package com.example.wat2eat.data.auth

import com.example.wat2eat.models.Result
import com.example.wat2eat.models.User

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class UserRepository private constructor() {

    // Singleton implementation
    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository().also { instance = it }
            }

    }
    private val dataSource = AuthDataSource()
    private var user: User? = null

    suspend fun login(email: String, password: String): Result<User> {
        val result = dataSource.login(email, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    suspend fun signUp(username: String, email: String, password: String): Result<User> {
        val result = dataSource.signUp(username, email, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

//    fun isLoggedIn(): Boolean {
//        return user != null
//    }

//    fun logout() {
//        user = null
//        dataSource.logout()
//    }

    private fun setLoggedInUser(user: User) {
        this.user = user
    }
    fun getLoggedInUser(): User? {
        return this.user
    }
}
