package com.dungtran.alarmclock.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dungtran.alarmclock.R
import com.dungtran.alarmclock.alarmdata.Alarm
import com.dungtran.alarmclock.databinding.FragmentSetAlarmBinding
import com.dungtran.alarmclock.model.AlarmViewModel
import kotlinx.coroutines.launch

class SetAlarmFragment : Fragment() {
    lateinit var binding: FragmentSetAlarmBinding
    lateinit var viewModel: AlarmViewModel
    var isRecurring = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetAlarmBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(AlarmViewModel::class.java)
        return inflater.inflate(R.layout.fragment_set_alarm, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRecurring.setOnClickListener {
            onRecurringClick()
        }


        if (viewModel.position == -1) {
            binding.btnSaveSetAlarm.setOnClickListener {
                if (isRecurring) {
                    if (!checkRecurrence()) {
                        Toast.makeText(context, "Bạn chưa chọn ngày!", Toast.LENGTH_LONG).show()
                    }
                    else
                        addAlarm()
                }
                else
                    addAlarm()
            }
        }
        else {
            val updateAlarm = viewModel.allData.value?.get(viewModel.position)
            binding.tvDisplayTime.text = updateAlarm?.getRecurrenceText()
            isRecurring = !updateAlarm!!.isRecurrence
            if (updateAlarm!!.isRecurrence) {
                binding.cbMon.isChecked = updateAlarm.monday
                binding.cbTue.isChecked = updateAlarm.tuesday
                binding.cbWed.isChecked = updateAlarm.wednesday
                binding.cbThu.isChecked = updateAlarm.thursday
                binding.cbFri.isChecked = updateAlarm.friday
                binding.cbSat.isChecked = updateAlarm.saturday
                binding.cbSun.isChecked = updateAlarm.sunday
            }

            viewModel.update(updateAlarm!!)
        }

        binding.btnExitSetAlarm.setOnClickListener {
//            findNavController().navigate(R.id.action_setAlarmFragment_to_action_alarms)
        }

    }
    private fun onRecurringClick() {
        if (isRecurring) {
            isRecurring = false
            binding.btnRecurring.text = "Không lặp"
            binding.btnRecurring.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            binding.lnlCheckboxDay.visibility = View.VISIBLE
            binding.tvRecurrence.visibility = View.VISIBLE
        }
        else {
            isRecurring = true
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
        var alarm = Alarm(
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.insertAlarm(alarm)
        }
        alarm.alarmSchedule(requireContext())

    }
}