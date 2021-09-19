 package com.dungtran.alarmclock.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.dungtran.alarmclock.R
import com.dungtran.alarmclock.broadcastreceiver.NotificationReceive
import com.dungtran.alarmclock.notification.CountDownNotification


 class CountDownService : Service() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let{
            val key = it.getIntExtra("exit",0)
            if (key == 1) {
                stopSelf()
            }
        }
        var stringNotification = "Đếm ngược"
        sendNotification(stringNotification)
        return START_NOT_STICKY

    }

    @SuppressLint("ResourceAsColor")
    private fun sendNotification(stringNotification: String) {
        val broadcastIntent = Intent(this, NotificationReceive::class.java)
        broadcastIntent.putExtra("exit", 1)

        val actionIntent = PendingIntent.getBroadcast(this,
        0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, CountDownNotification.channelId)
                .setContentTitle(stringNotification)
                .setContentText("Đã hết giờ!")
                .setSmallIcon(R.drawable.ic_baseline_doorbell_24)
                .addAction(R.mipmap.ic_launcher, "Exit", actionIntent)
                .setColor(R.color.red)
                .setAutoCancel(false)
                .build()
        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}