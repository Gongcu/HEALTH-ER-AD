package com.health.myapplication.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.health.myapplication.dao.AlarmDao
import com.health.myapplication.entity.etc.Alarm

@Database(entities = [Alarm::class], version = 2)
abstract class AlarmDatabase() : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao

    companion object {
        private var INSTANCE: AlarmDatabase? = null
        private val sLock = Any()

        @VisibleForTesting
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table
                database.execSQL((
                        "CREATE TABLE alarm_new (" +
                                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "minute INTEGER NOT NULL, " +
                                "second INTEGER NOT NULL)"))
                // Copy the data
                database.execSQL(("INSERT INTO alarm_new (_id, minute, second) "
                        + "SELECT _id, minute, second "
                        + "FROM alarmTable"))
                // Remove the old table
                database.execSQL("DROP TABLE alarmTable")
                database.execSQL("ALTER TABLE alarm_new RENAME TO alarm");
            }
        }

        fun getInstance(context: Context): AlarmDatabase? {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            AlarmDatabase::class.java, "alarmTable.db")
                            .addMigrations(MIGRATION_1_2)
                            .build()
                }
                return INSTANCE
            }
        }
    }
}