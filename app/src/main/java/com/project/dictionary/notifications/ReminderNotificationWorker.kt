package com.project.dictionary.notifications

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class ReminderNotificationWorker(
    private val appContext: Context, workerParameters: WorkerParameters
) : Worker(appContext, workerParameters) {

    override fun doWork(): Result {
        Log.i("mLogFirebase", "doWork")
//        NotificationHandler.createReminderNotification(appContext)
        return Result.success()
    }

    companion object {
        fun schedule(appContext: Context) {
//            Log.i("mLogFirebase", "schedule")
            val notificationRequest = PeriodicWorkRequestBuilder<ReminderNotificationWorker>(
                30, TimeUnit.HOURS,
                15, TimeUnit.MINUTES
            )
//                .setInitialDelay(target.timeInMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .build()
            WorkManager.getInstance(appContext)
                .enqueueUniquePeriodicWork(
                    "reminder_notification_work",
                    ExistingPeriodicWorkPolicy.UPDATE,
                    notificationRequest
                )
        }
    }
}
