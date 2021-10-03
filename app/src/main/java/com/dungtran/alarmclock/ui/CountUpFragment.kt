package com.dungtran.alarmclock.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dungtran.alarmclock.R
import com.dungtran.alarmclock.databinding.FragmentCountUpBinding
import com.dungtran.alarmclock.viewmodel.CountUpViewModel


class CountUpFragment : Fragment() {
    lateinit var binding: FragmentCountUpBinding
    lateinit var countUpViewModel: CountUpViewModel

    private var isTimeStart: Boolean = false
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countUpViewModel = ViewModelProvider(requireActivity()).get(CountUpViewModel::class.java)

        countUpViewModel.timeResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.tvTimer.text = it
        })


        binding.btnStartContinue.setOnClickListener {
            startStopTapped()
        }

        binding.btnReset.setOnClickListener {
            if (!isTimeStart)
                reset()
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun startStopTapped() {

        if (isTimeStart) {
            isTimeStart = false
            binding.btnStartContinue.text = "tiếp tục"
            binding.btnStartContinue.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_light))
            binding.btnReset.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_light))
            countUpViewModel.cancelTimeTask()
        }
        else {
            isTimeStart = true
            binding.btnStartContinue.text = "dừng"
            binding.btnStartContinue.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            binding.btnReset.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_gray))
            countUpViewModel.startTimer()
        }
    }

    private fun reset() {
        countUpViewModel.resetTime()
        binding.btnStartContinue.text = "bắt đầu"
        binding.tvTimer.text = "00:00.00"
        binding.btnStartContinue.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_light))
    }

}