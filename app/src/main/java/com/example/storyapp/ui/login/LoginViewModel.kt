package com.example.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.api.response.LoginResponse
import com.example.storyapp.data.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String): LiveData<LoginResponse> {
        val result = MutableLiveData<LoginResponse>()
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _isLoading.value = false
                val response = loginRepository.login(email, password)
                result.postValue(response)
            } catch (e: Exception) {
                // handle errors
                _isLoading.value = false
                result.postValue(e.message?.let { LoginResponse(error = true, message = it) })
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
        isEmailValid = android.util.Patterns.EMAIL_ADDRESS
            .matcher(email)
            .matches()
    }
}