package com.example.dicodingevent.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.data.preferences.SettingPreferences
import com.example.dicodingevent.data.repository.FavoriteEventRepository
import com.example.dicodingevent.di.Injection
import com.example.dicodingevent.ui.favorite.FavoriteViewModel
import com.example.dicodingevent.ui.settings.SettingsViewModel

class ViewModelFactory private constructor(
    private val favoriteEventRepository: FavoriteEventRepository,
    private val settingPreferences: SettingPreferences
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(favoriteEventRepository) as T
        } else if (modelClass.isAssignableFrom(DetailEventViewModel::class.java)) {
            return DetailEventViewModel(favoriteEventRepository) as T
        } else if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(settingPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.provideSettingPreferences(context)
                )
            }.also { instance = it }
    }
}
