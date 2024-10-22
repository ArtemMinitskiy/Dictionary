package com.project.dictionary.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.RemoteViews
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.project.dictionary.MainActivity
import com.project.dictionary.R
import com.project.dictionary.firebase.RealtimeDatabaseRepositoryImpl
import com.project.dictionary.model.Word
import com.project.dictionary.ui.theme.color1
import com.project.dictionary.ui.theme.color2
import com.project.dictionary.ui.theme.color3
import com.project.dictionary.ui.theme.color4
import com.project.dictionary.utils.Constants.CHANNEL_ID
import com.project.dictionary.utils.Constants.NOTIFICATION_WORD
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.random.Random

object NotificationHandler {
    private val databaseRepositoryImpl = RealtimeDatabaseRepositoryImpl()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("mLogFirebase", "CoroutineExceptionHandler ${throwable.message}")
    }
    private val scope = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler)
    private var word = Word()

    private var remoteViews: RemoteViews? = null
    private var expandedRemoteViews: RemoteViews? = null
    private var listRandomIndex = 0

    @OptIn(ExperimentalCoroutinesApi::class)
    fun createReminderNotification(context: Context) {
        Log.i("mLogFirebase", "createReminderNotification")
        scope.launch {
            databaseRepositoryImpl.fetchWords().collectLatest {
                when {
                    it.isSuccess -> {
                        scope.launch {
                            it.let { result ->
                                result.getOrNull()?.let { list ->
                                    listRandomIndex = Random.nextInt(0, list.size - 1)
                                    word = list[listRandomIndex]
                                    Log.e("mLogFirebase", "WORD: $word")
                                }
                            }
                        }.invokeOnCompletion {
                            Log.i("mLogFirebase", "Send Notification")
                            remoteViews = RemoteViews(context.packageName, R.layout.custom_notification_layout)

                            remoteViews?.setTextViewText(R.id.notificationText, word.wordName)

                            remoteViews?.setInt(
                                R.id.root, "setBackgroundColor", when (listRandomIndex % 4) {
                                    0 -> color1.toArgb()
                                    1 -> color2.toArgb()
                                    2 -> color3.toArgb()
                                    else -> color4.toArgb()
                                }
                            )

                            expandedRemoteViews = RemoteViews(context.packageName, R.layout.custom_expanded_notification_layout)

                            expandedRemoteViews?.setTextViewText(R.id.notificationText, word.wordName)
                            expandedRemoteViews?.setTextViewText(R.id.notificationDescText,
//                                if (word.wordDescription.length > 149)
//                                    word.wordDescription.replaceFirstChar { it.titlecase() }.substring(0, 150) + "..."
//                                else
                                    word.wordDescription.replaceFirstChar { it.titlecase() }
                            )

                            expandedRemoteViews?.setInt(
                                R.id.root, "setBackgroundColor", when (listRandomIndex % 4) {
                                    0 -> color1.toArgb()
                                    1 -> color2.toArgb()
                                    2 -> color3.toArgb()
                                    else -> color4.toArgb()
                                }
                            )

                            //No back-stack when launched
                            val intent = Intent(context, MainActivity::class.java).apply {
                                putExtra(NOTIFICATION_WORD, word)
                                action = Intent.ACTION_MAIN
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                        Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_NO_HISTORY
                            }

                            val pendingIntent = PendingIntent.getActivity(context, listRandomIndex, intent, PendingIntent.FLAG_IMMUTABLE)

                            createNotificationChannel(context) //This won't create a new channel everytime, safe to call

                            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true) //Remove notification when tapped
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) //Show on lock screen
                                .setContentIntent(pendingIntent)
                                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                                .setCustomBigContentView(expandedRemoteViews)
                                .setContent(remoteViews) //For launching the MainActivity

                            with(NotificationManagerCompat.from(context)) {
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                    return@invokeOnCompletion
                                }
                                notify(1, builder.build())
                            }
                        }
                    }

                    it.isFailure -> {
                        scope.cancel()
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
