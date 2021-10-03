package com.dungtran.alarmclock.alarmdata

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmRepository(private val alarmDAO: AlarmDAO) {
//    var allData: LiveData<List<Alarm>> = alarmDAO.getAllAlarm()
    fun getAllData(): LiveData<List<Alarm>> = alarmDAO.getAllAlarm()

    suspend fun insertAlarm(alarm: Alarm) {
        alarmDAO.insertAlarm(alarm)
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        Log.d("Alarm Repository", "delete alarm: deleted")
        alarmDAO.deleteAlarm(alarm)
    }

    fun updateAlarm(alarm: Alarm) {
        Log.d("Alarm Repository", "updateAlarm: in update")
        CoroutineScope(Dispatchers.IO).launch {
            alarmDAO.updateAlarm(alarm)
        }

    }
}