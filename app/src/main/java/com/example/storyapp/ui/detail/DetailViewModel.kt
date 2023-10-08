package com.example.storyapp.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.api.response.Story
import com.example.storyapp.di.Injection
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val storyRepository = Injection.provideStoryRepository(application)

    private val _detailStory = MutableLiveData<Story?>()
    val detailStory: MutableLiveData<Story?> = _detailStory

    fun getDetailStories(id: String){
        viewModelScope.launch {
            try {
                val response = storyRepository.getDetailStory(id)
                _detailStory.postValue(response)
            } catch (e: Exception) {
                // Handle exception and show error message
            }
        }
    }
}