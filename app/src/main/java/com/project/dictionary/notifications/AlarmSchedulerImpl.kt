package  com.project.dictionary.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.project.dictionary.utils.Constants.THREE_HOURS
import java.util.Calendar

class AlarmSchedulerImpl(
    private val context: Context
) : AlarmScheduler {

    @SuppressLint("ScheduleExactAlarm")
    override fun schedule(alarmItem: AlarmItem) {
//        Log.i("mLogFirebase", "schedule")

        val alarmManager = context.getSystemService(AlarmManager::class.java)

        val rightNow: Calendar = Calendar.getInstance()
        val currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY)
        val currentMinute = rightNow.get(Calendar.MINUTE)
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", alarmItem.message)
        }

        val calendarFiring = Calendar.getInstance()

        calendarFiring.set(Calendar.HOUR_OF_DAY, currentHourIn24Format)
        calendarFiring.set(Calendar.MINUTE, currentMinute + 5)
        calendarFiring.set(Calendar.SECOND, 0)
        val intendedNotificationTime = calendarFiring.timeInMillis

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            intendedNotificationTime,
            THREE_HOURS,
            PendingIntent.getBroadcast(
                context,
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(alarmItem: AlarmItem) {
//        alarmManager.cancel(
//            PendingIntent.getBroadcast(
//                context,
//                alarmItem.hashCode(),
//                Intent(context, AlarmReceiver::class.java),
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//        )
    }
}