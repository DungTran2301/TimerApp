package com.dungtran.alarmclock.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.dungtran.alarmclock.service.AlarmService
import com.dungtran.alarmclock.service.RescheduleAlarmService
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Alarm receiver", "onReceive: Khoi chay receiver")
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            startRescheduleAlarmsService(context, intent)
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

    private fun startRescheduleAlarmsService(context: Context, intent: Intent) {
        val intentService = Intent(context, RescheduleAlarmService::class.java)
        intentService.putExtra("DISMISS", false)
        intentService.putExtra("HOUR", intent.getIntExtra("HOURS", 0))
        intentService.putExtra("MINUTE", intent.getIntExtra("MINUTES", 0))
        Log.d("Alarm receiver", "startRescheduleAlarmsService: bat dau service alarm")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            context.startForegroundService(intentService)
        else
            context.startService(intentService)
    }

    private fun startAlarmService(context: Context, intent: Intent) {
        val intentService = Intent(context, AlarmService::class.java)
        intentService.putExtra("DISMISS", false)
        intentService.putExtra("HOUR", intent.getIntExtra("HOURS", 0))
        intentService.putExtra("MINUTE", intent.getIntExtra("MINUTES", 0))
        Log.d("Alarm receiver", "startAlarmsService: bat dau service alarm")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            context.startForegroundService(intentService)
        else
            context.startService(intentService)
    }

}