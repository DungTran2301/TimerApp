package com.dungtran.alarmclock.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.dungtran.alarmclock.service.AlarmService
import com.dungtran.alarmclock.service.RescheduleAlarmService
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            startRescheduleAlarmsService(context)
        }
        else {
            if (intent.getBooleanExtra("ISRECURRENCE",  false)) {
                if (hasAlarmToday(intent)) {
                    startAlarmService(context, intent)
                }
            }
            else {
               startAlarmService(context, intent)
            }
        }
    }

    private fun hasAlarmToday(intent: Intent): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val today = calendar.get(Calendar.DAY_OF_WEEK)

        when (today) {
            Calendar.MONDAY -> {
                if (intent.getBooleanExtra("MONDAY", false))
                    return true
                return false
            }
            Calendar.TUESDAY -> {
                if (intent.getBooleanExtra("TUESDAY", false))
                    return true
                return false
            }
            Calendar.WEDNESDAY -> {
                if (intent.getBooleanExtra("WEDNESDAY", false))
                    return true
                return false
            }
            Calendar.THURSDAY -> {
                if (intent.getBooleanExtra("THURSDAY", false))
                    return true
                return false
            }
            Calendar.FRIDAY -> {
                if (intent.getBooleanExtra("FRIDAY", false))
                    return true
                return false
            }
            Calendar.SATURDAY -> {
                if (intent.getBooleanExtra("SATURDAY", false))
                    return true
                return false
            }
            Calendar.SUNDAY -> {
                if (intent.getBooleanExtra("SUNDAY", false))
                    return true
                return false
            }

        }
        return false
    }

    private fun startRescheduleAlarmsService(context: Context) {
        val intentService = Intent(context, AlarmService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            context.startForegroundService(intentService)
        else
            context.startService(intentService)
    }

    private fun startAlarmService(context: Context, intent: Intent) {
        val intentService = Intent(context, RescheduleAlarmService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            context.startForegroundService(intentService)
        else
            context.startService(intentService)
    }

}