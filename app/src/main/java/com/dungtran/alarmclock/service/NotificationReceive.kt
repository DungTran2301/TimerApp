package com.dungtran.alarmclock.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceive : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        intent?.let{
            val id = intent.getIntExtra("exit",0)
            val intentService = Intent(context, CountDownService::class.java)
            intentService.putExtra("exit", id)
            context?.startService(intentService)
        }

    }
}