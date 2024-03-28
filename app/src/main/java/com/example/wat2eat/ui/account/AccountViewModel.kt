package com.example.wat2eat.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wat2eat.data.auth.UserRepository
import com.example.wat2eat.models.User
import kotlinx.coroutines.launch

class AccountViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> = _userData

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _userData.value = userRepository.getLoggedInUser()
        }
    }
}