package com.project.dictionary

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class ReminderNotificationWorker(private val appContext: Context, workerParameters: WorkerParameters) : Worker(appContext, workerParameters) {

    override fun doWork(): Result {
        NotificationHandler.createReminderNotification(appContext)
        return Result.success()
    }

    companion object {
        fun schedule(appContext: Context) {
            val notificationRequest = PeriodicWorkRequestBuilder<ReminderNotificationWorker>(15, TimeUnit.MINUTES)
//                .setInitialDelay(target.timeInMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .build()
            WorkManager.getInstance(appContext)
                .enqueueUniquePeriodicWork(
                    "reminder_notification_work",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    notificationRequest
                )
        }
    }
}
