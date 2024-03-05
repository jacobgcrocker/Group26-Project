package com.example.wat2eat.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.wat2eat.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
    private lateinit var accountViewModel: AccountViewModel
    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        accountViewModel = ViewModelProvider(this, AccountViewModelFactory())[AccountViewModel::class.java]

        accountViewModel.userData.observe(viewLifecycleOwner, { user ->
            if (user != null) {
                binding.textAccount.text = user.toString()
            } else {
                // TODO: Add feature to log in
                // Do we want to force the user to log in between using the app?
                // Or user can use the app without logging in
                //  but with limited features (no profile, saved recipes, etc.) ?
                binding.textAccount.text = "Please log in"
            }
        })
        
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}