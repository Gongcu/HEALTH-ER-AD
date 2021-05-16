package com.health.myapplication.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.health.myapplication.dao.OneRmRecordDao
import com.health.myapplication.entity.one_rm.OneRmRecord

@Database(entities = [OneRmRecord::class], version = 2)
abstract class OneRmRecordDatabase() : RoomDatabase() {
    abstract fun oneRMRecordDao(): OneRmRecordDao

    companion object {
        private var INSTANCE: OneRmRecordDatabase? = null
        private val sLock = Any()

        @VisibleForTesting
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table
                database.execSQL("CREATE TABLE onermTable_new (" +
                                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "exercise TEXT NOT NULL, "+
                                "weight REAL NOT NULL, "+
                                "dateId INTEGER)")
                // Copy the data
                database.execSQL("INSERT INTO onermTable_new (_id, exercise,weight,dateid) "
                        + "SELECT _id, exercise,onerm,parentid "
                        + "FROM onermTable")

                // Remove the old table
                database.execSQL("DROP TABLE onermTable")
                database.execSQL("ALTER TABLE onermTable_new RENAME TO onermTable");
            }
        }

        fun getInstance(context: Context): OneRmRecordDatabase {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            OneRmRecordDatabase::class.java, "calculator.db")
                            .addMigrations(MIGRATION_1_2)
                            .build()
                }
                return INSTANCE!!
            }
        }
    }
}