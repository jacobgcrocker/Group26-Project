package com.example.wat2eat.data.auth

import com.example.wat2eat.models.Result
import com.example.wat2eat.models.User

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class UserRepository(val dataSource: AuthDataSource) {

    private var user: User? = null

    suspend fun login(username: String, password: String): Result<User> {
        // handle login
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    fun isLoggedIn(): Boolean {
        return user != null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    private fun setLoggedInUser(user: User) {
        this.user = user
    }
}
