package com.dungtran.alarmclock.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dungtran.alarmclock.R
import com.dungtran.alarmclock.alarmdata.Alarm
import com.dungtran.alarmclock.databinding.FragmentSetAlarmBinding
import com.dungtran.alarmclock.model.AlarmLiveDataFactory
import com.dungtran.alarmclock.model.AlarmViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SetAlarmFragment : Fragment() {
    lateinit var binding: FragmentSetAlarmBinding
    private val viewModel:  AlarmViewModel by viewModels { AlarmLiveDataFactory(requireContext()) }
    var isRecurring = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tpkGetAlarmTime.setIs24HourView(true)

        binding.btnRecurring.setOnClickListener {
            onRecurringClick()
        }

        if (viewModel.position != -1) {
            setInformationForView()
        }


        binding.btnExitSetAlarm.setOnClickListener {
            Log.d("SetAlarmFragment: ", "cn button exit click")
            findNavController().navigate(R.id.action_setAlarmFragment_to_action_alarms)
        }

        binding.btnSaveSetAlarm.setOnClickListener {

            Log.d("set alarm fragment: ", "save click")
            if (viewModel.position == -1) {
                Log.d("set alarm fragment: ", "save new alarm")
                if (isRecurring) {
                    if (!checkRecurrence()) {
                        Toast.makeText(context, "Bạn chưa chọn ngày!", Toast.LENGTH_LONG).show()
                    } else
                        addAlarm()
                } else
                    addAlarm()
            }
            else {
                lifecycleScope.launch(Dispatchers.IO){
                    val alarm = viewModel.allData.value!!.get(viewModel.position)
                    val alarmUpdated = setUpdateAlarm(alarm)
                    Log.d("Set alarm fragment", "${alarm.hours} - ${alarm.minutes}")
                    viewModel.updateAlarm(alarmUpdated)
                }
            }

            findNavController().navigate(R.id.action_setAlarmFragment_to_action_alarms)

        }
    }
    private fun onRecurringClick() {
        if (!isRecurring) {
            isRecurring = true
            binding.btnRecurring.text = "Không lặp"
            binding.btnRecurring.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            binding.lnlCheckboxDay.visibility = View.VISIBLE
            binding.tvRecurrence.visibility = View.VISIBLE
        }
        else {
            isRecurring = false
            binding.btnRecurring.text = "Lặp lại"
            binding.btnRecurring.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_light))
            binding.lnlCheckboxDay.visibility = View.GONE
            binding.tvRecurrence.visibility = View.GONE
        }
    }

    private fun checkRecurrence(): Boolean {
        return binding.cbMon.isChecked ||
                binding.cbTue.isChecked ||
                binding.cbWed.isChecked ||
                binding.cbThu.isChecked ||
                binding.cbFri.isChecked ||
                binding.cbSat.isChecked ||
                binding.cbSun.isChecked
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun addAlarm() {
        var alarm = getInformation()
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.insertAlarm(alarm)
        }

        lifecycleScope.launch(Dispatchers.Default){
            Log.d("Set Alarm fragment", "add - schedule alarm")
            alarm.alarmSchedule(requireContext())
        }


    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getInformation(): Alarm {
        return Alarm(
            binding.tpkGetAlarmTime.hour,
            binding.tpkGetAlarmTime.minute,
            true,
            isRecurring,
            binding.cbMon.isChecked,
            binding.cbTue.isChecked,
            binding.cbWed.isChecked,
            binding.cbThu.isChecked,
            binding.cbFri.isChecked,
            binding.cbSat.isChecked,
            binding.cbSun.isChecked
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setUpdateAlarm(alarm: Alarm): Alarm {
        alarm.hours = binding.tpkGetAlarmTime.hour
        alarm.minutes = binding.tpkGetAlarmTime.minute
        alarm.isRecurrence = isRecurring
        alarm.monday = binding.cbMon.isChecked
        alarm.tuesday = binding.cbTue.isChecked
        alarm.wednesday = binding.cbWed.isChecked
        alarm.thursday = binding.cbThu.isChecked
        alarm.friday = binding.cbFri.isChecked
        alarm.saturday = binding.cbSat.isChecked
        alarm.sunday = binding.cbSun.isChecked

        lifecycleScope.launch(Dispatchers.Default){
            alarm.alarmSchedule(requireContext())
        }
        return alarm
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setInformationForView() {
        val updateAlarm = viewModel.allData.value!![viewModel.position]

        binding.tpkGetAlarmTime.hour = updateAlarm.hours
        binding.tpkGetAlarmTime.minute = updateAlarm.minutes
        binding.tvDisplayTime.text = updateAlarm.getRecurrenceText()
        isRecurring = updateAlarm.isRecurrence
        if (updateAlarm.isRecurrence) {
            binding.cbMon.isChecked = updateAlarm.monday
            binding.cbTue.isChecked = updateAlarm.tuesday
            binding.cbWed.isChecked = updateAlarm.wednesday
            binding.cbThu.isChecked = updateAlarm.thursday
            binding.cbFri.isChecked = updateAlarm.friday
            binding.cbSat.isChecked = updateAlarm.saturday
            binding.cbSun.isChecked = updateAlarm.sunday
            binding.btnRecurring.text = "Không lặp"
            binding.btnRecurring.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            binding.lnlCheckboxDay.visibility = View.VISIBLE
        }
    }
}