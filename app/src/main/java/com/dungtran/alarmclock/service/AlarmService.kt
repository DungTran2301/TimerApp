package com.dungtran.alarmclock.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.os.Vibrator
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dungtran.alarmclock.R
import com.dungtran.alarmclock.notification.CountDownNotification
import com.dungtran.alarmclock.ui.RingActivity

class AlarmService : Service(){
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator

    @Override
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer.create(this, R.raw.ring)
        mediaPlayer.isLooping = true

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent!!.getBooleanExtra("DISMISS", false)) {
            Log.d("Alarm service", "onStartCommand: alarm service stop")
            mediaPlayer.stop()
            vibrator.cancel()
        }

        val notificationIntent = Intent(this, RingActivity::class.java)
        notificationIntent.putExtra("HOUR", intent.getIntExtra("HOUR",0))
        notificationIntent.putExtra("MINUTE", intent.getIntExtra("MINUTE",0))
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification: Notification = NotificationCompat.Builder(this, CountDownNotification.channelId)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentTitle("Báo thức")
            .setContentText("Ring Ring")
            .setSilent(true)
            .setSmallIcon(R.drawable.ic_baseline_doorbell_24)
            .setContentIntent(pendingIntent)
            .build()
        mediaPlayer.start()

        Log.d("Alarm service", "onStartCommand: check music bug")

//        val pattern = longArrayOf(0, 100, 1000)
//        vibrator.vibrate(pattern, 0)
        startForeground(1, notification)
        Log.d("Alarm service", "onStartCommand: start foreground success")

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        vibrator.cancel()
    }
}