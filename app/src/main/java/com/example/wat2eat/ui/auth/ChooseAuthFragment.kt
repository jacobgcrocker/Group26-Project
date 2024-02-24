package com.example.wat2eat.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wat2eat.R
import com.example.wat2eat.databinding.FragmentChooseAuthBinding
import com.google.android.material.button.MaterialButton

class ChooseAuthFragment : Fragment() {
    private lateinit var buttonLogin: MaterialButton
    private lateinit var buttonSignUp: MaterialButton
    private lateinit var binding: FragmentChooseAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        binding = FragmentChooseAuthBinding.inflate(layoutInflater)
        val root: View = binding.root

        buttonLogin = binding.buttonLogin
        buttonSignUp = binding.buttonSignup
        setupButtonListeners()

        return root
    }

    private fun setupButtonListeners() {
        buttonLogin.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.activity_auth, LoginFragment())
                .addToBackStack(LoginFragment::class.simpleName).commit()
        }

        buttonSignUp.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.activity_auth, SignUpFragment())
                .addToBackStack(SignUpFragment::class.simpleName).commit()
        }
    }
}