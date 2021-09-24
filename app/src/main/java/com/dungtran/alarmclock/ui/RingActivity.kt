package com.dungtran.alarmclock.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.dungtran.alarmclock.R
import com.dungtran.alarmclock.broadcastreceiver.AlarmReceiver
import com.dungtran.alarmclock.databinding.ActivityRingBinding
import com.dungtran.alarmclock.service.AlarmService

class RingActivity : AppCompatActivity() {
    lateinit var binding: ActivityRingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("Ring Activity", "onCreate: start ring activity")
        setContentView(R.layout.activity_ring)
        binding = ActivityRingBinding.inflate(layoutInflater)

        val hourString = String.format("%02d", intent.getIntExtra("HOUR", 0))
        val minuteString = String.format("%02d", intent.getIntExtra("MINUTE", 0))

        Log.d("Ring activity", "onCreate: $hourString:$minuteString")

        binding.tvAlarmTime.text = "$hourString:$minuteString"

        binding.btnDismiss.setOnClickListener {
            val intentRing = Intent(applicationContext, AlarmReceiver::class.java)
            intentRing.putExtra("DISMISS", true)
            applicationContext.sendBroadcast(intentRing)
            finish()
        }

        binding.btnSnooze.setOnClickListener {

        }
    }
}