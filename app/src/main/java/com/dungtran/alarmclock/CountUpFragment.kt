package com.dungtran.alarmclock

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dungtran.alarmclock.databinding.FragmentCountUpBinding
import java.util.*
import kotlin.math.roundToInt


class CountUpFragment : Fragment() {
    lateinit var binding: FragmentCountUpBinding
    var timerStart: Boolean = false
    lateinit var timer: Timer
    lateinit var timerTask: TimerTask
    var time = 0.0

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timer = Timer()
        binding.btnStartContinue.setOnClickListener {
            startStopTapped()
        }

        binding.btnReset.setOnClickListener {
            if (!timerStart)
                resetTimer()
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun startStopTapped() {
        if (timerStart) {
            timerStart = false
            binding.btnStartContinue.text = "tiếp tục"
            binding.btnStartContinue.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_light))
            binding.btnReset.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_light))
            timerTask.cancel()
        }
        else {
            timerStart = true
            binding.btnStartContinue.text = "dừng"
            binding.btnStartContinue.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            binding.btnReset.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_gray))
            startTimer()
        }
    }

    private fun resetTimer() {
        if (timerTask != null) {
            timerTask.cancel()
            binding.btnStartContinue.text = "bắt đầu"
            time = 0.0
            binding.tvTimer.text = "00:00.00"
            binding.btnStartContinue.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_light))
        }
    }
    private fun startTimer() {
        timerTask = object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    time++
                    binding.tvTimer.text = getTimerText()
                }
            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, 10)

    }
    private fun getTimerText() : String {

        val rounded = time.roundToInt()
        val millis = rounded % 100
        val seconds = rounded / 100 % 60
        val minutes = rounded / 100 / 60 % 60

        return formatTime(millis, seconds, minutes)
    }
    private fun formatTime(millis: Int, seconds: Int, minutes: Int): String {
        return String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "." + String.format("%02d", millis)
    }
}