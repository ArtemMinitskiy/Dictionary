package  com.project.dictionary.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
//        Log.i("mLogFirebase", "onReceive")
        context?.let { NotificationHandler.createReminderNotification(it) }
    }
}