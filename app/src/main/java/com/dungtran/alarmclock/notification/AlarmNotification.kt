package com.dungtran.alarmclock.notification

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class AlarmNotification : Application() {

    val ALARM_CHANNEL_ID: String = "ALARM_CHANNEL_ID"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel (
                ALARM_CHANNEL_ID,
                "Alarm service channel",
                NotificationManager.IMPORTANCE_DEFAULT
            );

            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(serviceChannel)
        }
    }

}