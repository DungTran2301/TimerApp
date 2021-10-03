package com.dungtran.alarmclock.service

import android.app.Activity
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
import com.dungtran.alarmclock.alarmdata.AlarmDatabase
import com.dungtran.alarmclock.broadcastreceiver.AlarmReceiver
import com.dungtran.alarmclock.notification.CountDownNotification
import com.dungtran.alarmclock.ui.MainActivity
import com.dungtran.alarmclock.ui.RingActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        if (intent!!.getBooleanExtra("OFF", false)) {
            Log.d("Alarm service", "onStartCommand: alarm service stop")
            mediaPlayer.stop()
            vibrator.cancel()

            stopSelf()
            val id = intent.getIntExtra("ID", -1)
            Log.d("Alarm Service", "onStartCommand: $id")

            if(id != -1){
                val alarmDao = AlarmDatabase.getDatabase(applicationContext).alarmDAO()
                CoroutineScope(Dispatchers.IO).launch {
                    val alarm = alarmDao.getSingleAlarm(id)
                    Log.d("Alarm service", "alarm infor ${alarm}")
                    if (!alarm.isRecurrence) alarm.isStart = false
                    alarmDao.updateAlarm(alarm)
                    val intentChangeStatus = Intent(applicationContext, MainActivity::class.java)
                    intentChangeStatus.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    applicationContext.startActivity(intentChangeStatus)
                }
            }

        }
        else {
            sentNotification(intent)
        }
        return START_STICKY
    }

    private fun sentNotification(intent: Intent) {
        Log.d("Alarm service", "sentNotification: current alarm ID ${intent.getIntExtra("ID", -1)}")

        val notificationIntent = Intent(this, RingActivity::class.java)
        notificationIntent.putExtra("HOUR", intent.getIntExtra("HOUR", 0))
        notificationIntent.putExtra("MINUTE", intent.getIntExtra("MINUTE", 0))
        notificationIntent.putExtra("ID", intent.getIntExtra("ID", -1))
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification: Notification =
            NotificationCompat.Builder(this, CountDownNotification.channelId)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle("Báo thức")
                .setContentText("Ring Ring")
                .setSilent(true)
                .setSmallIcon(R.drawable.ic_baseline_doorbell_24)
                .setContentIntent(pendingIntent)
                .build()
        mediaPlayer.start()


        //        val pattern = longArrayOf(0, 100, 1000)
        //        vibrator.vibrate(pattern, 0)
        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        vibrator.cancel()
    }
}