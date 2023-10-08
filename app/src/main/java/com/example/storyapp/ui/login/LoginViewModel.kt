package com.example.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.api.response.LoginResponse
import com.example.storyapp.data.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    fun login(email: String, password: String): LiveData<LoginResponse> {
        val result = MutableLiveData<LoginResponse>()
        viewModelScope.launch {
            try {
                val response = loginRepository.login(email, password)
                result.postValue(response)
            } catch (e: Exception) {
                // Handle errors
            }
        }
        return result
    }
}