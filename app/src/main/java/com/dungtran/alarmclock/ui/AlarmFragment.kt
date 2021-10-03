package com.dungtran.alarmclock.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dungtran.alarmclock.R
import com.dungtran.alarmclock.adapter.AlarmAdapter
import com.dungtran.alarmclock.alarmdata.Alarm
import com.dungtran.alarmclock.databinding.FragmentAlarmBinding
import com.dungtran.alarmclock.viewmodel.AlarmLiveDataFactory
import com.dungtran.alarmclock.viewmodel.AlarmViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AlarmFragment : Fragment() {
    lateinit var binding: FragmentAlarmBinding
    private val alarmViewModel: AlarmViewModel by viewModels { AlarmLiveDataFactory(requireContext()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddAlarm.setOnClickListener {
            moveToAddAlarm()
        }

        val adapter = AlarmAdapter({
            alarmViewModel.hostAlarm = it
            findNavController().navigate(R.id.action_action_alarms_to_setAlarmFragment)
        },{
            context?.let { it1 ->
                deleteAlarm(alarm = it, it1)
            }

        }, {
//            context?.let { it1 ->
//                alarmViewModel.changeStatus(it, it1)
//            }
            if (it.isStart) {
                context?.let { ctx ->
                    it.cancelAlarm(ctx)
                    alarmViewModel.updateAlarm(it)
                }
            } else {
                context?.let { ctx ->
                    it.alarmSchedule(ctx)
                    alarmViewModel.updateAlarm(it)
                }
            }

        }, requireContext())

        alarmViewModel.allData.observe(viewLifecycleOwner){
            Log.d("Alarm Fragment", "Size of list Alarm ${it.size}")
            adapter.listAlarms = it
            adapter.notifyDataSetChanged()
        }
        binding.rcvAlarms.adapter = adapter
    }

    private fun moveToAddAlarm () {
        alarmViewModel.hostAlarm = null
        findNavController().navigate(R.id.action_action_alarms_to_setAlarmFragment)
    }

    private fun deleteAlarm(alarm: Alarm, context: Context){
        Log.d("AlarmFragment", "Delete Alarm at")
        binding.layoutDelete.visibility = View.VISIBLE
        binding.btnCancelDelete.setOnClickListener {
            binding.layoutDelete.visibility = View.GONE
        }
        binding.btnDelete.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                alarmViewModel.deleteAlarm(alarm, context)
            }
            binding.layoutDelete.visibility = View.GONE
        }
    }

//    private fun changeStatus(alarm: Alarm, context: Context) {
//        alarm.isStart = !alarm.isStart
//        Log.d("Alarm Fragment", "${alarm.hours}:${alarm.minutes} - change status")
//        lifecycleScope.launch(Dispatchers.IO) {
//            alarmViewModel.updateAlarm(alarm)
//        }
//    }

}