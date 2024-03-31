package com.example.wat2eat.data.auth

import android.util.Log
import com.example.wat2eat.api.RetrofitClient
import com.example.wat2eat.models.Result
import com.example.wat2eat.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Handles Firebase authentication API calls
 */
class AuthDataSource {

    companion object {
        private const val TAG = "LoginLogic"
    }

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * attempts to login user with provided [email] and [password], returns Result with either
     * User object or Exception.
     */
    suspend fun login(email: String, password: String): Result<User> {
        // log in with email
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            return when (result) {
                is Result.Success -> {
                    Log.d(TAG, "Firebase Auth user login successful")
                    val user = auth.currentUser?.let { retrieveUser(it) }
                    if (user != null) {
                        Result.Success(user)
                    } else {
                        Result.Error(IllegalStateException("Log in successful but user not found"))
                    }
                }
                is Result.Error -> {
                    Result.Error(result.exception)
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error occurred when calling signInWithEmailAndPassword", e)
            return Result.Error(e)
        }
    }

    /**
     * attempts to sign up user with provided [email] and [password] with Firebase Auth. If
     * successful, append new User object to database with [username], returns Result with either
     * User object or Exception.
     */
    suspend fun signUp(username: String, email: String, password: String): Result<User> {
        // sign up with email
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            return when (result) {
                is Result.Success -> {
                    Log.d(TAG, "Firebase Auth user sign up successful")
                    val user = auth.currentUser?.let { appendUser(it, username) }
                    if (user != null) {
                        Result.Success(user)
                    } else {
                        Result.Error(IllegalStateException("Sign up successful but user not found"))
                    }
                }
                is Result.Error -> {
                    Result.Error(result.exception)
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error occurred when calling createUserWithEmailAndPassword", e)
            return Result.Error(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    private fun retrieveUser(firebaseUser: FirebaseUser): User? {
        var response : User? = null
        runBlocking {
            withContext(Dispatchers.IO) {
                val call = RetrofitClient.userServiceInstance.retrieveUser(firebaseUser.uid)
                response = call.execute().body()
            }
        }
        return response
    }


    private fun appendUser(firebaseUser: FirebaseUser, username: String): User? {
        val email: String = firebaseUser.email.orEmpty()
        val body = HashMap<String, String>();
        body["email"] = email
        body["userId"] = firebaseUser.uid
        body["displayName"] = username
        var newUser : User? = null
        runBlocking {
            withContext(Dispatchers.IO) {
                val call = RetrofitClient.userServiceInstance.appendUser(body)
                newUser = call.execute().body()
                Log.d(TAG, "User created in database $newUser")
            }
        }
        return newUser
    }

    // referenced https://stackoverflow.com/q/67473666
    private suspend fun <T : Any> Task<T>.await(): Result<T> {
        if (isComplete) {
            val ex = exception
            if (ex == null) {
                if (isCanceled) {
                    return Result.Error(CancellationException(("Task was cancelled")))
                }
                return Result.Success(result)
            }
            return Result.Error(ex)
        }
        return suspendCancellableCoroutine { cont ->
            addOnCompleteListener {
                val ex = exception
                if (ex == null) {
                    if (isCanceled) {
                        cont.cancel()
                    } else {
                        cont.resume(Result.Success(result))
                    }
                } else {
                    cont.resumeWithException(ex)
                }
            }
        }
    }
}