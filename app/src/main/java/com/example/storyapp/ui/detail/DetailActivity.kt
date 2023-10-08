package com.example.storyapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        detailViewModel.detailStory.observe(this) { story ->
            setupView()
            Log.d("DetailActivity", "onCreate: $story")
        }
        detailViewModel.getDetailStories(intent.getStringExtra("id")!!)
    }

    private fun setupView() {
        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")
        val photoUrl = intent.getStringExtra("photoUrl")

        Glide.with(this)
            .load(photoUrl)
            .into(binding.imgDetail)
        binding.tvTitle.text = name
        binding.tvDesc.text = description
        Log.d("DetailActivity", "setupView: $name")
    }
}