package com.dungtran.alarmclock.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.dungtran.alarmclock.alarmdata.Alarm
import com.dungtran.alarmclock.alarmdata.AlarmDatabase
import com.dungtran.alarmclock.alarmdata.AlarmRepository

class RescheduleAlarmService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val alarmDAO = AlarmDatabase.getDatabase(applicationContext).alarmDAO()
        val alarmRepository = AlarmRepository(alarmDAO)

        alarmRepository.getAllData().value!!.let {
            for (alarm in it) {
                if (alarm.isStart) {
                    alarm.alarmSchedule(applicationContext)
                }
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}