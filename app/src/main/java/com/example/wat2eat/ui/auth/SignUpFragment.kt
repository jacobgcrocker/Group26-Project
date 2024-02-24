package com.example.wat2eat.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wat2eat.MainActivity
import com.example.wat2eat.databinding.FragmentSignUpBinding
import com.example.wat2eat.ui.utils.afterTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class SignUpFragment : Fragment() {

    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var inputUsername: TextInputEditText
    private lateinit var inputEmail: TextInputEditText
    private lateinit var inputPassword: TextInputEditText
    private lateinit var inputRePassword: TextInputEditText
    private lateinit var buttonSignUp: MaterialButton
    private lateinit var loading: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        val root: View = binding.root

        // TODO: INPUT FIELD BACKGROUNDS ARE HARDCODED WHITE, NEED TO CHANGE IF WE'RE IMPLEMENTING DARK MODE
        inputUsername = binding.inputUsername
        inputEmail = binding.inputEmail
        inputPassword = binding.inputPassword
        inputRePassword = binding.inputRePassword
        buttonSignUp = binding.buttonSignup
        loading = binding.loading

        binding.inputEmailLayout.errorIconDrawable = null
        binding.inputPasswordLayout.errorIconDrawable = null
        binding.inputRePasswordLayout.errorIconDrawable = null

        signUpViewModel =
            ViewModelProvider(this, SignUpViewModelFactory())[SignUpViewModel::class.java]

        setupObservers()

        handleInputEvents()

        return root
    }

    private fun setupObservers() {
        // update UI if form is invalid
        signUpViewModel.signUpFormState.observe(viewLifecycleOwner, Observer {
            val signUpFormState = it ?: return@Observer
            buttonSignUp.isEnabled = signUpFormState.isDataValid

            binding.inputEmailLayout.error =
                if (signUpFormState.emailError != null) getString(signUpFormState.emailError) else null
            binding.inputPasswordLayout.error =
                if (signUpFormState.passwordError != null) getString(signUpFormState.passwordError) else null
            binding.inputRePasswordLayout.error =
                if (signUpFormState.rePasswordError != null) getString(signUpFormState.rePasswordError) else null

        })

        // update UI when sign up returns result
        signUpViewModel.signUpResult.observe(viewLifecycleOwner, Observer {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            val loginResult = it ?: return@Observer

            loading.visibility = View.INVISIBLE
            if (loginResult.error != null) {
                Toast.makeText(context, loginResult.error, Toast.LENGTH_LONG).show()
            }
            if (loginResult.success != null) {
                startActivity(Intent(context, MainActivity::class.java))
            }
        })
    }

    private fun handleInputEvents() {
        triggerInputUpdate(inputEmail)
        triggerInputUpdate(inputPassword)
        triggerInputUpdate(inputRePassword)

        // allow pressing enter on keyboard to submit form
        inputRePassword.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> triggerSignUp()
            }
            false
        }

        buttonSignUp.setOnClickListener {
            // disable touch events, referenced https://stackoverflow.com/a/45613741
            activity?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            loading.visibility = View.VISIBLE
            triggerSignUp()
        }
    }

    private fun triggerInputUpdate(inputField: TextInputEditText) {
        inputField.afterTextChanged {
            signUpViewModel.signUpFormDataChanged(
                inputEmail.text.toString(),
                inputPassword.text.toString(),
                inputRePassword.text.toString()
            )
        }
    }

    private fun triggerSignUp() {
        signUpViewModel.signUp(
            inputUsername.text.toString(),
            inputEmail.text.toString(),
            inputPassword.text.toString()
        )
    }
}