package com.dungtran.alarmclock.alarmdata

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.dungtran.alarmclock.broadcastreceiver.AlarmReceiver
import java.util.*
import android.content.Intent as Intent

@Entity(tableName = "alarm_table")
class Alarm {
    @PrimaryKey
    var alarmId: Int = 0
    var hours: Int = 0
    var minutes: Int = 0
    var isStart = false // Trang thai cua alarm
    var isRecurrence = false
    var monday = false
    var tuesday = false
    var wednesday = false
    var thursday = false
    var friday = false
    var saturday = false
    var sunday = false
    @Ignore
    var isToday = true

    constructor(hours: Int, minutes: Int, isStart: Boolean, isRecurrence: Boolean, monday: Boolean, tuesday: Boolean, wednesday: Boolean, thursday: Boolean, friday: Boolean, saturday: Boolean, sunday: Boolean) {
        this.alarmId = System.currentTimeMillis().toInt()
        this.hours = hours
        this.minutes = minutes
        this.isStart = isStart
        this.isRecurrence = isRecurrence
        this.monday = monday
        this.tuesday = tuesday
        this.wednesday = wednesday
        this.thursday = thursday
        this.friday = friday
        this.saturday = saturday
        this.sunday = sunday
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun alarmSchedule(context: Context) {
        isStart = true
        Log.d("Alarm", "Start alarm Schedule")
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("HOURS", hours)
        intent.putExtra("MINUTES", minutes)
        intent.putExtra("ISRECURRENCE", isRecurrence)
        intent.putExtra("ID", alarmId)
        intent.putExtra("MONDAY", monday)
        intent.putExtra("TUESDAY", tuesday)
        intent.putExtra("WEDNESDAY", wednesday)
        intent.putExtra("THURSDAY", thursday)
        intent.putExtra("FRIDAY", friday)
        intent.putExtra("SATURDAY", saturday)
        intent.putExtra("SUNDAY", sunday)

        val alarmPendingIntent: PendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0)
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, minutes)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1)
            isToday = false
        }
        else isToday = true

        if (!isRecurrence) {
            Log.d("Alarm", "alarmSchedule: set NOT repeating")
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    alarmPendingIntent
            )
        }
        else {
            Log.d("Alarm", "alarmSchedule: set repeating")
            calendar.set(Calendar.MINUTE, minutes)
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    alarmPendingIntent
            )
        }
    }

    fun cancelAlarm(context: Context) {
        isStart = false
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0)
        alarmManager.cancel(alarmPendingIntent)
        Log.d("Alarm", "cancelAlarm: success")
    }
    fun getRecurrenceText(): String {
        var res = ""
        if (!isRecurrence) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.set(Calendar.HOUR_OF_DAY, hours)
            calendar.set(Calendar.MINUTE, minutes)
            isToday = calendar.timeInMillis > System.currentTimeMillis()

            res = if (isToday) "Hôm nay" else "Ngày mai"
        }
        else {
            if (monday)
                res += "2 "
            if (tuesday)
                res += "3 "
            if (wednesday)
                res += "4 "
            if (thursday)
                res += "5 "
            if (friday)
                res += "6 "
            if (saturday)
                res += "7 "
            if (sunday)
                res += "C "
        }
        return res
    }
}