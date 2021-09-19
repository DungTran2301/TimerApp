package com.dungtran.alarmclock.alarmdata

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.dungtran.alarmclock.broadcastreceiver.AlarmReceiver
import java.util.*
import android.content.Intent as Intent

@Entity(tableName = "alarm_table")
class Alarm {
    @PrimaryKey(autoGenerate = true)
    var alarmId = 0
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
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("HOURS", hours)
        intent.putExtra("MINUTES", minutes)
        intent.putExtra("ISRECURRENCE", isRecurrence)
        intent.putExtra("MONDAY", monday)
        intent.putExtra("TUESDAY", tuesday)
        intent.putExtra("WEDNESDAY", wednesday)
        intent.putExtra("THURSDAY", thursday)
        intent.putExtra("FRIDAY", friday)
        intent.putExtra("SATURDAY", saturday)
        intent.putExtra("SUNDAY", sunday)


        val alarmPendingIntent: PendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0)
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hours)
        calendar.set(Calendar.MINUTE, minutes)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1)
            isToday = false
        }

        if (!isRecurrence) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    alarmPendingIntent
            )
        }
        else {
            val RUN_DAILY: Long = 1000 * 60 * 60 * 24
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    RUN_DAILY,
                    alarmPendingIntent
            )
        }
        this.isStart = true
    }

    fun cancelAlarm(context: Context) {
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0)
        alarmManager.cancel(alarmPendingIntent)
        this.isStart = false

    }
    fun getRecurrenceText(): String {
        var res = ""
        if (!isRecurrence) {
            if (isToday)
                res = "Hôm nay"
            else res = "Ngày mai"
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