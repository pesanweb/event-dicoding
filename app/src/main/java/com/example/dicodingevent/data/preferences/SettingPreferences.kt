package com.example.dicodingevent.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[THEME_KEY] ?: false
            }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    fun getNotificationSetting(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[NOTIFICATION_KEY] ?: false
            }
    }

    suspend fun saveNotificationSetting(isDailyReminderActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_KEY] = isDailyReminderActive
        }
    }

    companion object {
        private val THEME_KEY = booleanPreferencesKey("theme_setting")
        private val NOTIFICATION_KEY = booleanPreferencesKey("notification_setting")

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = "settings"
        )

        @Volatile
        private var instance: SettingPreferences? = null

        fun getInstance(context: Context): SettingPreferences =
            instance ?: synchronized(this) {
                instance ?: SettingPreferences(context.applicationContext.dataStore)
                    .also { instance = it }
            }
    }
}
