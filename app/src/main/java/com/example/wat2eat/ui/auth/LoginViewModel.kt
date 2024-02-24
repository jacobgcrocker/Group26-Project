package com.example.wat2eat.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.wat2eat.R
import com.example.wat2eat.data.auth.AuthResult
import com.example.wat2eat.models.Result
import com.example.wat2eat.data.auth.UserRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.launch


class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<AuthFormState>()
    val loginFormState: LiveData<AuthFormState> = _loginForm

    private val _loginResult = MutableLiveData<AuthResult>()
    val loginResult: LiveData<AuthResult> = _loginResult

    /**
     * attempts to login user with provided [email] and [password], updates [loginResult] to notify
     * [LoginFragment] of results
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.login(email, password)

            if (result is Result.Success) {
                _loginResult.value =
                    AuthResult(success = result.data)
            } else if (result is Result.Error) {
                // then an error occurred
                when (result.exception) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        _loginResult.value = AuthResult(error = R.string.login_incorrect_credentials)
                    }
                    else -> _loginResult.value = AuthResult(error = R.string.login_failed)
                }
            }
        }
    }

    /**
     * sign in form validation for login fragment
     */
    fun loginDataChanged(email: String) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _loginForm.value = AuthFormState(emailError = R.string.invalid_email)
        } else {
            _loginForm.value = AuthFormState(isDataValid = true)
        }
    }
}
