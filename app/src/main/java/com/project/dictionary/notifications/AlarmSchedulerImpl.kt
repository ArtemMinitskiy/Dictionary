package  com.project.dictionary.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.project.dictionary.utils.Constants.FOUR_HOURS
import java.util.Calendar

class AlarmSchedulerImpl(
    private val context: Context
) : AlarmScheduler {

    @SuppressLint("ScheduleExactAlarm")
    override fun schedule(alarmItem: AlarmItem) {
//        Log.i("mLogFirebase", "Schedule")

        val alarmManager = context.getSystemService(AlarmManager::class.java)

        val rightNow: Calendar = Calendar.getInstance()
        val currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY)
        val currentMinute = rightNow.get(Calendar.MINUTE)
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", alarmItem.message)
        }

        val calendarFiring = Calendar.getInstance()

        calendarFiring.set(Calendar.HOUR_OF_DAY, currentHourIn24Format + FOUR_HOURS.toInt())
        calendarFiring.set(Calendar.MINUTE, currentMinute)
        //Test
//        calendarFiring.set(Calendar.HOUR_OF_DAY, currentHourIn24Format)
//        calendarFiring.set(Calendar.MINUTE, currentMinute + TEST_TIME)
        calendarFiring.set(Calendar.SECOND, 0)
        val intendedNotificationTime = calendarFiring.timeInMillis

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            intendedNotificationTime,
            FOUR_HOURS,
            PendingIntent.getBroadcast(
                context,
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(alarmItem: AlarmItem) {

    }
}