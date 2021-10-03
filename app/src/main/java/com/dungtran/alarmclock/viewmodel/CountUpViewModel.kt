package com.dungtran.alarmclock.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class CountUpViewModel : ViewModel() {
    private var time = 0.0
    private var timerTask: TimerTask? = null
    private var timer = Timer()

    private val _timeResult = MutableLiveData<String>()
    val timeResult: LiveData<String> = _timeResult


    fun startTimer() {
        timerTask = object : TimerTask() {
            override fun run() {
                time++
                getTimerText()
            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, 10)
    }

    private fun getTimerText() {
        val rounded = time.toInt()
        val millis = rounded % 100
        val seconds = rounded / 100 % 60
        val minutes = rounded / 100 / 60 % 60
        _timeResult.postValue(formatTime(millis, seconds, minutes))
    }
    private fun formatTime(millis: Int, seconds: Int, minutes: Int): String {
        return String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "." + String.format("%02d", millis)
    }

    fun cancelTimeTask(){
        timerTask!!.cancel()
    }
    fun resetTime(){
        timerTask?.let{
            cancelTimeTask()
            time = 0.0
        }
    }






}