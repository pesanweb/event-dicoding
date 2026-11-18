package com.example.dicodingevent.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.dicodingevent.data.preferences.SettingPreferences
import com.example.dicodingevent.data.retrofit.ApiConfig
import com.example.dicodingevent.utils.NotificationHelper
import kotlinx.coroutines.flow.first

class DailyReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val pref = SettingPreferences.getInstance(applicationContext)
        val isReminderActive = pref.getNotificationSetting().first()

        if (!isReminderActive) {
            return Result.success()
        }

        return try {
            val response = ApiConfig.getApiService().getEvents(active = -1, limit = 1)
            if (!response.error && response.listEvents.isNotEmpty()) {
                val event = response.listEvents.first()
                NotificationHelper.showDailyReminderNotification(
                    applicationContext,
                    event.id,
                    event.name,
                    event.beginTime
                )
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "DailyReminderWork"
    }
}
