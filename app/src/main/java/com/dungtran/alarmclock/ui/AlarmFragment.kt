package com.dungtran.alarmclock.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dungtran.alarmclock.R
import com.dungtran.alarmclock.adapter.AlarmAdapter
import com.dungtran.alarmclock.databinding.FragmentAlarmBinding
import com.dungtran.alarmclock.model.AlarmLiveDataFactory
import com.dungtran.alarmclock.model.AlarmViewModel


class AlarmFragment : Fragment() {
    lateinit var binding: FragmentAlarmBinding
    private val alarmViewModel: AlarmViewModel by viewModels { AlarmLiveDataFactory(requireContext()) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddAlarm.setOnClickListener {
            moveToAddAlarm()
        }
        val adapter = AlarmAdapter{ _, position ->
            alarmViewModel.position = position
            findNavController().navigate(R.id.action_action_alarms_to_setAlarmFragment)
        }
        alarmViewModel.allData.observe(viewLifecycleOwner){
            Log.d("Alarm Fragment", "onViewCreated: ${it.size}")
            adapter.listAlarms = it
        }
        binding.rcvAlarms.adapter = adapter
    }
    private fun moveToAddAlarm () {
        alarmViewModel.position = -1
        findNavController().navigate(R.id.action_action_alarms_to_setAlarmFragment)
    }

}