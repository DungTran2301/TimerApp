package com.dungtran.alarmclock.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dungtran.alarmclock.alarmdata.Alarm
import com.dungtran.alarmclock.databinding.ItemAlarmBinding

class AlarmAdapter(
    private val clickListener: (alarm: Alarm) -> Unit,
    private val deleteAlarm: (alarm: Alarm) -> Unit,
    private val changeStatus: (alarm: Alarm) -> Unit,
    val context: Context
) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

//    var listAlarms: List<Alarm> = mutableListOf()
    var listAlarms: List<Alarm> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bind(alarm: Alarm){
            val hourString = String.format("%02d", alarm.hours)
            val minuteString = String.format("%02d", alarm.minutes)
            binding.tvItemAlarmTime.text = "$hourString:$minuteString"
            binding.tvItemCalender.text = alarm.getRecurrenceText()
            binding.switchItemAlarmStarted.isChecked = alarm.isStart

            binding.root.setOnClickListener {
                clickListener(alarm)
            }

            binding.root.setOnLongClickListener {
                deleteAlarm(alarm)
                true
            }

            binding.switchItemAlarmStarted.setOnCheckedChangeListener { _, _ ->
                changeStatus(alarm)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listAlarms[position])
    }

    override fun getItemCount(): Int = listAlarms.size

}

