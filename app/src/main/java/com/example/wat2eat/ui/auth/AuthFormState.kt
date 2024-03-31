package com.example.wat2eat.ui.auth

import androidx.annotation.StringRes

/**
 * Data validation state of the login/sign-up form.
 */
data class AuthFormState(
    @StringRes
    val emailError: Int? = null,
    @StringRes
    val passwordError: Int? = null,
    @StringRes
    val rePasswordError: Int? = null,
    val isDataValid: Boolean = false
)