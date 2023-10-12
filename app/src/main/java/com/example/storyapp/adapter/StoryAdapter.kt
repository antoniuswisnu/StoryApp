package com.example.storyapp.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.api.response.DetailResponse
import com.example.storyapp.api.response.ListStoryItem
import com.example.storyapp.api.response.Story
import com.example.storyapp.databinding.ItemStoryBinding
import com.example.storyapp.ui.detail.DetailActivity

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    private var stories = listOf<ListStoryItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setStories(stories: List<ListStoryItem>) {
        this.stories = stories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val _story = stories[position]
        with(holder.itemView) {
            val binding = ItemStoryBinding.bind(this)
            binding.tvItemName.text = _story.name
            binding.tvDetailDescription.text = _story.description
            val url = _story.photoUrl
            Glide.with(context)
                .load(url)
                .into(binding.ivItemPhoto)

            holder.itemView.setOnClickListener {
//                intent.putExtra("id", story.id)
//                intent.putExtra("name", story.name)
//                intent.putExtra("description", story.description)
//                intent.putExtra("photoUrl", story.photoUrl)

                val story = Story(
                    _story.photoUrl,
                    _story.createdAt,
                    _story.name,
                    _story.description,
                    _story.id
                )

                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STORY, story)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        holder.itemView.context as Activity,
                        Pair(binding.ivItemPhoto, "iv_item_photo"),
                        Pair(binding.tvItemName, "tv_item_name"),
                        Pair(binding.tvDetailDescription, "tv_detail_description")
                    )

                context.startActivity(intent, optionsCompat.toBundle())
                Log.e("StoryAdapter", "onBindViewHolder: $story")
            }
        }
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}

