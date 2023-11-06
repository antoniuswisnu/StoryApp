package com.example.storyapp.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.pref.LoginPreferences
import com.example.storyapp.data.repository.UserRepository
import com.example.storyapp.di.Injection
import com.example.storyapp.ui.login.SessionViewModel
import com.example.storyapp.ui.main.MainViewModel
import com.example.storyapp.ui.main.StoriesViewModel
import com.example.storyapp.ui.maps.MapViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SessionViewModel::class.java) -> {
                SessionViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}

class MapViewModelFactory constructor(private val application: Application, private val userPreference: LoginPreferences)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MapViewModel::class.java) -> {
                MapViewModel(application, userPreference) as T
            }
            modelClass.isAssignableFrom(StoriesViewModel::class.java) -> {
                StoriesViewModel(application, userPreference) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MapViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            application: Application,
            userPreference: LoginPreferences
        ): MapViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MapViewModelFactory(application, userPreference)
            }
    }
}



