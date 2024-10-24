package  com.project.dictionary.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.project.dictionary.utils.Constants.NOTIFICATION_COLOR

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
//        Log.i("mLogFirebase", "onReceive ${intent?.getStringExtra(NOTIFICATION_COLOR)}")
        context?.let { NotificationHandler.createReminderNotification(it, intent?.getStringExtra(NOTIFICATION_COLOR)) }
    }
}