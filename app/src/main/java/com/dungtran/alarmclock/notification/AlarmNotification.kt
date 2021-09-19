package com.dungtran.alarmclock.notification

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class AlarmNotification : Application() {
    companion object {
        val ALARM_CHANNEL_ID: String = "ALARM_CHANEL_ID"
    }

    override fun onCreate() {
        super.onCreate()

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var serviceChannel = NotificationChannel (
                ALARM_CHANNEL_ID,
                "Alarm service channel",
                NotificationManager.IMPORTANCE_DEFAULT
            );

            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(serviceChannel)
        }
    }

}