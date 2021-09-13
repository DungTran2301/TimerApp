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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dungtran.alarmclock.databinding.FragmentCountDownBinding
import com.dungtran.alarmclock.model.CountDownViewModel
import com.dungtran.alarmclock.model.CountUpViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.math.roundToInt

class CountDownFragment : Fragment() {
    lateinit var binding: FragmentCountDownBinding
    private var isCounting = false

    lateinit var countDownViewModel: CountDownViewModel

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
        countDownViewModel = ViewModelProvider(requireActivity()).get(CountDownViewModel::class.java)

        countDownViewModel.timeDisplay.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            binding.tvTimeRunning.text = it
        })

        countDownViewModel.isTimeOut.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            if (it) taskComplete()
        })

        setTimer()

        binding.btnStartCountDown.setOnClickListener {
            getTimeAndStart(view)
        }

        binding.btnStopAndContinueCountDown.setOnClickListener {
            stopContinueTapped()
        }

        binding.btnExit.setOnClickListener {
                exitTimer()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getTimeAndStart(view: View) {
        val hours = binding.timePicker.hour
        val minutes = binding.timePicker.minute
        if (minutes != 0 || hours != 0) {
            startCounting(0, minutes, hours)
        }
        else
            Snackbar.make(view, "Bạn chưa chọn thời gian!", 3000).show()
    }

    private fun startCounting(seconds: Int, minutes: Int, hours: Int) {

        countDownViewModel.setTime(hours, minutes, seconds)

        binding.btnStartCountDown.visibility = View.GONE
        binding.btnExit.visibility = View.VISIBLE
        binding.btnStopAndContinueCountDown.visibility = View.VISIBLE
        binding.timePicker.visibility  = View.GONE
        binding.tvTimeRunning.visibility = View.VISIBLE
        stopContinueTapped()
    }

    private fun exitTimer() {
        countDownViewModel.resetTime()
        isCounting = false
        countDownViewModel.setTime(0, 0, 0)
        binding.btnStartCountDown.visibility = View.VISIBLE
        binding.btnExit.visibility = View.GONE
        binding.btnStopAndContinueCountDown.visibility = View.GONE
        binding.timePicker.visibility  = View.VISIBLE
        binding.tvTimeRunning.visibility = View.GONE
    }

    private fun stopContinueTapped() {
        if (isCounting) {
            isCounting = false
            binding.btnStopAndContinueCountDown.text = "tiếp tục"
            binding.btnStopAndContinueCountDown.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_light))
            countDownViewModel.taskComplete()
        }
        else {
            isCounting = true
            binding.btnStopAndContinueCountDown.text = "dừng"
            binding.btnStopAndContinueCountDown.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            countDownViewModel.startTimer()
        }
    }

    private fun taskComplete(){
        countDownViewModel.taskComplete()
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

