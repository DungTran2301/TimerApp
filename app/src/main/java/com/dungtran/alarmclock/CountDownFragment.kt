package com.dungtran.alarmclock

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker.OnValueChangeListener
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dungtran.alarmclock.databinding.FragmentCountDownBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.math.roundToInt

class CountDownFragment : Fragment() {
    lateinit var binding: FragmentCountDownBinding
    var isCounting = false
    var onCounting = false
    lateinit var timer: Timer
    lateinit var timerTask: TimerTask
    var time = 0.0

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountDownBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timer = Timer()
        setTimer()
        binding.btnStartCountDown.setOnClickListener {
            var hours = binding.timePicker.hour
            var minutes = binding.timePicker.minute
            if (minutes != 0 || hours != 0) {
                startCounting(0, minutes, hours)
            }
            else
                Snackbar.make(view, "Bạn chưa chọn thời gian!", 3000).show()
        }

        binding.btnStopAndContinueCountDown.setOnClickListener {
            stopContinueTapped()
        }

        binding.btnExit.setOnClickListener {
                exitTimer()
        }
    }

    private fun startCounting(seconds: Int, minutes: Int, hours: Int) {

        time = (hours * 3600 + minutes * 60 + seconds).toDouble()

        binding.btnStartCountDown.visibility = View.GONE
        binding.btnExit.visibility = View.VISIBLE
        binding.btnStopAndContinueCountDown.visibility = View.VISIBLE
        binding.timePicker.visibility  = View.GONE
        binding.tvTimeRunning.visibility = View.VISIBLE
        stopContinueTapped()
    }

    private fun exitTimer() {
        if (timerTask != null) {
            timerTask.cancel()
            isCounting = false
            time = 0.0
            binding.tvTimeRunning.text = "00:00:00"
            binding.btnStartCountDown.visibility = View.VISIBLE
            binding.btnExit.visibility = View.GONE
            binding.btnStopAndContinueCountDown.visibility = View.GONE
            binding.timePicker.visibility  = View.VISIBLE
            binding.tvTimeRunning.visibility = View.GONE
        }
    }

    private fun stopContinueTapped() {
        if (isCounting) {
            isCounting = false
            binding.btnStopAndContinueCountDown.text = "tiếp tục"
            binding.btnStopAndContinueCountDown.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_light))
            timerTask.cancel()
        }
        else {
            isCounting = true
            binding.btnStopAndContinueCountDown.text = "dừng"
            binding.btnStopAndContinueCountDown.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            startTimer()
        }
    }

    private fun startTimer() {
        timerTask = object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    time--
                    binding.tvTimeRunning.text = getTimerText()
                }
            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, 1000)
    }

    private fun getTimerText() : String {
        if(time <= 0) taskComplete()

        val rounded = time.roundToInt()

        val seconds = rounded % 60
        val minutes = rounded / 60 % 60
        val hours = rounded / 3600

        return formatTime(seconds, minutes, hours)
    }

    private fun formatTime(seconds: Int, minutes: Int, hours: Int): String {
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds)
    }

    private fun taskComplete(){
        timerTask.cancel()
        binding.btnStartCountDown.visibility = View.VISIBLE
        binding.btnExit.visibility = View.GONE
        binding.btnStopAndContinueCountDown.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setTimer() {
        binding.timePicker.setIs24HourView(true)
        binding.timePicker.hour = 0
        binding.timePicker.minute = 0
    }
}

