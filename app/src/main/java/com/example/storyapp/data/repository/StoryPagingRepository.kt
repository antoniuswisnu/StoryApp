package com.example.storyapp.data.repository

import android.app.Application
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.observe
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.api.response.ListStoryItem
import com.example.storyapp.api.response.Story
import com.example.storyapp.api.response.StoryResponse
import com.example.storyapp.api.retrofit.ApiConfig
import com.example.storyapp.api.retrofit.ApiService
import com.example.storyapp.data.paging.StoryPagingSource
import com.example.storyapp.data.pref.LoginPreferences
import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.di.ResultState
import com.example.storyapp.data.pref.UserPreference
import com.example.storyapp.data.pref.dataStore
import com.example.storyapp.ui.ViewModelFactory
import com.example.storyapp.ui.login.LoginViewModel
import com.example.storyapp.ui.login.SessionViewModel
import com.example.storyapp.ui.main.MainViewModel
import com.example.storyapp.ui.welcome.WelcomeActivity


class StoryPagingRepository constructor(application: Application, private val userPreferences: LoginPreferences) {

    private val token = userPreferences.getUser().token.toString()
    private val apiService: ApiService = ApiConfig.getApiService(token)

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(userPreferences, apiService)
            }
        ).liveData
    }

    fun getStoriesLocation(): LiveData<ResultState<StoryResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.getStoriesLocation(
                page = 1,
                size = 100,
                location = 1
            )
            if (response.error) {
                emit(ResultState.Error(response.message))
            } else {
                emit(ResultState.Success(response))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }
    suspend fun getDetailStory(id: String) : Story? {
        return apiService.getDetailStories(id).story
    }
//    companion object {
//        @Volatile
//        private var instance: StoryPagingRepository? = null
//
//        fun getInstance(apiService: ApiService,storyDatabase: StoryDatabase): StoryPagingRepository =
//            instance ?: synchronized(this) {
//                instance ?: StoryPagingRepository(apiService, storyDatabase).also { instance = it }
//            }
//    }
}
