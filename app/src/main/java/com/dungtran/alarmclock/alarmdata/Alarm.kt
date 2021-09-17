package com.dungtran.alarmclock.alarmdata

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dungtran.alarmclock.service.AlarmReceiver
import java.util.*
import android.content.Intent as Intent

@Entity(tableName = "alarm_table")
class Alarm {
    @PrimaryKey(autoGenerate = true)
    private var alarmId = 0
    private var hours: Int = 0
    private var minutes: Int = 0
    private var isStart = false // Trang thai cua alarm
    private var isRecurrence = false
    private var monday = false
    private var tuesday = false
    private var wednesday = false
    private var thursday = false
    private var friday = false
    private var saturday = false
    private var sunday = false


    constructor(alarmId: Int, hours: Int, minutes: Int, isStart: Boolean, isRecurrence: Boolean, monday: Boolean, tuesday: Boolean, wednesday: Boolean, thursday: Boolean, friday: Boolean, saturday: Boolean, sunday: Boolean) {
        this.alarmId = alarmId
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
        }

        if (!isRecurrence) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    alarmPendingIntent
            )
        }
        else {
            val RUN_DAILY: Long = 1000 * 60 *60 * 24
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
    public fun getRecurrenceText(): String {
        var res = ""
        if (!isRecurrence) {
            return null.toString()
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
            return res
        }
    }
}