package com.dungtran.alarmclock.alarmdata

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update

class AlarmRepository(private val alarmDAO: AlarmDAO) {
//    var allData: LiveData<List<Alarm>> = alarmDAO.getAllAlarm()
    fun getAllData(): LiveData<List<Alarm>> = alarmDAO.getAllAlarm()

    suspend fun insertAlarm(alarm: Alarm) {
        Log.d("Repository: ", "insertAlarm success")
        alarmDAO.insertAlarm(alarm)
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        alarmDAO.deleteAlarm(alarm)
    }

    suspend fun updateAlarm(alarm: Alarm) {
        alarmDAO.updateAlarm(alarm)
    }
}