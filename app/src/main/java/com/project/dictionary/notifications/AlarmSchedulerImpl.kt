package  com.project.dictionary.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.project.dictionary.utils.Constants.FOUR_HOURS_MILLIS
import com.project.dictionary.utils.Constants.NOTIFICATION_COLOR
import com.project.dictionary.utils.Constants.TEST_TIME
import com.project.dictionary.utils.Constants.THREE_HOURS
import java.util.Calendar

class AlarmSchedulerImpl(
    private val context: Context
) : AlarmScheduler {
    @SuppressLint("ScheduleExactAlarm")
    override fun schedule(color: String) {
//        Log.e("mLogFirebase", "Schedule $color")

        val alarmManager = context.getSystemService(AlarmManager::class.java)

        val rightNow: Calendar = Calendar.getInstance()
        val currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY)
        val currentMinute = rightNow.get(Calendar.MINUTE)
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(NOTIFICATION_COLOR, color)

        val calendarFiring = Calendar.getInstance()

        calendarFiring.set(Calendar.HOUR_OF_DAY, currentHourIn24Format + THREE_HOURS)
        calendarFiring.set(Calendar.MINUTE, currentMinute)
        //Test
//        calendarFiring.set(Calendar.HOUR_OF_DAY, currentHourIn24Format)
//        calendarFiring.set(Calendar.MINUTE, currentMinute + TEST_TIME)
        calendarFiring.set(Calendar.SECOND, 0)
        val intendedNotificationTime = calendarFiring.timeInMillis

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            intendedNotificationTime,
//            60000,
            FOUR_HOURS_MILLIS,
            PendingIntent.getBroadcast(
                context,
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}