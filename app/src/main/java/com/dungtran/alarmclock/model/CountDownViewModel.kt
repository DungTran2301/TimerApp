package com.dungtran.alarmclock.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.math.roundToInt

class CountDownViewModel : ViewModel() {
    private var timer = Timer()
    private var timerTask: TimerTask? = null
    private var time = 0.0

    private val _timeDisplay = MutableLiveData<String>()
    val timeDisplay: LiveData<String> = _timeDisplay

    private var _isTimeOut = MutableLiveData<Boolean>()
    val isTimeOut: LiveData<Boolean> = _isTimeOut

    fun startTimer() {
        _isTimeOut.postValue(false)

        timerTask = object : TimerTask() {
            override fun run() {
                time--
                getTimerText()
            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, 1000)
    }

    private fun getTimerText() {
        if(time <= 0) taskComplete()
        val rounded = time.roundToInt()
        if (rounded <= 0) _isTimeOut.postValue(true)
        val seconds = rounded % 60
        val minutes = rounded / 60 % 60
        val hours = rounded / 3600

        _timeDisplay.postValue(formatTime(seconds, minutes, hours))
    }

    private fun formatTime(seconds: Int, minutes: Int, hours: Int): String {
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds)
    }

    fun setTime(hours: Int, minutes: Int, seconds: Int) {
        time = (hours * 3600 + minutes * 60 + seconds).toDouble()
    }

    fun taskComplete() {
        timerTask!!.cancel()
    }

    fun resetTime() {
        timerTask!!.let {
            taskComplete()
            time = 0.0
        }
    }

}