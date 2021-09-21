package com.dungtran.alarmclock.alarmdata

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Alarm::class], version = 1, exportSchema = false)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDAO(): AlarmDAO
    companion object {
        private const val NAME_DATABASE = "alarm_database"

        @Volatile
        private var INSTANCE: AlarmDatabase? = null

        fun getDatabase(context: Context): AlarmDatabase {
            return INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmDatabase::class.java,
                    NAME_DATABASE)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}