package com.example.storyapp.ui.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.adapter.StoryAdapter
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.ViewModelFactory
import com.example.storyapp.ui.camera.UploadActivity
import com.example.storyapp.ui.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
            binding.fab.setOnClickListener {
                val intent = Intent(this, UploadActivity::class.java)
                intent.putExtra("token", user.token)
                startActivity(intent)
            }
        }

        setupView()
        storyViewModel = ViewModelProvider(this)[StoryViewModel::class.java]
        storyAdapter = StoryAdapter()
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.adapter = storyAdapter

        storyViewModel.stories.observe(this) { stories ->
            storyAdapter.setStories(stories)
        }

        storyViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        storyViewModel.getStories()
    }

    override fun onResume() {
        super.onResume()
        storyViewModel.getStories()
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