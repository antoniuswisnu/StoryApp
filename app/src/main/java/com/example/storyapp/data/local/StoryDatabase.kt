//package com.example.storyapp.data.local
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import androidx.room.TypeConverters
//import com.example.storyapp.api.response.ListStoryItem
//import com.example.storyapp.api.response.StoryResponse
//import com.example.storyapp.di.ListStoryItemTypeConverter
//
//@Database(
//    entities = [ListStoryItem::class, RemoteKeys::class, StoryResponse::class],
//    version = 2,
//    exportSchema = false
//)
//@TypeConverters(ListStoryItemTypeConverter::class)
//abstract class StoryDatabase : RoomDatabase() {
//    abstract fun storyDao(): StoryDao
//    abstract fun remoteKeysDao(): RemoteDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: StoryDatabase? = null
//
//        @JvmStatic
//        fun getDatabase(context: Context): StoryDatabase {
//            return INSTANCE ?: synchronized(this) {
//                INSTANCE ?: Room.databaseBuilder(
//                    context.applicationContext,
//                    StoryDatabase::class.java, "story_database"
//                )
//                    .fallbackToDestructiveMigration()
//                    .build()
//                    .also { INSTANCE = it }
//            }
//        }
//    }
//}