package com.example.wat2eat.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wat2eat.R


class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if (savedInstanceState != null) {
            return
        }

        val loginFragment = LoginFragment()
        loginFragment.arguments = intent.extras

        supportFragmentManager.beginTransaction().add(R.id.activity_auth, loginFragment).commit()
    }
}