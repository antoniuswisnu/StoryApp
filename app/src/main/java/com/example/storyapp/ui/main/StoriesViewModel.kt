package com.example.storyapp.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.api.response.ListStoryItem
import com.example.storyapp.data.pref.LoginPreferences
import com.example.storyapp.data.repository.StoryPagingRepository

class StoriesViewModel (application: Application,
                        userPreference: LoginPreferences
) : ViewModel() {
    private val repository = StoryPagingRepository(application, userPreference)

    val getListStory: LiveData<PagingData<ListStoryItem>> =
        repository.getStories().cachedIn(viewModelScope)
}