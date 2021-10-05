package com.dungtran.alarmclock.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.dungtran.alarmclock.alarmdata.Alarm
import com.dungtran.alarmclock.databinding.ItemAlarmBinding
import com.dungtran.alarmclock.viewmodel.AlarmViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class AlarmAdapter(
    val changeStatus: (Alarm) -> Unit,
    private val clickListener: (alarm: Alarm) -> Unit,
    private val deleteAlarm: (alarm: Alarm) -> Unit,
    var context: Context
) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

//    var listAlarms: List<Alarm> = mutableListOf()
    var listAlarms: List<Alarm> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

//    fun updateData(list: List<Alarm>){
//        if(listAlarms.isEmpty()){
//            listAlarms = list
//            notifyDataSetChanged()
//        }else{
//            listAlarms = list
//        }
//    }


    inner class ViewHolder(private val binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.KITKAT)
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
                Log.d("alarm adapter", "bind: ${alarm.alarmId}")
                changeStatus(alarm)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listAlarms[position])
    }
    override fun getItemCount(): Int = listAlarms.size
}

