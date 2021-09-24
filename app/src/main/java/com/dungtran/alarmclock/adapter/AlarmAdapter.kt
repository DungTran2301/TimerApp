package com.dungtran.alarmclock.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dungtran.alarmclock.R
import com.dungtran.alarmclock.alarmdata.Alarm
import com.dungtran.alarmclock.databinding.ItemAlarmBinding

class AlarmAdapter(
        private val clickListener: (position: Int) -> Unit,
        private val deleteAlarm: (position: Int) -> Unit,
        private val changeStatus: (position: Int) -> Unit
) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bind(position: Int){
            val alarm = listAlarms[position]
            val hourString = String.format("%02d", alarm.hours)
            val minuteString = String.format("%02d", alarm.minutes)
            binding.tvItemAlarmTime.text = "$hourString:$minuteString"
            binding.tvItemCalender.text = alarm.getRecurrenceText()
            binding.switchItemAlarmStarted.isChecked = alarm.isStart
            binding.root.setOnClickListener {
                clickListener(position)
            }
            binding.root.setOnLongClickListener {
                deleteAlarm(position)
                true
            }

//            binding.switchItemAlarmStarted.setOnCheckedChangeListener { _, _ ->
//                changeStatus(position)
//            }
        }

        fun changeItemStatus(position: Int) {
            binding.switchItemAlarmStarted.setOnCheckedChangeListener { _, _ ->
                changeStatus(position)
            }
            notifyItemChanged(position)
        }

    }
//    interface OnAlarmItemClicklistener {
//        fun OnItemClick(item: Alarm, position: Int)
//    }
    var listAlarms: List<Alarm> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
//        notifyItemChanged(position)
    }

    override fun getItemCount(): Int = listAlarms.size


}

