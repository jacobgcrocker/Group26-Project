package com.example.wat2eat.ui.auth

import android.util.Log
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.launch


class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _signUpForm = MutableLiveData<AuthFormState>()
    val signUpFormState: LiveData<AuthFormState> = _signUpForm

    private val _signUpResult = MutableLiveData<AuthResult>()
    val signUpResult: LiveData<AuthResult> = _signUpResult

    /**
     * attempts to login user with provided [email] and [password], updates [signUpResult] to notify
     * [LoginFragment] of results
     */
    fun signUp(username: String, email: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.signUp(username, email, password)

            if (result is Result.Success) {
                _signUpResult.value =
                    AuthResult(success = result.data)
            } else if (result is Result.Error) {
                // then an error occurred
                when (result.exception) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        Log.d(
                            "SignUpViewModel",
                            "FirebaseAuthInvalidCredentialsException occurred ${result.exception.message}"
                        )
                        _signUpResult.value =
                            AuthResult(error = R.string.login_incorrect_credentials)
                    }

                    is FirebaseAuthUserCollisionException -> {
                        _signUpResult.value = AuthResult(error = R.string.sign_up_email_in_use)
                    }

                    else -> _signUpResult.value = AuthResult(error = R.string.login_failed)
                }
            }
        }
    }

    /**
     * sign up form validation for sign up fragment
     */
    fun signUpFormDataChanged(email: String, password: String, rePassword: String) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _signUpForm.value = AuthFormState(emailError = R.string.invalid_email)
        } else if (password.length < 6) {
            _signUpForm.value = AuthFormState(passwordError = R.string.invalid_password)
        } else if (rePassword != password) {
            _signUpForm.value = AuthFormState(rePasswordError = R.string.invalid_re_password)
        } else {
            _signUpForm.value = AuthFormState(isDataValid = true)
        }
    }
}
