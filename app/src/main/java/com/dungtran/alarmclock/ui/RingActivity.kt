package com.dungtran.alarmclock.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.dungtran.alarmclock.R
import com.dungtran.alarmclock.broadcastreceiver.AlarmReceiver
import com.dungtran.alarmclock.databinding.ActivityRingBinding
import com.dungtran.alarmclock.service.AlarmService

class RingActivity : AppCompatActivity() {
    lateinit var binding: ActivityRingBinding
    lateinit var hourString: String
    lateinit var minuteString: String
    private var idAlarm = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idAlarm = intent.getIntExtra("ID", -1)
        Log.d("Ring Activity", "onCreate: alarm Id $idAlarm")
        binding = ActivityRingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hourString = String.format("%02d", intent.getIntExtra("HOUR", 0))
        minuteString = String.format("%02d", intent.getIntExtra("MINUTE", 0))
        Log.d("Ring activity", "onCreate: $hourString:$minuteString")

        binding.tvAlarmTime.text = "$hourString:$minuteString"

        binding.btnDismiss.setOnClickListener {
            Log.d("Ring activity", "on Dismiss click")
            val intentRing = Intent(applicationContext, AlarmReceiver::class.java)
            intentRing.putExtra("OFF", true)
            intentRing.putExtra("ID", idAlarm)
            applicationContext.sendBroadcast(intentRing)
            finish()
        }
    }



}