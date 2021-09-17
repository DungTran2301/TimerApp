package com.dungtran.alarmclock.alarmdata

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update

class AlarmRepository(private val alarmDAO: AlarmDAO) {
    val readAllData: LiveData<ArrayList<Alarm>> = alarmDAO.getAllAlarm()

    suspend fun insertAlarm(alarm: Alarm) {
        alarmDAO.insertAlarm(alarm)
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        alarmDAO.deleteAlarm(alarm)
    }

    suspend fun updateAlarm(alarm: Alarm) {
        alarmDAO.updateAlarm(alarm)
    }
}