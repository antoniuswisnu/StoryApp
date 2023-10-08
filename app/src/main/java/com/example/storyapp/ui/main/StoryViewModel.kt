package com.example.storyapp.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.api.response.ListStoryItem
import com.example.storyapp.di.Injection
import kotlinx.coroutines.launch

class StoryViewModel(application: Application) : AndroidViewModel(application) {
    private val storyRepository = Injection.provideStoryRepository(application)

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories

    fun getStories() {
        viewModelScope.launch {
            try {
                val response = storyRepository.getStories()
                _stories.postValue(response)
            } catch (e: Exception) {
                // Handle exception and show error message
            }
        }
    }
}
