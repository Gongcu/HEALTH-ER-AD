package com.health.myapplication.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.health.myapplication.dao.BodyWeightDao
import com.health.myapplication.dao.RecordDateDao
import com.health.myapplication.model.BodyWeight
import com.health.myapplication.model.RecordDate

@Database(entities = [RecordDate::class], version = 3)
abstract class RecordDateDatabase() : RoomDatabase() {
    abstract fun recordDateDao(): RecordDateDao

    companion object {
        private var INSTANCE: RecordDateDatabase? = null
        private val sLock = Any()

        @VisibleForTesting
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table
                database.execSQL(
                        "CREATE TABLE dateTable_new (" +
                                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "date TEXT DEFAULT ((datetime('now'))))")
                // Copy the data
                database.execSQL(("INSERT INTO dateTable_new (_id, date) "
                        + "SELECT _id, date "
                        + "FROM dateTable"))
                // Remove the old table
                database.execSQL("DROP TABLE dateTable")
                database.execSQL("ALTER TABLE dateTable_new RENAME TO dateTable");
            }
        }

        fun getInstance(context: Context): RecordDateDatabase? {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            RecordDateDatabase::class.java, "dateTable.db")
                            .addMigrations(MIGRATION_2_3)
                            .build()
                }
                return INSTANCE
            }
        }
    }
}