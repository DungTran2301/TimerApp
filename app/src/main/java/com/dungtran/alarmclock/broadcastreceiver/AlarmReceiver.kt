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
    private var isOff: Boolean = false
    override fun onReceive(context: Context, intent: Intent) {
        isOff = intent.getBooleanExtra("OFF", false)
        Log.d("Alarm receiver", "onReceive: Khoi chay receiver")
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            startRescheduleAlarmsService(context, intent)
        }
        else {
            if (intent.getBooleanExtra("ISRECURRENCE",  false)) {
                Log.d("Alarm receiver", "onReceive: báo thức lặp lại đã vào")
                if (hasAlarmToday(intent)) {
                    Log.d("Alarm receiver", "onReceive: Hôm nay có báo thức")
                    startAlarmService(context, intent)
                }
            }
            else {
                Log.d("Alarm receiver", "onReceive: báo thức KO lặp lại đã vào")
               startAlarmService(context, intent)
            }
        }
    }

    private fun hasAlarmToday(intent: Intent): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()

        when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> {
                return intent.getBooleanExtra("MONDAY", false)
            }
            Calendar.TUESDAY -> {
                return intent.getBooleanExtra("TUESDAY", false)
            }
            Calendar.WEDNESDAY -> {
                return intent.getBooleanExtra("WEDNESDAY", false)
            }
            Calendar.THURSDAY -> {
                return intent.getBooleanExtra("THURSDAY", false)
            }
            Calendar.FRIDAY -> {
                return intent.getBooleanExtra("FRIDAY", false)
            }
            Calendar.SATURDAY -> {
                return intent.getBooleanExtra("SATURDAY", false)
            }
            Calendar.SUNDAY -> {
                return intent.getBooleanExtra("SUNDAY", false)
            }
        }
        return false
    }

    private fun startRescheduleAlarmsService(context: Context, intent: Intent) {
        val intentService = Intent(context, RescheduleAlarmService::class.java)
        Log.d("Alarm receiver", "startRescheduleAlarmsService: bat dau service alarm")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            context.startForegroundService(intentService)
        else
            context.startService(intentService)
    }

    private fun startAlarmService(context: Context, intent: Intent) {
        val intentService = Intent(context, AlarmService::class.java)
        intentService.putExtra("OFF", isOff)
        intentService.putExtra("ID", intent.getIntExtra("ID",-1))
        intentService.putExtra("HOUR", intent.getIntExtra("HOURS", 0))
        intentService.putExtra("MINUTE", intent.getIntExtra("MINUTES", 0))
        Log.d("Alarm receiver", "startAlarmsService: bat dau service alarm")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            context.startForegroundService(intentService)
        else
            context.startService(intentService)
    }

}