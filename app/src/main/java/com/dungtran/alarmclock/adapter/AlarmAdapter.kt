package com.dungtran.alarmclock.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dungtran.alarmclock.alarmdata.Alarm
import com.dungtran.alarmclock.databinding.ItemAlarmBinding

class AlarmAdapter(
        private val clickListener: (item: Alarm, position: Int) -> Unit
) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val alarm = listAlarms[position]
            binding.tvItemAlarmTime.text = "${alarm.hours} : ${alarm.minutes}"
            binding.tvItemCalender.text = "${alarm.getRecurrenceText()}"
            binding.switchItemAlarmStarted.isChecked = alarm.isStart
            binding.root.setOnLongClickListener {
                clickListener(alarm, position)
                true
            }
        }

    }
//    interface OnAlarmItemClicklistener {
//        fun OnItemClick(item: Alarm, position: Int)
//    }
    var listAlarms = ArrayList<Alarm>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = listAlarms.size
}