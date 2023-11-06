package com.example.storyapp.ui.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.adapter.StoryAdapter
import com.example.storyapp.api.response.LoginResult
import com.example.storyapp.data.paging.LoadingStateAdapter
import com.example.storyapp.data.pref.LoginPreferences
import com.example.storyapp.data.pref.UserPreference
import com.example.storyapp.data.pref.dataStore
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.MapViewModelFactory
import com.example.storyapp.ui.ViewModelFactory
import com.example.storyapp.ui.camera.UploadActivity
import com.example.storyapp.ui.maps.MapsActivity
import com.example.storyapp.ui.welcome.WelcomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
//    private val storiesViewModel by viewModels<StoriesViewModel> {
//        MapViewModelFactory.getInstance(application, UserPreference(dataStore))
//    }
    private lateinit var storiesViewModel :  StoriesViewModel
    private lateinit var preference: LoginPreferences
    private lateinit var binding: ActivityMainBinding

    private lateinit var loginModel: LoginResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        storiesViewModel = ViewModelProvider(this)[StoriesViewModel::class.java]
        storiesViewModel = obtainViewModel(this as AppCompatActivity)
        setupView()
        showLoading(true)
        preference = LoginPreferences(this)
        loginModel = preference.getUser()

        lifecycleScope.launch{
            withContext(Dispatchers.Main){
                if(loginModel.token != null){
                    binding.rvStories.layoutManager = LinearLayoutManager(this@MainActivity)
                    getData()
                    binding.fab.setOnClickListener {
                        val intent = Intent(this@MainActivity, UploadActivity::class.java)
                        intent.putExtra("token", loginModel.token)
                        startActivity(intent)
                    }
                }else{
                    startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
                    finish()
                }
            }
        }

//        viewModel.getSession().observe(this) { user ->
//            if (!user.isLogin) {
//                startActivity(Intent(this, WelcomeActivity::class.java))
//                finish()
//            } else {
//                binding.rvStories.layoutManager = LinearLayoutManager(this)
//                getData()
//            }
//            Log.d("MainActivity", "onCreate: $user")
//
//        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): StoriesViewModel {
        val userPreference = LoginPreferences(activity.application)
        val factory = MapViewModelFactory.getInstance(activity.application, userPreference)
        return ViewModelProvider(activity, factory)[StoriesViewModel::class.java]
    }

    private fun getData() {
        showLoading(false)
        val adapter = StoryAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        storiesViewModel.getListStory.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.about -> {
                Toast.makeText(this,"About Selected",Toast.LENGTH_SHORT).show()
            }
            R.id.settings -> {
                Toast.makeText(this,"Settings Selected",Toast.LENGTH_SHORT).show()
                startActivity(Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.exit -> {
                viewModel.logout()
                Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show()
            }
            R.id.maps -> {
                Toast.makeText(this,"Maps Selected",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MapsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}