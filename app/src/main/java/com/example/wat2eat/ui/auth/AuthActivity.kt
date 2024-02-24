package com.example.wat2eat.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wat2eat.R


class AuthActivity : AppCompatActivity() {
    // TODO: CHECK IF USER ALREADY SIGNED IN IN ONSTART
    // https://firebase.google.com/docs/auth/android/password-auth#create_a_password-based_account
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if (savedInstanceState != null) {
            return
        }

        val chooseAuthFragment = ChooseAuthFragment()
        chooseAuthFragment.arguments = intent.extras

        supportFragmentManager.beginTransaction().add(R.id.activity_auth, chooseAuthFragment).commit()
    }
}