package com.dungtran.alarmclock.alarmdata

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update

class AlarmRepository(application: Application) {
    var allData: LiveData<ArrayList<Alarm>> //= alarmDAO.getAllAlarm()
    private lateinit var alarmDAO: AlarmDAO

    init {
        var alarmDatabase = AlarmDatabase.getDatabase(application)
        alarmDAO = alarmDatabase.alarmDAO()
        allData = alarmDAO.getAllAlarm()
    }
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