package com.example.dicodingevent.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteEvent::class], version = 1, exportSchema = false)
abstract class FavoriteEventDatabase : RoomDatabase() {
    abstract fun favoriteEventDao(): FavoriteEventDao

    companion object {
        @Volatile
        private var instance: FavoriteEventDatabase? = null

        fun getInstance(context: Context): FavoriteEventDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteEventDatabase::class.java, "FavoriteEvent.db"
                ).build()
            }
    }
}
