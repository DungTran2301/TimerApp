package com.dungtran.alarmclock.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.dungtran.alarmclock.R
import com.dungtran.alarmclock.notification.AlarmNotification.Companion.ALARM_CHANNEL_ID
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
        val notiricationIntent = Intent(this, RingActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notiricationIntent, 0)
        var notification: Notification = NotificationCompat.Builder(this, ALARM_CHANNEL_ID)
            .setContentTitle("Báo thức")
            .setContentText("Ring Ring")
            .setSmallIcon(R.drawable.ic_baseline_doorbell_24)
            .setContentIntent(pendingIntent)
            .build()

        mediaPlayer.start()

        val pattern = longArrayOf(0, 100, 1000)
        vibrator.vibrate(pattern, 0)
        startForeground(1, notification)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        vibrator.cancel()
    }
}