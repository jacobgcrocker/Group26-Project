package com.example.wat2eat.ui.auth

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.wat2eat.MainActivity
import com.example.wat2eat.databinding.FragmentLoginBinding

import com.example.wat2eat.ui.utils.afterTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding
    private lateinit var inputEmail: TextInputEditText
    private lateinit var inputPassword: TextInputEditText
    private lateinit var buttonLogin: MaterialButton
    private lateinit var loading: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        binding = FragmentLoginBinding.inflate(layoutInflater)
        val root: View = binding.root

        // TODO: INPUT FIELD BACKGROUNDS ARE HARDCODED WHITE, NEED TO CHANGE IF WE'RE IMPLEMENTING DARK MODE
        inputEmail = binding.inputEmail
        inputPassword = binding.inputPassword
        buttonLogin = binding.buttonLogin
        loading = binding.loading

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        setupObservers()

        handleInputEvents()

        return root
    }

    private fun setupObservers() {
        // update UI if form is invalid
        loginViewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            val loginState = it ?: return@Observer
            buttonLogin.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                inputEmail.error = getString(loginState.usernameError)
            }
        })

        // update UI when login returns result
        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer {
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
        inputEmail.afterTextChanged {
            loginViewModel.loginDataChanged(
                inputEmail.text.toString()
            )
        }

        // allow pressing enter on keyboard to submit form
        inputPassword.apply {
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            inputEmail.text.toString(),
                            inputPassword.text.toString()
                        )
                }
                false
            }

            buttonLogin.setOnClickListener {
                // disable UI, https://stackoverflow.com/a/45613741
                activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                loading.visibility = View.VISIBLE
                loginViewModel.login(inputEmail.text.toString(), inputPassword.text.toString())
            }
        }
    }
}