package com.example.storyapp.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.api.response.RegisterResponse
import com.example.storyapp.data.repository.RegisterRepository
import kotlinx.coroutines.launch

class SignUpViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    sealed class RegisterResult {
        data class Success(val response: RegisterResponse) : RegisterResult()
        data class Error(val message: String) : RegisterResult()
    }

    fun register(name: String, email: String, password: String): LiveData<RegisterResult> {
        val result = MutableLiveData<RegisterResult>()
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _isLoading.value = false
                val response = registerRepository.register(name, email, password)
                result.postValue(RegisterResult.Success(response))
                Log.e("SignUpViewModel", "register: $response")
            } catch (e: Exception) {
                _isLoading.value = false
                result.postValue(RegisterResult.Error(e.message ?: "Unknown error"))
            }
        }
        return result
    }

    var isPasswordValid = false
    var isEmailValid = false

    fun validatePassword(password: String) {
        isPasswordValid = password.length >= 8
    }

    fun validateEmail(email: String) {
        isEmailValid = email.contains("@email.com")
    }
}