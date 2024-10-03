package com.project.dictionary

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.project.dictionary.Constants.NOTIFICATION_WORD
import com.project.dictionary.firebase.RealtimeDatabaseRepositoryImpl
import com.project.dictionary.model.Word
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.random.Random

object NotificationHandler {
    private const val CHANNEL_ID = "transactions_reminder_channel"
    private val databaseRepositoryImpl = RealtimeDatabaseRepositoryImpl()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("mLogFirebase", "CoroutineExceptionHandler ${throwable.message}")
    }
    private val scope = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler)
    private var word = Word("", "", "")

    @OptIn(ExperimentalCoroutinesApi::class)
    fun createReminderNotification(context: Context) {
//        Log.i("mLogFirebase", "createReminderNotification")

        scope.launch {
            databaseRepositoryImpl.fetchWords().collectLatest {
                when {
                    it.isSuccess -> {
                        scope.launch {
                            it.let { result ->
                                result.getOrNull()?.let { list ->
                                    word = list[Random.nextInt(0, list.size - 1)]
                                    Log.e("mLogFirebase", "WORD: $word")
                                }
                            }
                        }.invokeOnCompletion {
                            Log.i("mLogFirebase", "Send Notification")

                            //No back-stack when launched
                            val intent = Intent(context, MainActivity::class.java).apply {
                                putExtra(NOTIFICATION_WORD, word)
                                action = Intent.ACTION_MAIN
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                        Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_NO_HISTORY
                            }

                            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

                            createNotificationChannel(context) //This won't create a new channel everytime, safe to call

                            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher_round)
                                .setContentTitle(word.wordName)
                                .setContentText(if (word.wordDescription.length >= 30) word.wordDescription.substring(0, 29).replaceFirstChar { it.titlecase() } + "..." else "...")
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true) //Remove notification when tapped
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) //Show on lock screen
                                .setContentIntent(pendingIntent) //For launching the MainActivity

                            with(NotificationManagerCompat.from(context)) {
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                    return@invokeOnCompletion
                                }
                                notify(1, builder.build())
                            }
                        }
                    }

                    it.isFailure -> {
                        Log.e("mLogFirebase", "Failure ${it.exceptionOrNull()?.printStackTrace()}")
                    }
                }
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        val name = "name"
        val descriptionText = "descriptionText"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        //Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
