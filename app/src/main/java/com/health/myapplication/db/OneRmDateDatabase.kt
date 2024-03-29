package com.health.myapplication.db

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.health.myapplication.dao.OneRmDateDao
import com.health.myapplication.entity.one_rm.OneRmDate


@Database(entities = [OneRmDate::class], version = 2)
abstract class OneRmDateDatabase() : RoomDatabase() {
    abstract fun oneRMDateDao(): OneRmDateDao

    companion object {
        private var INSTANCE: OneRmDateDatabase? = null
        private val sLock = Any()

        @VisibleForTesting
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table
                database.execSQL(
                    "CREATE TABLE caldateTable_new (" +
                            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "date TEXT DEFAULT ((datetime('now'))))")
                // Copy the data
                database.execSQL(("INSERT INTO caldateTable_new (_id, date) "
                        + "SELECT _id, date "
                        + "FROM caldateTable"))
                // Remove the old table
                database.execSQL("DROP TABLE caldateTable")
                database.execSQL("ALTER TABLE caldateTable_new RENAME TO caldateTable");
            }
        }

        fun getInstance(application: Application): OneRmDateDatabase? {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(application.applicationContext,
                            OneRmDateDatabase::class.java, "caldateTable.db")
                            .addMigrations(MIGRATION_1_2)
                            .build()
                }
                return INSTANCE
            }
        }
    }
}