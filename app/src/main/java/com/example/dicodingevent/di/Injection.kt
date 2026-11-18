package com.example.dicodingevent.di

import android.content.Context
import com.example.dicodingevent.data.local.room.FavoriteEventDatabase
import com.example.dicodingevent.data.preferences.SettingPreferences
import com.example.dicodingevent.data.repository.FavoriteEventRepository

object Injection {
    fun provideRepository(context: Context): FavoriteEventRepository {
        val database = FavoriteEventDatabase.getInstance(context)
        val dao = database.favoriteEventDao()
        return FavoriteEventRepository.getInstance(dao)
    }

    fun provideSettingPreferences(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context)
    }
}
