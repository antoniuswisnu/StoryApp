//package com.example.storyapp.data.local
//
//import androidx.paging.PagingSource
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.example.storyapp.api.response.ListStoryItem
//import com.example.storyapp.api.response.StoryResponse
//
//@Dao
//interface StoryDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertStory(story: StoryResponse)
//
//    @Query("SELECT * FROM story_item")
//    fun getAllStory(): PagingSource<Int, ListStoryItem>
//
//    @Query("DELETE FROM story_item")
//    suspend fun deleteALl()
//}