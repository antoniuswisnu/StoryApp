package com.example.storyapp.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.api.response.RegisterResponse
import com.example.storyapp.data.repository.RegisterRepository
import kotlinx.coroutines.launch

class SignUpViewModel(private val registerRepository: RegisterRepository) : ViewModel() {
    fun register(name: String, email: String, password: String): LiveData<RegisterResponse> {
        val result = MutableLiveData<RegisterResponse>()
        viewModelScope.launch {
            try {
                val response = registerRepository.register(name, email, password)
                result.postValue(response)
            } catch (e: Exception) {
                // Handle errors
            }
        }
        return result
    }
}