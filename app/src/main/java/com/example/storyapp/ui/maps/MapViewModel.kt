package com.example.storyapp.ui.maps

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.pref.LoginPreferences
import com.example.storyapp.data.repository.StoryPagingRepository

class MapViewModel (application: Application, userPreference: LoginPreferences) : ViewModel() {

    private val repository = StoryPagingRepository(application, userPreference)
    fun getStories() = repository.getStoriesLocation()
}