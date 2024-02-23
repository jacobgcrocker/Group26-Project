package com.example.wat2eat.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.wat2eat.R
import com.example.wat2eat.data.auth.LoginResult
import com.example.wat2eat.models.Result
import com.example.wat2eat.data.auth.UserRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.launch


class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    /**
     * attempts to login user with provided [email] and [password], updates [loginResult] to notify
     * [LoginFragment] of results
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.login(email, password)

            if (result is Result.Success) {
                _loginResult.value =
                    LoginResult(success = result.data)
            } else if (result is Result.Error) {
                // then an error occurred
                when (result.exception) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        _loginResult.value = LoginResult(error = R.string.login_incorrect_credentials)
                    }
                    else -> _loginResult.value = LoginResult(error = R.string.login_failed)
                }
            }
        }
    }

    /**
     * sign in form validation for login fragment
     */
    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_email)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // email checking
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }
}
