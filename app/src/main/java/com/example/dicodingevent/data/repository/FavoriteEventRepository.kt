package com.example.dicodingevent.data.repository

import androidx.lifecycle.LiveData
import com.example.dicodingevent.data.local.room.FavoriteEvent
import com.example.dicodingevent.data.local.room.FavoriteEventDao

class FavoriteEventRepository private constructor(
    private val favoriteEventDao: FavoriteEventDao
) {
    fun getAllFavoriteEvents(): LiveData<List<FavoriteEvent>> =
        favoriteEventDao.getAllFavoriteEvents()

    fun getFavoriteEventById(id: String): LiveData<FavoriteEvent> =
        favoriteEventDao.getFavoriteEventById(id)

    suspend fun insert(event: FavoriteEvent) {
        favoriteEventDao.insert(event)
    }

    suspend fun delete(event: FavoriteEvent) {
        favoriteEventDao.delete(event)
    }

    companion object {
        @Volatile
        private var instance: FavoriteEventRepository? = null

        fun getInstance(
            favoriteEventDao: FavoriteEventDao
        ): FavoriteEventRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteEventRepository(favoriteEventDao)
            }.also { instance = it }
    }
}
