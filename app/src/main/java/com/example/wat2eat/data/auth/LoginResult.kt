package com.example.wat2eat.data.auth

import androidx.annotation.StringRes
import com.example.wat2eat.models.User

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: User? = null,
    @StringRes
    val error: Int? = null
)